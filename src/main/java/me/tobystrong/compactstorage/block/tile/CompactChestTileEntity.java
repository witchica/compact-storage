package me.tobystrong.compactstorage.block.tile;

import me.tobystrong.compactstorage.CompactStorage;
import me.tobystrong.compactstorage.container.CompactChestContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.*;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

/*
Only include on the client size
 */
@OnlyIn(
        value = Dist.CLIENT,
        _interface = IChestLid.class
)
public class CompactChestTileEntity extends TileEntity implements INamedContainerProvider, ICapabilitySerializable<CompoundNBT>, ITickableTileEntity, IChestLid {
    private LazyOptional<ItemStackHandler> inventory;
    public int width = 9;
    public int height = 3;

    public float lidAngle = 0f;
    public float prevLidAngle = 0f;
    private int playersUsing = 0;

    /*
        This is used to let the Block know what message to send the client once upgraded
     */
    public enum UpgradeStatus {
        SUCCESS,
        MAX_WIDTH,
        MAX_HEIGHT,
        IN_USE,
        ERROR;
    }

    public CompactChestTileEntity() {
        super(CompactStorage.COMPACT_CHEST_TILE_TYPE);
        inventory = LazyOptional.of(() -> new ItemStackHandler(9 * 3));
    }

    /*
        Provides capability information to other blocks and allow them to interact with us
     */
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return inventory.cast();
        }

        return super.getCapability(cap);
    }

    /*
        Update the size of our ItemStackHandler to handle upgrades
     */
    public void updateItemStackHandlerSize(int w, int h) {
        ItemStackHandler handler = inventory.orElseThrow(NullPointerException::new);
        ItemStackHandler newHandler = new ItemStackHandler(w * h);

        for(int i = 0; i < handler.getSlots(); i++) {
            newHandler.setStackInSlot(i, handler.getStackInSlot(i));
        }

        this.inventory = LazyOptional.of(() -> newHandler);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);

        //writes the data from ItemStack handler to NBT
        this.inventory.ifPresent(inv -> compound.put("Inventory", inv.serializeNBT()));

        compound.putInt("width", width);
        compound.putInt("height", height);

        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);

        //reads the data from nbt to our ItemStackHandler
        this.inventory.ifPresent(inv -> inv.deserializeNBT(nbt.getCompound("Inventory")));

        this.width = nbt.contains("width") ? nbt.getInt("width") : 9;
        this.height = nbt.contains("height") ? nbt.getInt("height") : 3;

        updateItemStackHandlerSize(width, height);
    }

    /*
        This function is used by the block to determine if an upgrade is allowed
     */
    public UpgradeStatus handleUpgradeItem(ItemStack upgrade) {
        //make sure not in use to prevent weird behaviour
        if(playersUsing == 0) {
            if(upgrade.getItem() == CompactStorage.upgrade_row) {
                if(width == 24) {
                    return UpgradeStatus.MAX_WIDTH;
                } else {
                    width += 1;
                    return UpgradeStatus.SUCCESS;
                }
            }

            if(upgrade.getItem() == CompactStorage.upgrade_column) {
                if(height == 12) {
                    return UpgradeStatus.MAX_HEIGHT;
                } else {
                    height += 1;
                    return UpgradeStatus.SUCCESS;
                }
            }
        } else {
            return UpgradeStatus.IN_USE;
        }

        return UpgradeStatus.ERROR;
    }

    /*
        Checks if the player is within a 5 block radius of the chest
     */
    public boolean canPlayerAccess(PlayerEntity entity) {
        if(entity.getPosX() > pos.getX() - 5 && entity.getPosY() > pos.getY() - 5 && entity.getPosZ() > pos.getZ() - 5) {
            if(entity.getPosX() < pos.getX() + 5 && entity.getPosY() < pos.getY() + 5 && entity.getPosZ() < pos.getZ() + 5) {
                return true;
            }
        }

        return false;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getPos(), 0, this.write(new CompoundNBT()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.read(world.getBlockState(pkt.getPos()), pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return write(super.getUpdateTag());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        read(world.getBlockState(pos), tag);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.compactstorage.compact_chest");
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
        // checks that the size matches before opening the container so we don't throw an exception
        inventory.ifPresent(inv -> {
            if(inv.getSlots() != width * height) {
                updateItemStackHandlerSize(width, height);
            }
        });

        return new CompactChestContainer(windowId, playerInventory, width, height, this, inventory.orElseThrow(NullPointerException::new));
    }

    @Override
    public void tick() {
        this.playersUsing = 0;

        //checks for entities within the AABB defined and adds them to the playersUsing if they have this chest open
        float d = 5f;
        List<PlayerEntity> players = world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(pos.add(-d, -d, -d), pos.add(d, d, d)));
        for(PlayerEntity p : players) {
            if(p.openContainer instanceof CompactChestContainer) {
                CompactChestContainer container = (CompactChestContainer) p.openContainer;
                if(container.chestInventory == inventory.orElseThrow(NullPointerException::new)) {
                    playersUsing++;
                }
            }
        }

        // sets the lid angle for animation
        this.prevLidAngle = lidAngle;

        if(playersUsing > 0 && lidAngle == 0f) {
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 1f, 1f, false);
        }

        if (playersUsing == 0 && this.lidAngle > 0.0F || playersUsing > 0 && this.lidAngle < 1.0F) {
            float f1 = this.lidAngle;
            if (playersUsing > 0) {
                this.lidAngle += 0.1F;
            } else {
                this.lidAngle -= 0.1F;
            }

            if (this.lidAngle > 1.0F) {
                this.lidAngle = 1.0F;
            }

            float f2 = 0.5F;
            if (this.lidAngle < 0.5F && f1 >= 0.5F) {
                world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 1f, 1f, false);
            }

            if (this.lidAngle < 0.0F) {
                this.lidAngle = 0.0F;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getLidAngle(float partialTicks) {
        return MathHelper.lerp(partialTicks, prevLidAngle, lidAngle);
    }
}
