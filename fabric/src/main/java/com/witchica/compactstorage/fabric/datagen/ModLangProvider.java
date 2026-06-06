package com.witchica.compactstorage.fabric.datagen;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.mod.CompactStorageBlocks;
import com.witchica.compactstorage.mod.CompactStorageItemTags;
import com.witchica.compactstorage.mod.CompactStorageItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import com.witchica.compactstorage.data.StorageType;
import net.minecraft.tags.TagKey;

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

        translationBuilder.add(CompactStorageItems.UPGRADE_WIDTH.asItem(), "Storage Upgrade (Width)");
        translationBuilder.add(CompactStorageItems.UPGRADE_HEIGHT.asItem(), "Storage Upgrade (Height)");
        translationBuilder.add(CompactStorageItems.UPGRADE_RETAINING.asItem(), "Storage Upgrade (Retaining)");
        translationBuilder.add(CompactStorageItems.UPGRADE_VOID_SLOT.asItem(), "Storage Upgrade (Void Slot)");

        translationBuilder.add(id("general").toLanguageKey("itemGroup"), "CompactStorage (Iron)");
        translationBuilder.add(id("wood").toLanguageKey("itemGroup"), "CompactStorage (Wood)");

        translationBuilder.add(createTooltipDescriptorId("upgrade_width"), "Can be applied to CompactStorage inventories to increase their width");
        translationBuilder.add(createTooltipDescriptorId("upgrade_width_limit"), "Width is limited to a maximum of %d");
        translationBuilder.add(createTooltipDescriptorId("upgrade_height"), "Can be applied to CompactStorage inventories to increase their height");
        translationBuilder.add(createTooltipDescriptorId("upgrade_height_limit"), "Height is limited to a maximum of %d");
        translationBuilder.add(createTooltipDescriptorId("upgrade_retaining"), "Allows CompactStorage inventories to keep their contents when broken");

        translationBuilder.add("message.compact_storage.upgrades.width.fail", "Inventory has already reached the maximum width.");
        translationBuilder.add("message.compact_storage.upgrades.height.fail", "Inventory has already reached the maximum height.");
        translationBuilder.add("message.compact_storage.upgrades.retaining.fail", "Inventory has already got the retaining upgrade.");
        translationBuilder.add("message.compact_storage.upgrades.void_slot.fail", "Inventory has already got the void slot upgrade.");

        translationBuilder.add("message.compact_storage.upgrades.not_allowed", "Inventory cannot accept this upgrade type.");

        translationBuilder.add("advancement.compact_storage.compacting_your_storage.title", "Compacting your storage!");
        translationBuilder.add("advancement.compact_storage.compacting_your_storage.description", "Get your first Compact Chest");

        translationBuilder.add("advancement.compact_storage.got_barrel.title", "Barrels, now even cooler");
        translationBuilder.add("advancement.compact_storage.got_barrel.description", "On your way to becoming a master of CompactStorage!");

        translationBuilder.add("advancement.compact_storage.got_drum.title", "64 x 64 = 4096");
        translationBuilder.add("advancement.compact_storage.got_drum.description", "Finding ways to store lots of one thing");

        translationBuilder.add("advancement.compact_storage.got_backpack.title", "Storing things on the move? No problem.");
        translationBuilder.add("advancement.compact_storage.got_backpack.description", "Now you can take lots of things with you, how cool is that?");

        translationBuilder.add("key.category.compact_storage.default", "CompactStorage");
        translationBuilder.add("key.compact_storage.backpack_open", "Open Backpack (Curios / Trinkets)");

        translationBuilder.add(createTooltipDescriptorId("upgrades.resizable"), "Size: %d x %d");
        translationBuilder.add(createTooltipDescriptorId("upgrades.retaining"), "Retaining");

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
