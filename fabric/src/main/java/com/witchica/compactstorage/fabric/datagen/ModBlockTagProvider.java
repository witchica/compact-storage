package com.witchica.compactstorage.fabric.datagen;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.data.StorageType;
import com.witchica.compactstorage.mod.CompactStorageBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
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

    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        for(StorageType type : StorageType.values()) {
            valueLookupBuilder(COMPACT_CHESTS).add(CompactStorageBlocks.compactChests.get(type).asBlock());
            valueLookupBuilder(COMPACT_BARRELS).add(CompactStorageBlocks.compactBarrels.get(type).asBlock());
            valueLookupBuilder(ITEM_DRUMS).add(CompactStorageBlocks.itemDrums.get(type).asBlock());

            valueLookupBuilder(GUARDED_BY_PIGLINS).add(CompactStorageBlocks.compactChests.get(type).asBlock());
            valueLookupBuilder(GUARDED_BY_PIGLINS).add(CompactStorageBlocks.compactBarrels.get(type).asBlock());
            valueLookupBuilder(GUARDED_BY_PIGLINS).add(CompactStorageBlocks.itemDrums.get(type).asBlock());

            valueLookupBuilder(STORAGE_BLOCKS).add(CompactStorageBlocks.compactChests.get(type).asBlock());
            valueLookupBuilder(STORAGE_BLOCKS).add(CompactStorageBlocks.compactBarrels.get(type).asBlock());
            valueLookupBuilder(STORAGE_BLOCKS).add(CompactStorageBlocks.itemDrums.get(type).asBlock());

            if(type.isWooden()) {
                valueLookupBuilder(WOODEN_COMPACT_CHESTS).add(CompactStorageBlocks.compactChests.get(type).asBlock());
                valueLookupBuilder(WOODEN_COMPACT_BARRELS).add(CompactStorageBlocks.compactBarrels.get(type).asBlock());
                valueLookupBuilder(WOODEN_ITEM_DRUMS).add(CompactStorageBlocks.itemDrums.get(type).asBlock());

                valueLookupBuilder(MINEABLE_AXE).add(CompactStorageBlocks.compactChests.get(type).asBlock());
                valueLookupBuilder(MINEABLE_AXE).add(CompactStorageBlocks.compactBarrels.get(type).asBlock());
                valueLookupBuilder(MINEABLE_AXE).add(CompactStorageBlocks.itemDrums.get(type).asBlock());
            } else {
                valueLookupBuilder(COLORFUL_COMPACT_CHESTS).add(CompactStorageBlocks.compactChests.get(type).asBlock());
                valueLookupBuilder(COLORFUL_COMPACT_BARRELS).add(CompactStorageBlocks.compactBarrels.get(type).asBlock());
                valueLookupBuilder(COLORFUL_ITEM_DRUMS).add(CompactStorageBlocks.itemDrums.get(type).asBlock());

                valueLookupBuilder(MINEABLE_PICKAXE).add(CompactStorageBlocks.compactChests.get(type).asBlock());
                valueLookupBuilder(MINEABLE_PICKAXE).add(CompactStorageBlocks.compactBarrels.get(type).asBlock());
                valueLookupBuilder(MINEABLE_PICKAXE).add(CompactStorageBlocks.itemDrums.get(type).asBlock());
            }
        }
    }
}
