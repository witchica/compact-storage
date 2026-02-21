package com.witchica.compactstorage.mod;

import com.witchica.compactstorage.block.entity.CompactBarrelBlockEntity;
import com.witchica.compactstorage.block.entity.CompactChestBlockEntity;
import com.witchica.compactstorage.block.entity.base.BaseItemDrumBlockEntity;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityTypeRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CompactStorageBlockEntities {
    public static Holder<BlockEntityType<CompactChestBlockEntity>> COMPACT_CHEST_ENTITY;
    public static Holder<BlockEntityType<CompactBarrelBlockEntity>> COMPACT_BARREL_ENTITY;
    public static Holder<BlockEntityType<BaseItemDrumBlockEntity>> DRUM_BLOCK_ENTITY;

    public static void initialize(BalmBlockEntityTypeRegistrar registrar) {
        COMPACT_CHEST_ENTITY = registrar.register("compact_chest", CompactChestBlockEntity::new, CompactStorageBlocks.compactChests.values()).asHolder();
        COMPACT_BARREL_ENTITY = registrar.register("compact_barrel", CompactBarrelBlockEntity::new, CompactStorageBlocks.compactBarrels.values()).asHolder();
        DRUM_BLOCK_ENTITY = registrar.register("item_drum", BaseItemDrumBlockEntity::new, CompactStorageBlocks.itemDrums.values()).asHolder();
    }
}
