package com.witchica.compactstorage.mod;

import com.witchica.compactstorage.item.BackpackItem;
import com.witchica.compactstorage.item.StorageUpgradeItem;
import net.blay09.mods.balm.world.item.BalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.world.item.BalmItemRegistrar;
import net.blay09.mods.balm.world.item.DeferredItem;
import net.blay09.mods.balm.world.level.block.DiscriminatedBlocks;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import com.witchica.compactstorage.data.StorageType;
import com.witchica.compactstorage.data.UpgradeType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

import static com.witchica.compactstorage.CompactStorage.id;

public class CompactStorageItems {
    public static Holder<CreativeModeTab> IRON_TAB;
    public static Holder<CreativeModeTab> WOOD_TAB;
    public static DeferredItem UPGRADE_WIDTH;
    public static DeferredItem UPGRADE_HEIGHT;
    public static DeferredItem UPGRADE_RETAINING;

    public static Map<StorageType, DeferredItem> BACKPACK_ITEMS = new HashMap<>();

    public static void initializeItems(BalmItemRegistrar items) {
        UPGRADE_WIDTH = items.register(UpgradeType.WIDTH_UPGRADE.name(), (properties) -> new StorageUpgradeItem(properties, UpgradeType.WIDTH_UPGRADE)).asDeferredItem();
        UPGRADE_HEIGHT = items.register(UpgradeType.HEIGHT_UPGRADE.name(), (properties) -> new StorageUpgradeItem(properties, UpgradeType.HEIGHT_UPGRADE)).asDeferredItem();
        UPGRADE_RETAINING = items.register(UpgradeType.RETAINING_UPGRADE.name(), (properties) -> new StorageUpgradeItem(properties, UpgradeType.RETAINING_UPGRADE)).asDeferredItem();

        for(StorageType type : StorageType.values()) {
            BACKPACK_ITEMS.put(type, items.register(type.backpackNameFactory(), (properties -> new BackpackItem(properties, type))).asDeferredItem());
        }
    }

    public static void initializeCreativeTabs(BalmCreativeModeTabRegistrar creativeModeTabs) {
        IRON_TAB = creativeModeTabs.register("metal", builder ->
                builder.title(Component.translatable(id("general").toLanguageKey("itemGroup")))
                        .icon(() -> CompactStorageBlocks.compactChests.get(StorageType.RED).createStack())
                        .displayItems((displayParameters, output) -> {
                            StorageType.stream().forEach(storageType -> {
                                if(!storageType.isWooden()) {
                                    output.accept(CompactStorageBlocks.compactChests.get(storageType));
                                    output.accept(CompactStorageBlocks.compactBarrels.get(storageType));
                                    output.accept(CompactStorageBlocks.itemDrums.get(storageType));
                                    output.accept(CompactStorageItems.BACKPACK_ITEMS.get(storageType));
                                }
                            });

                            output.accept(UPGRADE_WIDTH);
                            output.accept(UPGRADE_HEIGHT);
                            output.accept(UPGRADE_RETAINING);
                        })
        ).asHolder();

        WOOD_TAB = creativeModeTabs.register("wood", builder ->
                builder.title(Component.translatable(id("wood").toLanguageKey("itemGroup")))
                        .icon(() -> CompactStorageBlocks.compactChests.get(StorageType.PALE_OAK).createStack())
                        .displayItems((displayParameters, output) -> {
                            StorageType.stream().forEach(storageType -> {
                                if(storageType.isWooden()) {
                                    output.accept(CompactStorageBlocks.compactChests.get(storageType));
                                    output.accept(CompactStorageBlocks.compactBarrels.get(storageType));
                                    output.accept(CompactStorageBlocks.itemDrums.get(storageType));
                                    output.accept(CompactStorageItems.BACKPACK_ITEMS.get(storageType));
                                }
                            });
                        })
        ).asHolder();
    }

}
