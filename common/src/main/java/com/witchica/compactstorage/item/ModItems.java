package com.witchica.compactstorage.item;

import com.witchica.compactstorage.block.ModBlocks;
import net.blay09.mods.balm.world.item.BalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.world.item.BalmItemRegistrar;
import net.blay09.mods.balm.world.item.DeferredItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import com.witchica.compactstorage.CompactStorage;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.WoodType;
import util.StorageTypes;

import java.util.Arrays;

import static com.witchica.compactstorage.CompactStorage.id;

public class ModItems {

    public static void initializeItems(BalmItemRegistrar items) {

    }

    public static void initializeCreativeTabs(BalmCreativeModeTabRegistrar creativeModeTabs) {
        creativeModeTabs.register("metal", builder ->
                builder.title(Component.translatable(id(CompactStorage.MOD_ID).toLanguageKey("itemGroup")))
                        .icon(() -> ModBlocks.metalCompactChests.get(StorageTypes.RED).createStack())
                        .displayItems((displayParameters, output) -> {
                            StorageTypes.stream().forEach(storageType -> {
                                if(!storageType.isWooden()) {
                                    output.accept(ModBlocks.metalCompactChests.get(storageType));
                                }
                            });
                        })
        );

        creativeModeTabs.register("wood", builder ->
                builder.title(Component.translatable(id(CompactStorage.MOD_ID).toLanguageKey("itemGroup")))
                        .icon(() -> ModBlocks.metalCompactChests.get(StorageTypes.RED).createStack())
                        .displayItems((displayParameters, output) -> {
                            StorageTypes.stream().forEach(storageType -> {
                                if(storageType.isWooden()) {
                                    output.accept(ModBlocks.metalCompactChests.get(storageType));
                                }
                            });
                        })
        );
    }

}
