package com.witchica.compactstorage.block.entity.base;

import com.witchica.compactstorage.mod.CompactStorageBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BaseItemDrumBlockEntity extends BlockEntity {
    public BaseItemDrumBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CompactStorageBlockEntities.DRUM_BLOCK_ENTITY.value(), blockPos, blockState);
    }

    public void tick() {

    }

    public static void ticker(Level level, BlockPos blockPos, BlockState blockState, BaseItemDrumBlockEntity baseItemDrumBlockEntity) {
        baseItemDrumBlockEntity.tick();
    }
}
