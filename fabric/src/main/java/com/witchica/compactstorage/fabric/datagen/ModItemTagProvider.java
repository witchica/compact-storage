package com.witchica.compactstorage.fabric.datagen;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.data.StorageType;
import com.witchica.compactstorage.mod.CompactStorageBlocks;
import com.witchica.compactstorage.mod.CompactStorageItems;
import com.witchica.compactstorage.mod.CompactStorageUpgrades;
import net.blay09.mods.balm.world.item.DeferredItem;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.impl.resource.pack.FabricPack;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagsProvider.ItemTagsProvider {

    public static final TagKey<Item> BACKPACK_ITEMS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "backpacks"));
    public static final TagKey<Item> PACK_FRAME_ITEMS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "pack_frames"));

    public static final TagKey<Item> COMPACT_CHESTS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "compact_chests"));
    public static final TagKey<Item> WOODEN_COMPACT_CHESTS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "wooden_compact_chests"));
    public static final TagKey<Item> COLORFUL_COMPACT_CHESTS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "colorful_compact_chests"));

    public static final TagKey<Item> COMPACT_BARRELS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "compact_barrels"));
    public static final TagKey<Item> WOODEN_COMPACT_BARRELS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "wooden_compact_barrels"));
    public static final TagKey<Item> COLORFUL_COMPACT_BARRELS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "colorful_compact_barrels"));

    public static final TagKey<Item> ITEM_DRUMS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "item_drums"));
    public static final TagKey<Item> WOODEN_ITEM_DRUMS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "wooden_item_drums"));
    public static final TagKey<Item> COLORFUL_ITEM_DRUMS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "colorful_item_drums"));

    public static final TagKey<Item> CURIOS_BACK_ITEMS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("curios", "back"));
    public static final TagKey<Item> TRINKETS_BACK_ITEMS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("trinkets", "chest/back"));

    public static final TagKey<Item> COMPACT_STORAGE_BLOCKS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "storage_blocks"));
    public static final TagKey<Item> COMPACT_STORAGE_ITEMS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "storage_items"));
    public static final TagKey<Item> UPGRADES = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "upgrades"));

    public ModItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        for(StorageType type : StorageType.values()) {
            if(type.isWooden()) {
                valueLookupBuilder(PACK_FRAME_ITEMS).add(CompactStorageItems.BACKPACK_ITEMS.get(type).asItem());
            } else {
                valueLookupBuilder(BACKPACK_ITEMS).add(CompactStorageItems.BACKPACK_ITEMS.get(type).asItem());
            }

            valueLookupBuilder(COMPACT_STORAGE_ITEMS).add(CompactStorageItems.BACKPACK_ITEMS.get(type).asItem());

            valueLookupBuilder(COMPACT_CHESTS).add(CompactStorageBlocks.compactChests.get(type).asItem());
            valueLookupBuilder(COMPACT_BARRELS).add(CompactStorageBlocks.compactBarrels.get(type).asItem());
            valueLookupBuilder(ITEM_DRUMS).add(CompactStorageBlocks.itemDrums.get(type).asItem());

            valueLookupBuilder(COMPACT_STORAGE_BLOCKS).add(CompactStorageBlocks.compactChests.get(type).asItem());
            valueLookupBuilder(COMPACT_STORAGE_BLOCKS).add(CompactStorageBlocks.compactBarrels.get(type).asItem());
            valueLookupBuilder(COMPACT_STORAGE_BLOCKS).add(CompactStorageBlocks.itemDrums.get(type).asItem());

            if(type.isWooden()) {
                valueLookupBuilder(WOODEN_COMPACT_CHESTS).add(CompactStorageBlocks.compactChests.get(type).asItem());
                valueLookupBuilder(WOODEN_COMPACT_BARRELS).add(CompactStorageBlocks.compactBarrels.get(type).asItem());
                valueLookupBuilder(WOODEN_ITEM_DRUMS).add(CompactStorageBlocks.itemDrums.get(type).asItem());
            } else {
                valueLookupBuilder(COLORFUL_COMPACT_CHESTS).add(CompactStorageBlocks.compactChests.get(type).asItem());
                valueLookupBuilder(COLORFUL_COMPACT_BARRELS).add(CompactStorageBlocks.compactBarrels.get(type).asItem());
                valueLookupBuilder(COLORFUL_ITEM_DRUMS).add(CompactStorageBlocks.itemDrums.get(type).asItem());
            }

            valueLookupBuilder(CURIOS_BACK_ITEMS).add(CompactStorageItems.BACKPACK_ITEMS.get(type).asItem());
            valueLookupBuilder(TRINKETS_BACK_ITEMS).add(CompactStorageItems.BACKPACK_ITEMS.get(type).asItem());
        }

        valueLookupBuilder(UPGRADES).add(CompactStorageItems.UPGRADES.values().stream().map(DeferredItem::asItem).toArray(Item[]::new));
    }

}
