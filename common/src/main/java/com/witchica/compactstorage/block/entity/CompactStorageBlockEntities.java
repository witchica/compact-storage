package com.witchica.compactstorage.block.entity;

import com.witchica.compactstorage.block.CompactStorageBlocks;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityTypeRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CompactStorageBlockEntities {
    public static Holder<BlockEntityType<CompactChestBlockEntity>> COMPACT_CHEST_ENTITY;

    public static void initialize(BalmBlockEntityTypeRegistrar registrar) {
        COMPACT_CHEST_ENTITY = registrar.register("compact_chest", CompactChestBlockEntity::new, CompactStorageBlocks.metalCompactChests.values()).asHolder();
    }
}
