package com.witchica.compactstorage.block.entity;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.block.CompactBarrelBlock;
import com.witchica.compactstorage.screen.CompactChestScreenHandler;
import com.witchica.compactstorage.util.CompactStorageInventoryImpl;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
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


public class CompactBarrelBlockEntity extends RandomizableContainerBlockEntity implements ExtendedScreenHandlerFactory, CompactStorageInventoryImpl {
    private NonNullList<ItemStack> inventory;

    public int inventoryWidth = 9;
    public int inventoryHeight = 6;

    public int playersUsing = 0;
    public int playersUsingOld = 0;
    public boolean isOpen = false;

    public InventoryStorage inventoryStorage;
    public CompactBarrelBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CompactStorage.COMPACT_BARREL_ENTITY_TYPE, blockPos, blockState);
        this.inventory = NonNullList.withSize(inventoryWidth * inventoryHeight, ItemStack.EMPTY);
        this.inventoryStorage = InventoryStorage.of(this, null);
    }

    @Override
    public int getContainerSize() {
        return inventoryWidth * inventoryHeight;
    }

    
    protected NonNullList<ItemStack> getInvStackList() {
        return inventory;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> stackList) {
        inventory = stackList;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.barrel");
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

    public void resizeInventory(boolean copy_contents) {
        NonNullList<ItemStack> newInventory = NonNullList.withSize(inventoryWidth * inventoryHeight, ItemStack.EMPTY);

        if(copy_contents) {
            NonNullList<ItemStack> list = this.inventory;

            for(int i = 0; i < list.size(); i++) {
                newInventory.set(i, list.get(i));
            }
        }

        this.inventory = newInventory;
        this.inventoryStorage = InventoryStorage.of(this, null);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        this.inventoryWidth = nbt.contains("inventory_width") ? nbt.getInt("inventory_width") : 9;
        this.inventoryHeight = nbt.contains("inventory_height") ? nbt.getInt("inventory_height") : 3;

        this.inventory = NonNullList.withSize(inventoryWidth * inventoryHeight, ItemStack.EMPTY);
        this.inventoryStorage = InventoryStorage.of(this, null);
        readItemsFromTag(inventory, nbt);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        writeItemsToTag(inventory, nbt);

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
        FriendlyByteBuf packetByteBuf = PacketByteBufs.create();
        packetByteBuf.writeInt(0);
        packetByteBuf.writeBlockPos(getBlockPos());

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

    @Override
    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
        buf.writeInt(0);
        buf.writeBlockPos(this.worldPosition);
    }

    public boolean increaseSize(int x, int y) {
        if((inventoryWidth > 23 && x > 0) || (inventoryHeight > 11 && y > 0)) {
            return false;
        }

        inventoryWidth += x;
        inventoryHeight += y;

        resizeInventory(true);
        setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 1);

        return true;
    }
}