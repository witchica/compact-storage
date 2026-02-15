package com.witchica.compactstorage.item;

import com.witchica.compactstorage.block.CompactStorageBlocks;
import net.blay09.mods.balm.world.item.BalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.world.item.BalmItemRegistrar;
import net.minecraft.network.chat.Component;
import util.StorageTypes;

import static com.witchica.compactstorage.CompactStorage.id;

public class CompactStorageItems {

    public static void initializeItems(BalmItemRegistrar items) {

    }

    public static void initializeCreativeTabs(BalmCreativeModeTabRegistrar creativeModeTabs) {
        creativeModeTabs.register("metal", builder ->
                builder.title(Component.translatable(id("general").toLanguageKey("itemGroup")))
                        .icon(() -> CompactStorageBlocks.metalCompactChests.get(StorageTypes.RED).createStack())
                        .displayItems((displayParameters, output) -> {
                            StorageTypes.stream().forEach(storageType -> {
                                if(!storageType.isWooden()) {
                                    output.accept(CompactStorageBlocks.metalCompactChests.get(storageType));
                                }
                            });
                        })
        );

        creativeModeTabs.register("wood", builder ->
                builder.title(Component.translatable(id("wood").toLanguageKey("itemGroup")))
                        .icon(() -> CompactStorageBlocks.metalCompactChests.get(StorageTypes.PALE_OAK).createStack())
                        .displayItems((displayParameters, output) -> {
                            StorageTypes.stream().forEach(storageType -> {
                                if(storageType.isWooden()) {
                                    output.accept(CompactStorageBlocks.metalCompactChests.get(storageType));
                                }
                            });
                        })
        );
    }

}
