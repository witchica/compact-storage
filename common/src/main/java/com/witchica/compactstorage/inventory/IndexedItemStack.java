package com.witchica.compactstorage.inventory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

/**
 * Vanilla's ItemStackWithSlot equivalent, but with a plain int slot index instead of an unsigned
 * byte - see IndexedItemStackHelper for why that matters.
 */
public record IndexedItemStack(int slot, ItemStack stack) {
    public static final Codec<IndexedItemStack> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.INT.fieldOf("Slot").forGetter(IndexedItemStack::slot),
            ItemStack.MAP_CODEC.forGetter(IndexedItemStack::stack)
    ).apply(builder, IndexedItemStack::new));
}
