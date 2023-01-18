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

    private final PlayerEntity player;
    private final int backpackSlot;
    private final boolean isInOffhand;

    public BackpackInventory(NbtCompound itemsNbt, PlayerEntity player, boolean isInOffhand) {
        this.backpackSlot = player.getInventory().selectedSlot;
        this.player = player;
        this.isInOffhand = isInOffhand;

        this.fromTag(itemsNbt);
    }


    public void resizeInventory(boolean copy_contents) {
        DefaultedList<ItemStack> newInventory = DefaultedList.ofSize(inventoryWidth * inventoryHeight, ItemStack.EMPTY);

        if(copy_contents) {
            DefaultedList<ItemStack> list = this.items;

            for(int i = 0; i < list.size(); i++) {
                newInventory.set(i, list.get(i));
            }
        }

        this.items = newInventory;
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

    public boolean increaseSize(int x, int y) {
        if((inventoryWidth > 23 && x > 0) || (inventoryHeight > 11 && y > 0)) {
            return false;
        }

        inventoryWidth += x;
        inventoryHeight += y;

        resizeInventory(true);
        markDirty();

        return true;
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
        PlayerInventory inventory = player.getInventory();

        if(isInOffhand) {
            if(!player.getStackInHand(Hand.OFF_HAND).hasNbt()) {
                player.getStackInHand(Hand.OFF_HAND).setNbt(new NbtCompound());
            }

            player.getStackInHand(Hand.OFF_HAND).getNbt().put("Backpack", toTag());
        } else {
            if(!inventory.getStack(backpackSlot).hasNbt()) {
                inventory.getStack(backpackSlot).setNbt(new NbtCompound());
            }

            inventory.getStack(backpackSlot).getNbt().put("Backpack", toTag());
        }
        player.playSound(SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.PLAYERS, 1f, 1f);
    }
}