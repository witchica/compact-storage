package com.witchica.compactstorage.mod;

import com.mojang.serialization.Codec;
import com.witchica.compactstorage.api.inventory.ResizableContainer;
import com.witchica.compactstorage.components.ResizableInventoryComponent;
import net.blay09.mods.balm.core.component.BalmDataComponentTypeRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;

public class CompactStorageComponents {
    public static Holder<DataComponentType<ResizableInventoryComponent>> RESIZABLE_INVENTORY_DATA;
    public static Holder<DataComponentType<Boolean>> RETAINING_DATA;
    public static Holder<DataComponentType<Boolean>> VOID_SLOT;
    public static Holder<DataComponentType<Integer>> ITEM_DRUM_SIZE;

    public static void initialize(BalmDataComponentTypeRegistrar registrar) {
        RESIZABLE_INVENTORY_DATA = registrar.register("resizable_inventory_data", ResizableInventoryComponent.CODEC).asHolder();
        RETAINING_DATA = registrar.register("retaining_data", Codec.BOOL).asHolder();
        VOID_SLOT = registrar.register("void_slot_data", Codec.BOOL).asHolder();
        ITEM_DRUM_SIZE = registrar.register("item_drum_data", Codec.INT).asHolder();
    }
}
