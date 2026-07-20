package com.witchica.compactstorage.util;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.ArrayList;
import java.util.List;

/**
 * Vanilla's ContainerHelper.saveAllItems/loadAllItems (used for chest/barrel/drum NBT) encode each
 * stack's slot as ExtraCodecs.UNSIGNED_BYTE, and DataComponents.CONTAINER/ItemContainerContents
 * (used for item-form contents - backpacks, and any storage picked up as an item) hard-caps at 256
 * entries and throws past it. Both are fine for vanilla's biggest container (a 54-slot double
 * chest); neither is fine once this mod's storages can run into the thousands of slots. This
 * mirrors both shapes with IndexedItemStack's full-int slot instead, so nothing wraps around or
 * gets rejected past 255/256.
 */
public class IndexedItemStackHelper {
    public static void saveAllItems(ValueOutput output, NonNullList<ItemStack> items) {
        ValueOutput.TypedOutputList<IndexedItemStack> list = output.list("Items", IndexedItemStack.CODEC);

        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if (!stack.isEmpty()) {
                list.add(new IndexedItemStack(i, stack));
            }
        }
    }

    public static void loadAllItems(ValueInput input, NonNullList<ItemStack> items) {
        for (IndexedItemStack entry : input.listOrEmpty("Items", IndexedItemStack.CODEC)) {
            if (entry.slot() >= 0 && entry.slot() < items.size()) {
                items.set(entry.slot(), entry.stack());
            }
        }
    }

    public static List<IndexedItemStack> toComponentList(List<ItemStack> items) {
        List<IndexedItemStack> result = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if (!stack.isEmpty()) {
                result.add(new IndexedItemStack(i, stack));
            }
        }

        return result;
    }

    public static void copyFromComponentList(List<IndexedItemStack> entries, List<ItemStack> items) {
        for (IndexedItemStack entry : entries) {
            if (entry.slot() >= 0 && entry.slot() < items.size()) {
                items.set(entry.slot(), entry.stack());
            }
        }
    }
}
