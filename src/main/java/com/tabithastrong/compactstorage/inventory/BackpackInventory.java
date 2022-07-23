package com.tabithastrong.compactstorage.inventory;

import com.tabithastrong.compactstorage.util.CompactStorageInventoryImpl;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

public class BackpackInventory implements Inventory, CompactStorageInventoryImpl  {
    public DefaultedList<ItemStack> items;
    public int inventoryWidth;
    public int inventoryHeight;

    private final Hand hand;
    private final PlayerEntity player;

    public BackpackInventory(NbtCompound itemsNbt, Hand hand, PlayerEntity player) {
        this.hand = hand;
        this.player = player;

        this.fromTag(itemsNbt);
    }

    @Override
    public void clear() {
        this.items.clear();
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
    public int size() {
        return getInventoryWidth() * getInventoryHeight();
    }

    @Override
    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getStack(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(this.items, slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.items, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.items.set(slot, stack);
    }

    @Override
    public void markDirty() {
        
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    public void fromTag(NbtCompound tag) {
        this.inventoryWidth = tag.contains("inventory_width") ? tag.getInt("inventory_width") : 9;
        this.inventoryHeight = tag.contains("inventory_height") ? tag.getInt("inventory_height") : 6;
        
        this.items = DefaultedList.ofSize(inventoryWidth * inventoryHeight, ItemStack.EMPTY);
        readItemsFromTag(this.items, tag);
    }

    public NbtCompound toTag() {
        NbtCompound tag = new NbtCompound();
        tag.putInt("inventory_width", inventoryWidth);
        tag.putInt("inventory_height", inventoryHeight);

        writeItemsToTag(this.items, tag);

        return tag;
    }

    @Override
    public void onOpen(PlayerEntity player) {
        Inventory.super.onOpen(player);
        player.playSound(SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.PLAYERS, 1f, 1f);
    }

    @Override
    public void onClose(PlayerEntity player) {
        Inventory.super.onClose(player);

        if(!player.getStackInHand(hand).hasNbt()) {
            player.getStackInHand(hand).setNbt(new NbtCompound());
        }

        player.getStackInHand(hand).getNbt().put("Backpack", toTag());
        player.playSound(SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.PLAYERS, 1f, 1f);
    }
}