package com.witchica.compactstorage.item;

import com.witchica.compactstorage.block.ModBlocks;
import com.witchica.compactstorage.data.StorageType;
import com.witchica.compactstorage.data.StorageUpgrade;
import com.witchica.compactstorage.upgrades.ModUpgrades;
import net.blay09.mods.balm.world.item.BalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.world.item.BalmItemRegistrar;
import net.blay09.mods.balm.world.item.DeferredItem;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;

import java.util.HashMap;
import java.util.Map;

import static com.witchica.compactstorage.CompactStorage.id;

public class ModItems {
    public static Holder<CreativeModeTab> TAB;

    public static Map<StorageType, DeferredItem> BACKPACK_ITEMS = new HashMap<>();

    public static void initializeItems(BalmItemRegistrar items) {
        for(StorageUpgrade upgrade : ModUpgrades.UPGRADES) {
            upgrade.setItem(items.register(upgrade.getItemName(), (properties -> new StorageUpgradeItem(properties, upgrade))).asDeferredItem());
        }

        for(StorageType type : StorageType.values()) {
            BACKPACK_ITEMS.put(type, items.register(type.backpackNameFactory(), (properties -> {
                final StorageType storageType = type;
                return new BackpackItem(properties.stacksTo(1).equippable(EquipmentSlot.CHEST), storageType);
            })).asDeferredItem());
        }

        items.addAlias("upgrade_row", "width_upgrade");
        items.addAlias("upgrade_column", "height_upgrade");
        items.addAlias("upgrade_retainer", "retainer_upgrade");
    }

    public static void initializeCreativeTabs(BalmCreativeModeTabRegistrar creativeModeTabs) {
        TAB = creativeModeTabs.register("general", builder ->
                builder.title(Component.translatable(id("general").toLanguageKey("itemGroup")))
                        .icon(() -> ModBlocks.compactChests.get(StorageType.RED).createStack())
                        .displayItems((displayParameters, output) -> {
                            StorageType.stream().forEach(storageType -> {
                                output.accept(ModBlocks.compactChests.get(storageType));
                            });

                            StorageType.stream().forEach(storageType -> {
                                output.accept(ModBlocks.compactBarrels.get(storageType));
                            });

                            StorageType.stream().forEach(storageType -> {
                                output.accept(ModBlocks.itemDrums.get(storageType));
                            });

                            StorageType.stream().forEach(storageType -> {
                                output.accept(ModItems.BACKPACK_ITEMS.get(storageType));
                            });

                            ModUpgrades.values().forEach(value -> {
                                output.accept(value.getItem().asItem());
                            });
                        })
        ).asHolder();
    }

}
