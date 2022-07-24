package com.tabithastrong.compactstorage.util;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public interface CompactStorageInventoryImpl {
    public default Container getInventory() {
        return (Container) this;
    }

    public int getInventoryWidth();
    public int getInventoryHeight();

    //custom Inventories.toTag implementation because the byte limit got hit lol
    default void writeItemsToTag(NonNullList<ItemStack> inventory, CompoundTag tag) {
        ListTag listTag = new ListTag();

        for(int i = 0; i < inventory.size(); ++i) {
            ItemStack itemStack = (ItemStack)inventory.get(i);
            if (!itemStack.isEmpty()) {
                CompoundTag nbt = new CompoundTag();
                nbt.putInt("Slot", i);
                itemStack.save(nbt);
                listTag.add(nbt);
            }
        }

        tag.put("Items", listTag);
    }

    default void readItemsFromTag(NonNullList<ItemStack> inventory, CompoundTag tag) {
        ListTag listTag = tag.getList("Items", 10);

        for(int i = 0; i < listTag.size(); ++i) {
            CompoundTag compoundTag = listTag.getCompound(i);
            int j = compoundTag.getInt("Slot");
            if (j >= 0 && j < inventory.size()) {
                inventory.set(j, ItemStack.of(compoundTag));
            }
        }
    }
}
