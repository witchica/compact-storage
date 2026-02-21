package com.witchica.compactstorage.mod;

import com.witchica.compactstorage.menu.CompactStorageMenuData;
import com.witchica.compactstorage.menu.GenericCompactStorageMenu;
import net.blay09.mods.balm.world.BalmMenuFactory;
import net.blay09.mods.balm.world.inventory.BalmMenuTypeRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

public class CompactStorageMenuTypes {
    public static Holder<MenuType<GenericCompactStorageMenu>> COMPACT_STORAGE_MENU;

    public static void initialize(BalmMenuTypeRegistrar menuTypes) {
        COMPACT_STORAGE_MENU = menuTypes.register("generic", new BalmMenuFactory<GenericCompactStorageMenu, CompactStorageMenuData>() {
            @Override
            public GenericCompactStorageMenu create(int i, Inventory inventory, CompactStorageMenuData data) {
                return new GenericCompactStorageMenu(i, inventory, data);
            }

            @Override
            public StreamCodec<RegistryFriendlyByteBuf, CompactStorageMenuData> getStreamCodec() {
                return CompactStorageMenuData.STREAM_CODEC.cast();
            }
        }).asHolder();
    }
}
