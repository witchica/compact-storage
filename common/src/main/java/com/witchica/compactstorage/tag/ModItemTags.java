package com.witchica.compactstorage.tag;

import com.witchica.compactstorage.CompactStorage;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModItemTags {
    public static final TagKey<Item> COLORFUL_COMPACT_BARRELS = TagKey.create(Registries.ITEM, CompactStorage.id("colorful_compact_barrels"));
    public static final TagKey<Item> COLORFUL_COMPACT_CHESTS = TagKey.create(Registries.ITEM, CompactStorage.id("colorful_compact_chests"));
    public static final TagKey<Item> COLORFUL_ITEM_DRUMS = TagKey.create(Registries.ITEM, CompactStorage.id("colorful_item_drums"));
    public static final TagKey<Item> COMPACT_BARRELS = TagKey.create(Registries.ITEM, CompactStorage.id("compact_barrels"));
    public static final TagKey<Item> COMPACT_CHESTS = TagKey.create(Registries.ITEM, CompactStorage.id("compact_chests"));
    public static final TagKey<Item> ITEM_DRUMS = TagKey.create(Registries.ITEM, CompactStorage.id("item_drums"));
    public static final TagKey<Item> WOODEN_COMPACT_BARRELS = TagKey.create(Registries.ITEM, CompactStorage.id("wooden_compact_barrels"));
    public static final TagKey<Item> WOODEN_COMPACT_CHESTS = TagKey.create(Registries.ITEM, CompactStorage.id("wooden_compact_chests"));
    public static final TagKey<Item> WOODEN_ITEM_DRUMS = TagKey.create(Registries.ITEM, CompactStorage.id("wooden_item_drums"));
    public static final TagKey<Item> STORAGE_BLOCKS = TagKey.create(Registries.ITEM, CompactStorage.id("storage_blocks"));
    public static final TagKey<Item> BACKPACKS = TagKey.create(Registries.ITEM, CompactStorage.id("backpacks"));
    public static final TagKey<Item> PACK_FRAMES = TagKey.create(Registries.ITEM, CompactStorage.id("pack_frames"));
    public static final TagKey<Item> STORAGE_ITEMS = TagKey.create(Registries.ITEM, CompactStorage.id("storage_items"));
    public static final TagKey<Item> UPGRADES = TagKey.create(Registries.ITEM, CompactStorage.id("upgrades"));
}
