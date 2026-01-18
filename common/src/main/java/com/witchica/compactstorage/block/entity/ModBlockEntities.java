package com.witchica.compactstorage.block.entity;

import com.witchica.compactstorage.block.ModBlocks;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityTypeRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static Holder<BlockEntityType<BaseCompactStorageBlockEntity>> COMPACT_CHEST_ENTITY;

    public static void initialize(BalmBlockEntityTypeRegistrar registrar) {
        COMPACT_CHEST_ENTITY = registrar.register("compact_chest", BaseCompactStorageBlockEntity::new, ModBlocks.metalCompactChests.values()).asHolder();
    }
}
