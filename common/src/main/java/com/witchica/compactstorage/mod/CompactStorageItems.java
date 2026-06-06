package com.witchica.compactstorage.mod;

import com.witchica.compactstorage.api.StorageUpgrade;
import com.witchica.compactstorage.item.BackpackItem;
import com.witchica.compactstorage.item.StorageUpgradeItem;
import net.blay09.mods.balm.world.item.BalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.world.item.BalmItemRegistrar;
import net.blay09.mods.balm.world.item.DeferredItem;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import com.witchica.compactstorage.data.StorageType;
import net.minecraft.world.item.CreativeModeTab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.witchica.compactstorage.CompactStorage.id;

public class CompactStorageItems {
    public static Holder<CreativeModeTab> IRON_TAB;
    public static Holder<CreativeModeTab> WOOD_TAB;
    public static Map<StorageUpgrade, DeferredItem> UPGRADES = new HashMap<>();

    public static Map<StorageType, DeferredItem> BACKPACK_ITEMS = new HashMap<>();

    public static void initializeItems(BalmItemRegistrar items) {

        for(StorageUpgrade upgrade : CompactStorageUpgrades.UPGRADES) {
            UPGRADES.put(upgrade, items.register(upgrade.getItemName(), (properties -> new StorageUpgradeItem(properties, upgrade))).asDeferredItem());
        }

        for(StorageType type : StorageType.values()) {
            BACKPACK_ITEMS.put(type, items.register(type.backpackNameFactory(), (properties -> {
                final StorageType storageType = type;
                return new BackpackItem(properties.stacksTo(1), storageType);
            })).asDeferredItem());
        }

        // Backwards Compatibility
        for(StorageType storageType : StorageType.values()) {
            if(!storageType.isWooden()) {
                items.addAlias("backpack_" + storageType.getName(), storageType.getName() + "_backpack");
            }
        }

        items.addAlias("upgrade_row", "width_upgrade");
        items.addAlias("upgrade_column", "height_upgrade");
        items.addAlias("upgrade_retainer", "retainer_upgrade");
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

                            UPGRADES.values().stream().map(DeferredItem::asItem).forEach(output::accept);
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

                            UPGRADES.values().stream().map(DeferredItem::asItem).forEach(output::accept);
                        })
        ).asHolder();
    }

}
