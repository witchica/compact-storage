package me.tobystrong.compactstorage.block.tile;

import me.tobystrong.compactstorage.CompactStorage;
import me.tobystrong.compactstorage.container.CompactChestContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CompactChestTileEntity extends TileEntity implements INamedContainerProvider, ICapabilitySerializable<CompoundNBT> {
    private LazyOptional<ItemStackHandler> inventory;
    public int width = 9;
    public int height = 3;

    public CompactChestTileEntity() {
        super(CompactStorage.COMPACT_CHEST_TILE_TYPE);
        inventory = LazyOptional.of(() -> new ItemStackHandler(9 * 3));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return inventory.cast();
        }

        return super.getCapability(cap);
    }

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

        this.inventory.ifPresent(inv -> compound.put("Inventory", inv.serializeNBT()));

        compound.putInt("width", width);
        compound.putInt("height", height);

        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);

        this.inventory.ifPresent(inv -> inv.deserializeNBT(nbt.getCompound("Inventory")));

        this.width = nbt.contains("width") ? nbt.getInt("width") : 9;
        this.height = nbt.contains("height") ? nbt.getInt("height") : 3;

        updateItemStackHandlerSize(width, height);
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

        inventory.ifPresent(inv -> {
            if(inv.getSlots() != width * height) {
                updateItemStackHandlerSize(width, height);
            }
        });

        return new CompactChestContainer(windowId, playerInventory, width, height, inventory.orElseThrow(NullPointerException::new));
    }
}
