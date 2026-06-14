package com.witchica.compactstorage.fabric.datagen;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.CompactStorageConfig;
import com.witchica.compactstorage.data.StorageUpgrade;
import com.witchica.compactstorage.mod.CompactStorageBlocks;
import com.witchica.compactstorage.mod.CompactStorageItemTags;
import com.witchica.compactstorage.mod.CompactStorageItems;
import com.witchica.compactstorage.mod.CompactStorageUpgrades;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import com.witchica.compactstorage.data.StorageType;
import net.minecraft.tags.TagKey;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;

import static com.witchica.compactstorage.CompactStorage.id;

public class ModLangProvider extends FabricLanguageProvider {
    protected ModLangProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    public String createTooltipDescriptorId(String tooltip) {
        return "tooltip." + CompactStorage.MOD_ID + "." + tooltip;
    }

    @Override
    public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder translationBuilder) {
        for(StorageType type : StorageType.values()) {
            translationBuilder.add(CompactStorageBlocks.compactChests.get(type).asBlock(), snakeCaseToName(type.getName()+"_chest"));
            translationBuilder.add(CompactStorageBlocks.compactBarrels.get(type).asBlock(), snakeCaseToName(type.getName()+"_barrel"));
            translationBuilder.add(CompactStorageBlocks.itemDrums.get(type).asBlock(), snakeCaseToName(type.getName()+"_item_drum"));
            translationBuilder.add(CompactStorageItems.BACKPACK_ITEMS.get(type).asItem(), snakeCaseToName(type.getName() + (type.isWooden() ? "_pack_frame" : "_backpack")));
        }

        translationBuilder.add(CompactStorageUpgrades.WIDTH_UPGRADE.getItem().asItem(), "Storage Upgrade (Width)");
        translationBuilder.add(CompactStorageUpgrades.HEIGHT_UPGRADE.getItem().asItem(), "Storage Upgrade (Height)");
        translationBuilder.add(CompactStorageUpgrades.RETAINING_UPGRADE.getItem().asItem(), "Storage Upgrade (Retaining)");
        translationBuilder.add(CompactStorageUpgrades.VOID_SLOT_UPGRADE.getItem().asItem(), "Storage Upgrade (Void Slot)");
        translationBuilder.add(CompactStorageUpgrades.ITEM_DRUM_UPGRADE.getItem().asItem(), "Storage Upgrade (Item Drum)");

        translationBuilder.add(id("general").toLanguageKey("itemGroup"), "CompactStorage");

        translationBuilder.add(createTooltipDescriptorId("upgrade_width"), "Can be applied to CompactStorage inventories to increase their width");
        translationBuilder.add(createTooltipDescriptorId("upgrade_width_limit"), "Width is limited to a maximum of %d");
        translationBuilder.add(createTooltipDescriptorId("upgrade_height"), "Can be applied to CompactStorage inventories to increase their height");
        translationBuilder.add(createTooltipDescriptorId("upgrade_height_limit"), "Height is limited to a maximum of %d");
        translationBuilder.add(createTooltipDescriptorId("upgrade_retaining"), "Allows CompactStorage inventories to keep their contents when broken");
        translationBuilder.add(createTooltipDescriptorId("upgrade_item_drum"), "Can be applied to Item Drums to increase their size");
        translationBuilder.add(createTooltipDescriptorId("upgrade_item_drum_limit"), "Size is limited to %d");
        translationBuilder.add(createTooltipDescriptorId("upgrade_void_slot"), "Can be applied to CompactStorage inventories to add a void slot");

        translationBuilder.add("message.compact_storage.upgrades.width.fail", "Inventory has already reached the maximum width.");
        translationBuilder.add("message.compact_storage.upgrades.height.fail", "Inventory has already reached the maximum height.");
        translationBuilder.add("message.compact_storage.upgrades.retaining.fail", "Inventory has already got the retaining upgrade.");
        translationBuilder.add("message.compact_storage.upgrades.void_slot.fail", "Inventory has already got the void slot upgrade.");

        translationBuilder.add("message.compact_storage.upgrades.not_allowed", "Inventory cannot accept this upgrade type.");
        translationBuilder.add("message.compact_storage.upgrades.item_drum.fail", "Item Drum has reached the maximum size.");

        translationBuilder.add("advancement.compact_storage.compacting_your_storage.title", "Compacting your storage!");
        translationBuilder.add("advancement.compact_storage.compacting_your_storage.description", "Get your first Compact Storage block");

        translationBuilder.add("advancement.compact_storage.got_barrel.title", "Barrels, now even cooler");
        translationBuilder.add("advancement.compact_storage.got_barrel.description", "On your way to becoming a master of CompactStorage!");

        translationBuilder.add("advancement.compact_storage.got_drum.title", "64 x 64 = 4096");
        translationBuilder.add("advancement.compact_storage.got_drum.description", "Finding ways to store lots of one thing");

        translationBuilder.add("advancement.compact_storage.got_backpack.title", "Storing things on the move? No problem.");
        translationBuilder.add("advancement.compact_storage.got_backpack.description", "Now you can take lots of things with you, how cool is that?");

        translationBuilder.add("advancement.compact_storage.width_upgrade.title", "Expanding width ways!");
        translationBuilder.add("advancement.compact_storage.width_upgrade.description", "Even more storage? Awesome.");

        translationBuilder.add("advancement.compact_storage.height_upgrade.title", "Expanding upwards!");
        translationBuilder.add("advancement.compact_storage.height_upgrade.description", "Even more storage? Vertically? Cool beans.");

        translationBuilder.add("advancement.compact_storage.item_drum_upgrade.title", "Drums, now bigger!");
        translationBuilder.add("advancement.compact_storage.item_drum_upgrade.description", "We aren't in 4096 item town anymore, are we?");

        translationBuilder.add("advancement.compact_storage.void_slot_upgrade.title", "Bye bye unwanted items!");
        translationBuilder.add("advancement.compact_storage.void_slot_upgrade.description", "Efficiently disposing of items since, well now... I guess?");

        translationBuilder.add("advancement.compact_storage.retainer_upgrade.title", "My chest grew legs!");
        translationBuilder.add("advancement.compact_storage.retainer_upgrade.description", "Now you can take your items with you, in block format!");

        translationBuilder.add("key.category.compact_storage.default", "CompactStorage");
        translationBuilder.add("key.compact_storage.backpack_open", "Open Backpack (Curios / Trinkets)");

        translationBuilder.add(createTooltipDescriptorId("upgrades.resizable"), "Size: %d x %d");
        translationBuilder.add(createTooltipDescriptorId("upgrades.retaining"), "Retaining");
        translationBuilder.add(createTooltipDescriptorId("upgrades.item_drum"), "Size: %d");
        translationBuilder.add(createTooltipDescriptorId("upgrades.void_slot"), "Void Slot");

        addForTag(CompactStorageItemTags.COLORFUL_COMPACT_BARRELS, translationBuilder);
        addForTag(CompactStorageItemTags.COLORFUL_COMPACT_CHESTS, translationBuilder);
        addForTag(CompactStorageItemTags.COLORFUL_ITEM_DRUMS, translationBuilder);
        addForTag(CompactStorageItemTags.COMPACT_BARRELS, translationBuilder);
        addForTag(CompactStorageItemTags.COMPACT_CHESTS, translationBuilder);
        addForTag(CompactStorageItemTags.ITEM_DRUMS, translationBuilder);
        addForTag(CompactStorageItemTags.WOODEN_COMPACT_BARRELS, translationBuilder);
        addForTag(CompactStorageItemTags.WOODEN_COMPACT_CHESTS, translationBuilder);
        addForTag(CompactStorageItemTags.WOODEN_ITEM_DRUMS, translationBuilder);
        addForTag(CompactStorageItemTags.STORAGE_BLOCKS, translationBuilder);
        addForTag(CompactStorageItemTags.BACKPACKS, translationBuilder);
        addForTag(CompactStorageItemTags.PACK_FRAMES, translationBuilder);
        addForTag(CompactStorageItemTags.STORAGE_ITEMS, translationBuilder);
        addForTag(CompactStorageItemTags.UPGRADES, translationBuilder);

        for(Field field : CompactStorageConfig.class.getFields()) {
            translationBuilder.add("compact_storage.configuration." + field.getName(), camelCaseToName(field.getName()));
        }

        for(StorageUpgrade storageUpgrade : CompactStorageUpgrades.values()) {
            translationBuilder.add("stat.compact_storage." + storageUpgrade.getStatisticName(), snakeCaseToName(storageUpgrade.getName()) + " Upgrades Applied");
        }
    }

    public String camelCaseToName(String s) {
        return (s.substring(0,1).toUpperCase() + s.substring(1)).replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
    }

    public void addForTag(TagKey tagKey, TranslationBuilder translationBuilder) {
        translationBuilder.add(tagKey.getTranslationKey(), snakeCaseToName(tagKey.location().getPath()));
    }

    private String snakeCaseToName(String s) {
        String[] bits = s.split("_");
        StringBuilder sb = new StringBuilder();

        for(String b : bits) {
            sb.append(b.substring(0,1).toUpperCase() + b.substring(1).toLowerCase());
            sb.append(" ");
        }

        return sb.toString().substring(0, sb.length()-1);
    }
}
