package com.tabithastrong.compactstorage.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CompactStorageItemHandler extends ItemStackHandler {

    public CompactStorageItemHandler(int size) {
        super(size);
    }

    public CompactStorageItemHandler(NonNullList<ItemStack> items) {
        super(items);
    }

    public NonNullList<ItemStack> getStacks() {
        return this.stacks;
    }
}
