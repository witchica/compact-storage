package com.witchica.compactstorage.tag;

import com.witchica.compactstorage.CompactStorage;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {
    public static final TagKey<Block> COLORFUL_COMPACT_BARRELS = TagKey.create(Registries.BLOCK, CompactStorage.id("colorful_compact_barrels"));
    public static final TagKey<Block> COLORFUL_COMPACT_CHESTS = TagKey.create(Registries.BLOCK, CompactStorage.id("colorful_compact_chests"));
    public static final TagKey<Block> COLORFUL_ITEM_DRUMS = TagKey.create(Registries.BLOCK, CompactStorage.id("colorful_item_drums"));
    public static final TagKey<Block> COMPACT_BARRELS = TagKey.create(Registries.BLOCK, CompactStorage.id("compact_barrels"));
    public static final TagKey<Block> COMPACT_CHESTS = TagKey.create(Registries.BLOCK, CompactStorage.id("compact_chests"));
    public static final TagKey<Block> ITEM_DRUMS = TagKey.create(Registries.BLOCK, CompactStorage.id("item_drums"));
    public static final TagKey<Block> WOODEN_COMPACT_BARRELS = TagKey.create(Registries.BLOCK, CompactStorage.id("wooden_compact_barrels"));
    public static final TagKey<Block> WOODEN_COMPACT_CHESTS = TagKey.create(Registries.BLOCK, CompactStorage.id("wooden_compact_chests"));
    public static final TagKey<Block> WOODEN_ITEM_DRUMS = TagKey.create(Registries.BLOCK, CompactStorage.id("wooden_item_drums"));
    public static final TagKey<Block> STORAGE_BLOCKS = TagKey.create(Registries.BLOCK, CompactStorage.id("storage_blocks"));
}
