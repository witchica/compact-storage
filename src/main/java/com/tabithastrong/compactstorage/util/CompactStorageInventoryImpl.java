package com.tabithastrong.compactstorage.util;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;

public interface CompactStorageInventoryImpl {
    public default Inventory getInventory() {
        return (Inventory) this;
    }

    public int getInventoryWidth();
    public int getInventoryHeight();

    //custom Inventories.toTag implementation because the byte limit got hit lol
    default void writeItemsToTag(DefaultedList<ItemStack> inventory, NbtCompound tag) {
        NbtList listTag = new NbtList();

        for(int i = 0; i < inventory.size(); ++i) {
            ItemStack itemStack = (ItemStack)inventory.get(i);
            if (!itemStack.isEmpty()) {
                NbtCompound nbt = new NbtCompound();
                nbt.putInt("Slot", i);
                itemStack.writeNbt(nbt);
                listTag.add(nbt);
            }
        }

        tag.put("Items", listTag);
    }

    default void readItemsFromTag(DefaultedList<ItemStack> inventory, NbtCompound tag) {
        NbtList listTag = tag.getList("Items", 10);

        for(int i = 0; i < listTag.size(); ++i) {
            NbtCompound compoundTag = listTag.getCompound(i);
            int j = compoundTag.getInt("Slot");
            if (j >= 0 && j < inventory.size()) {
                inventory.set(j, ItemStack.fromNbt(compoundTag));
            }
        }
    }
}
