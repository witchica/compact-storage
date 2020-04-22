package me.tobystrong.compactstorage.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;

public interface CompactStorageInventoryImpl {
    public default IInventory getInventory() {
        return (IInventory) this;
    }

    public int getInventoryWidth();
    public int getInventoryHeight();

    //custom Inventories.toTag implementation because the byte limit got hit lol
    default void writeItemsToTag(NonNullList<ItemStack> inventory, CompoundNBT tag) {
        ListNBT listTag = new ListNBT();

        for(int i = 0; i < inventory.size(); ++i) {
            ItemStack itemStack = (ItemStack)inventory.get(i);
            if (!itemStack.isEmpty()) {
                CompoundNBT compoundTag = new CompoundNBT();
                compoundTag.putInt("Slot", i);
                itemStack.write(compoundTag);
                listTag.add(compoundTag);
            }
        }

        tag.put("Items", listTag);
    }

    default void readItemsFromTag(NonNullList<ItemStack> inventory, CompoundNBT tag) {
        ListNBT listTag = tag.getList("Items", 10);

        for(int i = 0; i < listTag.size(); ++i) {
            CompoundNBT compoundTag = listTag.getCompound(i);
            int j = compoundTag.getInt("Slot");
            if (j >= 0 && j < inventory.size()) {
                inventory.set(j, ItemStack.read(compoundTag));
            }
        }
    }
}