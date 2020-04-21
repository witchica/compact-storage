package me.tobystrong.compactstorage.util;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.DefaultedList;

public interface CompactStorageInventoryImpl {
    public default Inventory getInventory() {
        return (Inventory) this;
    }

    public int getInventoryWidth();
    public int getInventoryHeight();

    //custom Inventories.toTag implementation because the byte limit got hit lol
    default void writeItemsToTag(DefaultedList<ItemStack> inventory, CompoundTag tag) {
        ListTag listTag = new ListTag();

        for(int i = 0; i < inventory.size(); ++i) {
            ItemStack itemStack = (ItemStack)inventory.get(i);
            if (!itemStack.isEmpty()) {
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.putInt("Slot", i);
                itemStack.toTag(compoundTag);
                listTag.add(compoundTag);
            }
        }

        tag.put("Items", listTag);
    }

    default void readItemsFromTag(DefaultedList<ItemStack> inventory, CompoundTag tag) {
        ListTag listTag = tag.getList("Items", 10);

        for(int i = 0; i < listTag.size(); ++i) {
            CompoundTag compoundTag = listTag.getCompound(i);
            int j = compoundTag.getInt("Slot");
            if (j >= 0 && j < inventory.size()) {
                inventory.set(j, ItemStack.fromTag(compoundTag));
            }
        }
    }
}