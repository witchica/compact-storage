package com.witchica.compactstorage.fabric.datagen;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.block.ModBlocks;
import com.witchica.compactstorage.data.StorageType;
import com.witchica.compactstorage.data.StorageUpgrade;
import com.witchica.compactstorage.item.ModItems;
import com.witchica.compactstorage.upgrades.ModUpgrades;
import net.blay09.mods.balm.world.item.DeferredItem;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

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
                tag(PACK_FRAME_ITEMS).add(ModItems.BACKPACK_ITEMS.get(type).asResourceKey());
            } else {
                tag(BACKPACK_ITEMS).add(ModItems.BACKPACK_ITEMS.get(type).asResourceKey());
            }

            tag(COMPACT_STORAGE_ITEMS).add(ModItems.BACKPACK_ITEMS.get(type).asResourceKey());

            tag(COMPACT_CHESTS).add(ModBlocks.compactChests.get(type).asBlockItemId().item());
            tag(COMPACT_BARRELS).add(ModBlocks.compactBarrels.get(type).asBlockItemId().item());
            tag(ITEM_DRUMS).add(ModBlocks.itemDrums.get(type).asBlockItemId().item());

            tag(COMPACT_STORAGE_BLOCKS).add(ModBlocks.compactChests.get(type).asBlockItemId().item());
            tag(COMPACT_STORAGE_BLOCKS).add(ModBlocks.compactBarrels.get(type).asBlockItemId().item());
            tag(COMPACT_STORAGE_BLOCKS).add(ModBlocks.itemDrums.get(type).asBlockItemId().item());

            if(type.isWooden()) {
                tag(WOODEN_COMPACT_CHESTS).add(ModBlocks.compactChests.get(type).asBlockItemId().item());
                tag(WOODEN_COMPACT_BARRELS).add(ModBlocks.compactBarrels.get(type).asBlockItemId().item());
                tag(WOODEN_ITEM_DRUMS).add(ModBlocks.itemDrums.get(type).asBlockItemId().item());
            } else {
                tag(COLORFUL_COMPACT_CHESTS).add(ModBlocks.compactChests.get(type).asBlockItemId().item());
                tag(COLORFUL_COMPACT_BARRELS).add(ModBlocks.compactBarrels.get(type).asBlockItemId().item());
                tag(COLORFUL_ITEM_DRUMS).add(ModBlocks.itemDrums.get(type).asBlockItemId().item());
            }

            tag(CURIOS_BACK_ITEMS).add(ModItems.BACKPACK_ITEMS.get(type).asResourceKey());
            tag(TRINKETS_BACK_ITEMS).add(ModItems.BACKPACK_ITEMS.get(type).asResourceKey());
        }

        tag(UPGRADES).add(ModUpgrades.values().stream().map(StorageUpgrade::getItem).map(DeferredItem::asResourceKey).toArray(ResourceKey[]::new));
    }

}
