package me.tobystrong.compactstorage.block.entity;

import me.tobystrong.compactstorage.CompactStorage;
import me.tobystrong.compactstorage.container.CompactChestContainer;
import me.tobystrong.compactstorage.util.CompactStorageInventoryImpl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

@OnlyIn(value = Dist.CLIENT, _interface = IChestLid.class)
public class CompactChestBlockEntity extends LockableLootTileEntity implements IChestLid, ITickableTileEntity, CompactStorageInventoryImpl {
    private NonNullList<ItemStack> inventory;

    public int inventory_width = 9;
    public int inventory_height = 6;

    public int players_using = 0;
    public int players_using_old = 0;
    public boolean is_open = false;

    public float lid_openness = 0f;

    public float last_lid_openness = 0f;

    public CompactChestBlockEntity() {
        super(CompactStorage.COMPACT_CHEST_ENTITY_TYPE);
        this.inventory = NonNullList.withSize(inventory_width * inventory_height, ItemStack.EMPTY);
    }

    @Override
    public ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.chest");
    }

    @Override
    public Container createMenu(int syncId, PlayerInventory playerInventory) {
        System.out.println("creating container");
        return new CompactChestContainer(CompactStorage.COMPACT_CHEST_CONTAINER_TYPE, syncId, playerInventory, (IInventory) this, inventory_width, inventory_height, null);
    }

    @Override
    public int getSizeInventory() {
        return inventory_width * inventory_height;
    }

    @Override
    public void openInventory(PlayerEntity player) {
        if(!player.isSpectator()) {
            players_using++;
        }
    }


    @Override
    public void closeInventory(PlayerEntity player) {
        if(!player.isSpectator()) {
            players_using--;
        }
    }

    public void resizeInventory(boolean copy_contents) {
        NonNullList<ItemStack> new_inventory = NonNullList.withSize(inventory_width * inventory_height, ItemStack.EMPTY);
        
        if(copy_contents) {
            NonNullList<ItemStack> list = this.inventory;

            for(int i = 0; i < list.size(); i++) {
                new_inventory.set(i, list.get(i));
            }
        }

        this.inventory = new_inventory;
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);

        writeItemsToTag(this.inventory, tag);

        tag.putInt("inventory_width", inventory_width);
        tag.putInt("inventory_height", inventory_height);
        return tag;
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);

        System.out.println((world.isRemote ? "client" : " Server") + " : " + (tag.contains("inventory_width") ? tag.getInt("inventory_width") : "DID NOT CONTAIN"));

        inventory_width = tag.contains("inventory_width") ? tag.getInt("inventory_width") : 9;
        inventory_height = tag.contains("inventory_height") ? tag.getInt("inventory_height") : 6;

        this.inventory = NonNullList.withSize(inventory_width * inventory_height, ItemStack.EMPTY);
        readItemsFromTag(this.inventory, tag);
    }

    @Override
    public int getInventoryWidth() {
        return inventory_width;
    }

    @Override
    public int getInventoryHeight() {
        return inventory_height;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullList) {
        this.inventory = nonNullList;
    }

    @Override
    public void tick() {
        last_lid_openness = lid_openness;

        if(players_using > 0 && players_using_old == 0) {
            is_open = true;
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 1f, 0.9f, true);
        }else if(players_using == 0 && players_using_old != 0) {
            is_open = false;
        }
        
        if (players_using == 0 && lid_openness >= 0.6f && lid_openness <= 0.7f) {
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 1f, 0.9f, true);
        }

        if(is_open && lid_openness < 0.5f) {
            lid_openness = Math.min(lid_openness += 0.25f, 1f);
        } else if(is_open && lid_openness < 1f) {
            lid_openness = Math.min(lid_openness += 0.1f, 1f);
        } else if(!is_open && lid_openness > 0.7f) {
            lid_openness = Math.max(lid_openness - 0.05f, 0f);
        } else if(!is_open && lid_openness > 0f) {
            lid_openness = Math.max(lid_openness - 0.15f, 0f);
        }

        players_using_old = players_using;
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        tag.putInt("inventory_width", inventory_width);
        tag.putInt("inventory_height", inventory_height);
        return super.getUpdateTag();
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag) {
        this.inventory_width = tag.getInt("inventory_width");
        this.inventory_height = tag.getInt("inventory_height");

        super.handleUpdateTag(tag);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 0, write(new CompoundNBT()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        read(pkt.getNbtCompound());
        super.onDataPacket(net, pkt);
    }


    @Override
    public void markDirty() {
        super.markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public float getLidAngle(float f) {
        return MathHelper.lerp(f, last_lid_openness, lid_openness);
    }
}
