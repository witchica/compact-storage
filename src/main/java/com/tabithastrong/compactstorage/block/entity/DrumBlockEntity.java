package com.tabithastrong.compactstorage.block.entity;

import com.tabithastrong.compactstorage.CompactStorage;
import com.tabithastrong.compactstorage.inventory.DrumItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class DrumBlockEntity extends BlockEntity {
    private LazyOptional<DrumItemStackHandler> inventoryHandlerLazyOptional;
    public int clientItemCount;

    public Optional<ItemStack> clientDisplayStack = Optional.empty();
    public int clientStackSize;
    public DrumBlockEntity(BlockPos pos, BlockState state) {
        super(CompactStorage.DRUM_ENTITY_TYPE.get(), pos, state);
        inventoryHandlerLazyOptional = LazyOptional.of(() -> new DrumItemStackHandler(this));
    }

    private void saveClientData(CompoundTag tag) {
        tag.putInt("ItemCount", getDrumItemStackHandler().getTotalItemCount());
        tag.putInt("StackSize", getDrumItemStackHandler().getMaxStackSize());

        ItemStack displayStack = getDrumItemStackHandler().getDisplayStack();
        tag.put("DisplayItem", displayStack.serializeNBT());
    }

    private void readClientData(CompoundTag tag) {
        this.clientItemCount = tag.getInt("ItemCount");
        this.clientStackSize = tag.getInt("StackSize");

        this.clientDisplayStack = Optional.of(ItemStack.of(tag.getCompound("DisplayItem")));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", inventoryHandlerLazyOptional.orElse(null).serializeNBT());
        saveClientData(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        inventoryHandlerLazyOptional.orElse(null).deserializeNBT(tag.getCompound("Inventory"));
        readClientData(tag);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return inventoryHandlerLazyOptional.cast();
        }

        return super.getCapability(cap);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return getCapability(cap);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        inventoryHandlerLazyOptional.invalidate();
    }

    public DrumItemStackHandler getDrumItemStackHandler() {
        return (DrumItemStackHandler) inventoryHandlerLazyOptional.orElse(null);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveClientData(tag);

        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        if(pkt.getTag() != null) {
            CompoundTag tag = pkt.getTag();
            readClientData(tag);
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if(hasLevel()) {
            getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);
        }
    }

    public String getTextToDisplay() {
        if(clientStackSize == 0) {
            return "Empty";
        }

        int numStacks = clientItemCount / clientStackSize;
        int leftover = clientItemCount % clientStackSize;

        if(numStacks == 0 & leftover == 0) {
            return "Empty";
        } else if(numStacks == 0) {
            return "" + leftover;
        } else if(leftover == 0) {
            return clientStackSize + " x " + numStacks;
        } else {
            return (clientStackSize + " x " + numStacks + " + " + leftover);
        }
    }
}
