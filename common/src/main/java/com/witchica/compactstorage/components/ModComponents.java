package com.witchica.compactstorage.components;

import com.mojang.serialization.Codec;
import com.witchica.compactstorage.util.IndexedItemStack;
import net.blay09.mods.balm.core.component.BalmDataComponentTypeRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;

import java.util.List;

public class ModComponents {
    public static Holder<DataComponentType<ResizableInventoryComponent>> RESIZABLE_INVENTORY_DATA;
    public static Holder<DataComponentType<Boolean>> RETAINING_DATA;
    public static Holder<DataComponentType<Boolean>> VOID_SLOT;
    public static Holder<DataComponentType<Integer>> ITEM_DRUM_SIZE;
    // Item-form contents (backpacks, and item-form pick-up for chests/barrels/drums), using a full
    // int slot index instead of vanilla DataComponents.CONTAINER's hard 256-entry cap - see
    // IndexedItemStack/IndexedItemStackHelper.
    public static Holder<DataComponentType<List<IndexedItemStack>>> UNBOUNDED_CONTAINER_DATA;
    public static Holder<DataComponentType<Boolean>> PRESERVES_ARRANGEMENT;
    public static Holder<DataComponentType<Integer>> SORT_PREFERENCE;

    public static void initialize(BalmDataComponentTypeRegistrar registrar) {
        RESIZABLE_INVENTORY_DATA = registrar.register("resizable_inventory_data", ResizableInventoryComponent.CODEC).asHolder();
        RETAINING_DATA = registrar.register("retaining_data", Codec.BOOL).asHolder();
        VOID_SLOT = registrar.register("void_slot_data", Codec.BOOL).asHolder();
        ITEM_DRUM_SIZE = registrar.register("item_drum_data", Codec.INT).asHolder();
        UNBOUNDED_CONTAINER_DATA = registrar.register("unbounded_container_data", IndexedItemStack.CODEC.listOf()).asHolder();
        PRESERVES_ARRANGEMENT = registrar.register("preserves_arrangement_data", Codec.BOOL).asHolder();
        SORT_PREFERENCE = registrar.register("sort_preference_data", Codec.INT).asHolder();
    }
}
