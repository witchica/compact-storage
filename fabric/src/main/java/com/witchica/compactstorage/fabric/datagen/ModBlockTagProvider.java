package com.witchica.compactstorage.fabric.datagen;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.block.ModBlocks;
import com.witchica.compactstorage.data.StorageType;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {
    public static final TagKey<Block> COMPACT_CHESTS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "compact_chests"));
    public static final TagKey<Block> WOODEN_COMPACT_CHESTS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "wooden_compact_chests"));
    public static final TagKey<Block> COLORFUL_COMPACT_CHESTS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "colorful_compact_chests"));

    public static final TagKey<Block> COMPACT_BARRELS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "compact_barrels"));
    public static final TagKey<Block> WOODEN_COMPACT_BARRELS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "wooden_compact_barrels"));
    public static final TagKey<Block> COLORFUL_COMPACT_BARRELS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "colorful_compact_barrels"));

    public static final TagKey<Block> ITEM_DRUMS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "item_drums"));
    public static final TagKey<Block> WOODEN_ITEM_DRUMS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "wooden_item_drums"));
    public static final TagKey<Block> COLORFUL_ITEM_DRUMS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "colorful_item_drums"));

    public static final TagKey<Block> STORAGE_BLOCKS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "storage_blocks"));

    public static final TagKey<Block> MINEABLE_AXE = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("minecraft", "mineable/axe"));
    public static final TagKey<Block> MINEABLE_PICKAXE = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("minecraft", "mineable/pickaxe"));

    public static final TagKey<Block> GUARDED_BY_PIGLINS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("minecraft", "guarded_by_piglins"));

    public ModBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }


    @Override
    protected void addTags(HolderLookup.Provider arg) {
        for(StorageType type : StorageType.values()) {
            tag(COMPACT_CHESTS).add(ModBlocks.compactChests.get(type).asResourceKey());
            tag(COMPACT_BARRELS).add(ModBlocks.compactBarrels.get(type).asResourceKey());
            tag(ITEM_DRUMS).add(ModBlocks.itemDrums.get(type).asResourceKey());

            tag(GUARDED_BY_PIGLINS).add(ModBlocks.compactChests.get(type).asResourceKey());
            tag(GUARDED_BY_PIGLINS).add(ModBlocks.compactBarrels.get(type).asResourceKey());
            tag(GUARDED_BY_PIGLINS).add(ModBlocks.itemDrums.get(type).asResourceKey());

            tag(STORAGE_BLOCKS).add(ModBlocks.compactChests.get(type).asResourceKey());
            tag(STORAGE_BLOCKS).add(ModBlocks.compactBarrels.get(type).asResourceKey());
            tag(STORAGE_BLOCKS).add(ModBlocks.itemDrums.get(type).asResourceKey());

            if(type.isWooden()) {
                tag(WOODEN_COMPACT_CHESTS).add(ModBlocks.compactChests.get(type).asResourceKey());
                tag(WOODEN_COMPACT_BARRELS).add(ModBlocks.compactBarrels.get(type).asResourceKey());
                tag(WOODEN_ITEM_DRUMS).add(ModBlocks.itemDrums.get(type).asResourceKey());

                tag(MINEABLE_AXE).add(ModBlocks.compactChests.get(type).asResourceKey());
                tag(MINEABLE_AXE).add(ModBlocks.compactBarrels.get(type).asResourceKey());
                tag(MINEABLE_AXE).add(ModBlocks.itemDrums.get(type).asResourceKey());
            } else {
                tag(COLORFUL_COMPACT_CHESTS).add(ModBlocks.compactChests.get(type).asResourceKey());
                tag(COLORFUL_COMPACT_BARRELS).add(ModBlocks.compactBarrels.get(type).asResourceKey());
                tag(COLORFUL_ITEM_DRUMS).add(ModBlocks.itemDrums.get(type).asResourceKey());

                tag(MINEABLE_PICKAXE).add(ModBlocks.compactChests.get(type).asResourceKey());
                tag(MINEABLE_PICKAXE).add(ModBlocks.compactBarrels.get(type).asResourceKey());
                tag(MINEABLE_PICKAXE).add(ModBlocks.itemDrums.get(type).asResourceKey());
            }
        }
    }
}
