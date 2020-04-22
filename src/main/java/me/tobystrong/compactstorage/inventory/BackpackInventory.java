package me.tobystrong.compactstorage.inventory;

import me.tobystrong.compactstorage.util.CompactStorageInventoryImpl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

public class BackpackInventory implements IInventory, CompactStorageInventoryImpl {
    public NonNullList<ItemStack> items;
    public int inventory_width;
    public int inventory_height;

    private final Hand hand;
    private final PlayerEntity player;

    public BackpackInventory(CompoundNBT items_tag, Hand hand, PlayerEntity player) {
        this.hand = hand;
        this.player = player;

        this.fromTag(items_tag);
    }

    @Override
    public void clear() {
        this.items.clear();
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
    public int getSizeInventory() {
        return getInventoryWidth() * getInventoryHeight();
    }

    @Override
    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return items.get(slot);
    }


    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        return ItemStackHelper.getAndSplit(this.items, slot, amount);
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        return ItemStackHelper.getAndRemove(this.items, slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        this.items.set(slot, stack);
    }

    @Override
    public void markDirty() {
        
    }



    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return true;
    }

    public void fromTag(CompoundNBT tag) {
        this.inventory_width = tag.contains("inventory_width") ? tag.getInt("inventory_width") : 9;
        this.inventory_height = tag.contains("inventory_height") ? tag.getInt("inventory_height") : 6;
        
        this.items = NonNullList.withSize(inventory_width * inventory_height, ItemStack.EMPTY);
        readItemsFromTag(this.items, tag);
    }

    public CompoundNBT toTag() {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("inventory_width", inventory_width);
        tag.putInt("inventory_height", inventory_height);

        writeItemsToTag(this.items, tag);

        return tag;
    }

    @Override
    public void openInventory(PlayerEntity player) {
        player.playSound(SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.PLAYERS, 1f, 1f);
    }

    @Override
    public void closeInventory(PlayerEntity player) {
        if(!player.getHeldItem(hand).hasTag()) {
            player.getHeldItem(hand).setTag(new CompoundNBT());
        }

        player.getHeldItem(hand).getTag().put("Backpack", toTag());
        player.playSound(SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.PLAYERS, 1f, 1f);
    }
}