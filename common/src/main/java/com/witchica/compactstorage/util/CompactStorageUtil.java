package com.witchica.compactstorage.util;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;

public class CompactStorageUtil {
    public static void loadItemsFromOldVersionIfPresent(ValueInput input, NonNullList<ItemStack> items) {
        ValueInput.TypedInputList<CompoundTag> targetItemTag = input.listOrEmpty("Items", CompoundTag.CODEC);

        if(targetItemTag.isEmpty()) {
            targetItemTag = input.listOrEmpty("Inventory", CompoundTag.CODEC);
        }

        if(!targetItemTag.isEmpty()) {
            CompoundTag fakeInventory = new CompoundTag();
            ListTag newList = new ListTag();
            for(CompoundTag subtag : targetItemTag) {
                // change of case here, annoying
                subtag.putInt("count", (int) subtag.getByteOr("Count", (byte) 1));
                // remove the old one
                subtag.remove("Count");
                newList.add(subtag);
            }

            // Create new tag that the actual loading system can just use off rip
            fakeInventory.put("Items", newList);

            ValueInput fakeInventoryInput = TagValueInput.create(ProblemReporter.DISCARDING, input.lookup(), fakeInventory);
            ContainerHelper.loadAllItems(fakeInventoryInput, items);
        }
    }
}
