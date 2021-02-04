package me.tobystrong.compactstorage.block.tile;

import me.tobystrong.compactstorage.CompactStorage;
import me.tobystrong.compactstorage.container.CompactChestContainer;
import me.tobystrong.compactstorage.container.CompactStorageBaseContainer;
import me.tobystrong.compactstorage.util.CompactStorageUtilMethods;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public abstract class BaseTileEntity extends TileEntity implements INamedContainerProvider, ICapabilitySerializable<CompoundNBT>, ITickableTileEntity {
    public LazyOptional<ItemStackHandler> inventory;
    public int width = 9;
    public int height = 3;

    protected int playersUsing = 0;

    public BaseTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        inventory = LazyOptional.of(() -> new ItemStackHandler(9 * 3));
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

        if(!this.inventory.isPresent()) {
            this.inventory = LazyOptional.of(() -> new ItemStackHandler(9 * 3));
        }

        inventory = CompactStorageUtilMethods.validateHandlerSize(inventory, width, height);
    }

    /*
        This function is used by the block to determine if an upgrade is allowed
     */
    public CompactStorageUtilMethods.UpgradeStatus handleUpgradeItem(ItemStack upgrade) {
        //make sure not in use to prevent weird behaviour
        if(playersUsing == 0) {
            if(upgrade.getItem() == CompactStorage.upgrade_row) {
                if(width == 24) {
                    return CompactStorageUtilMethods.UpgradeStatus.MAX_WIDTH;
                } else {
                    width += 1;
                    return CompactStorageUtilMethods.UpgradeStatus.SUCCESS;
                }
            }

            if(upgrade.getItem() == CompactStorage.upgrade_column) {
                if(height == 12) {
                    return CompactStorageUtilMethods.UpgradeStatus.MAX_HEIGHT;
                } else {
                    height += 1;
                    return CompactStorageUtilMethods.UpgradeStatus.SUCCESS;
                }
            }
        } else {
            return CompactStorageUtilMethods.UpgradeStatus.IN_USE;
        }

        return CompactStorageUtilMethods.UpgradeStatus.ERROR;
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
    public void tick() {
        this.playersUsing = 0;

        //checks for entities within the AABB defined and adds them to the playersUsing if they have this chest open
        float d = 5f;
        List<PlayerEntity> players = world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(pos.add(-d, -d, -d), pos.add(d, d, d)));
        for(PlayerEntity p : players) {
            if(p.openContainer instanceof CompactStorageBaseContainer) {
                CompactStorageBaseContainer container = (CompactStorageBaseContainer) p.openContainer;
                if(container.chestInventory == inventory.orElseThrow(NullPointerException::new)) {
                    playersUsing++;
                }
            }
        }
    }

    @Override
    public  abstract ITextComponent getDisplayName();

    @Override
    public abstract Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity);
}
