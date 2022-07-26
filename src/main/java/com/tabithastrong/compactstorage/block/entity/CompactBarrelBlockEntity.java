package com.tabithastrong.compactstorage.block.entity;

import com.tabithastrong.compactstorage.CompactStorage;
import com.tabithastrong.compactstorage.block.CompactBarrelBlock;

import com.tabithastrong.compactstorage.inventory.CompactStorageItemHandler;
import com.tabithastrong.compactstorage.screen.CompactChestScreenHandler;
import com.tabithastrong.compactstorage.util.CompactStorageInventoryImpl;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class CompactBarrelBlockEntity extends RandomizableContainerBlockEntity implements CompactStorageInventoryImpl {
    private CompactStorageItemHandler inventory;
    private LazyOptional<IItemHandler> inventoryHandlerLazyOptional = LazyOptional.of(()-> inventory);

    public int inventoryWidth = 9;
    public int inventoryHeight = 6;

    public int playersUsing = 0;
    public int playersUsingOld = 0;
    public boolean isOpen = false;

    public CompactBarrelBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CompactStorage.COMPACT_BARREL_ENTITY_TYPE.get(), blockPos, blockState);
        this.inventory = new CompactStorageItemHandler(inventoryWidth * inventoryHeight);
    }

    @Override
    public int getContainerSize() {
        return inventoryWidth * inventoryHeight;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return inventory.getStacks();
    }

    @Override
    protected void setItems(NonNullList<ItemStack> stackList) {
        inventory = new CompactStorageItemHandler(stackList);
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("container.barrel");
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return new CompactChestScreenHandler(syncId, playerInventory, writeToByteBuf());
    }

    @Override
    public int getInventoryWidth() {
        return inventoryWidth;
    }

    @Override
    public int getInventoryHeight() {
        return inventoryHeight;
    }

    @Override
    public void startOpen(Player player) {
        super.startOpen(player);

        if(!player.isSpectator()) {
            playersUsing += 1;
        }
    }

    @Override
    public void stopOpen(Player player) {
        super.stopOpen(player);

        if(!player.isSpectator()) {
            playersUsing -= 1;
        }
    }

    public void resizeInventory() {
        NonNullList<ItemStack> stacks = inventory.getStacks();
        inventory.setSize(inventoryWidth * inventoryHeight);

        for(int i = 0; i < stacks.size(); i++) {
            inventory.setStackInSlot(i, stacks.get(i));
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        this.inventoryWidth = nbt.contains("inventory_width") ? nbt.getInt("inventory_width") : 9;
        this.inventoryHeight = nbt.contains("inventory_height") ? nbt.getInt("inventory_height") : 3;

        this.inventory = new CompactStorageItemHandler(inventoryWidth * inventoryHeight);

        // Backwards compat
        if(nbt.contains("Inventory")) {
            this.inventory.deserializeNBT(nbt.getCompound("Inventory"));
        } else if (nbt.contains("Items")){
            this.inventory.deserializeNBT(nbt);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);

        nbt.put("Inventory", inventory.serializeNBT());
        nbt.putInt("inventory_width", inventoryWidth);
        nbt.putInt("inventory_height", inventoryHeight);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    public static void tick(Level world, BlockPos pos, BlockState state, CompactBarrelBlockEntity compactChestBlockEntity) {

        if(compactChestBlockEntity.playersUsing > 0 && compactChestBlockEntity.playersUsingOld == 0) {
            compactChestBlockEntity.isOpen = true;
            world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BARREL_OPEN, SoundSource.BLOCKS, 1f, 0.9f, true);
            world.setBlockAndUpdate(pos, state.setValue(CompactBarrelBlock.OPEN, true));
        }else if(compactChestBlockEntity.playersUsing == 0 && compactChestBlockEntity.playersUsingOld != 0) {
            compactChestBlockEntity.isOpen = false;
            world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BARREL_CLOSE, SoundSource.BLOCKS, 1f, 0.9f, true);
            world.setBlockAndUpdate(pos, state.setValue(CompactBarrelBlock.OPEN, false));
        }

        compactChestBlockEntity.playersUsingOld = compactChestBlockEntity.playersUsing;
    }

    public FriendlyByteBuf writeToByteBuf() {
        FriendlyByteBuf packetByteBuf = new FriendlyByteBuf(Unpooled.buffer());
        writeScreenOpeningData(packetByteBuf);
        return packetByteBuf;
    }

    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player playerEntity) {
        return new CompactChestScreenHandler(syncId, playerInventory, writeToByteBuf());
    }

    @Override
    public Component getDisplayName() {
        return getName();
    }

    public void writeScreenOpeningData(FriendlyByteBuf buf) {
        buf.writeInt(0);
        buf.writeBlockPos(this.worldPosition);
    }

    public boolean increaseSize(int x, int y) {
        if((inventoryWidth > 23 && x > 0) || (inventoryHeight > 11 && y > 0)) {
            return false;
        }

        inventoryWidth += x;
        inventoryHeight += y;

        resizeInventory();
        setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 1);

        return true;
    }

    /** IItemHandler **/

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return inventoryHandlerLazyOptional.cast();
        }

        return super.getCapability(cap, side);
    }
}