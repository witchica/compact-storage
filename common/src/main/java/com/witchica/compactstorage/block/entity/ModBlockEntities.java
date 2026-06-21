package com.witchica.compactstorage.block.entity;

import com.witchica.compactstorage.block.entity.base.BaseItemDrumBlockEntity;
import com.witchica.compactstorage.block.ModBlocks;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityTypeRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static Holder<BlockEntityType<CompactChestBlockEntity>> COMPACT_CHEST_ENTITY;
    public static Holder<BlockEntityType<CompactBarrelBlockEntity>> COMPACT_BARREL_ENTITY;
    public static Holder<BlockEntityType<BaseItemDrumBlockEntity>> DRUM_BLOCK_ENTITY;

    public static void initialize(BalmBlockEntityTypeRegistrar registrar) {
        COMPACT_CHEST_ENTITY = registrar.register("compact_chest", CompactChestBlockEntity::new, ModBlocks.compactChests.values()).asHolder();
        COMPACT_BARREL_ENTITY = registrar.register("compact_barrel", CompactBarrelBlockEntity::new, ModBlocks.compactBarrels.values()).asHolder();
        DRUM_BLOCK_ENTITY = registrar.register("drum", BaseItemDrumBlockEntity::new, ModBlocks.itemDrums.values()).asHolder();
    }
}
