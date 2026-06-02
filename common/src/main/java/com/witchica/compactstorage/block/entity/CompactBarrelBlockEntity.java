package com.witchica.compactstorage.block.entity;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.block.CompactBarrelBlock;
import com.witchica.compactstorage.block.entity.base.BaseCompactStorageBlockEntity;
import com.witchica.compactstorage.mod.CompactStorageBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CompactBarrelBlockEntity extends BaseCompactStorageBlockEntity {
    private Component defaultName = Component.translatable("block.compact_storage.compact_barrel");

    public CompactBarrelBlockEntity(BlockPos pos, BlockState blockState) {
        super(CompactStorageBlockEntities.COMPACT_BARREL_ENTITY.value(), pos, blockState);
        defaultName = blockState.getBlock().getName();
    }

    @Override
    public void tick() {

    }

    @Override
    public void onContainerOpened(Level level, BlockPos blockPos, BlockState blockState) {
        super.onContainerOpened(level, blockPos, blockState);
        if(blockState.getBlock() instanceof CompactBarrelBlock chestBlock) {
            playSound(level, blockPos, chestBlock.getStorageType().barrelOpenSound);
            this.getLevel().setBlock(getBlockPos(), getBlockState().setValue(CompactBarrelBlock.OPEN, true), 3);
        }
    }

    @Override
    public void onContainerClosed(Level level, BlockPos blockPos, BlockState blockState) {
        super.onContainerClosed(level, blockPos, blockState);
        if(blockState.getBlock() instanceof CompactBarrelBlock chestBlock) {
            playSound(level, blockPos, chestBlock.getStorageType().barrelCloseSound);
            this.getLevel().setBlock(getBlockPos(), getBlockState().setValue(CompactBarrelBlock.OPEN, false), 3);
        }
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if (id == 2) {
            boolean open = type > 0;
            return true;
        } else {
            return super.triggerEvent(id, type);
        }
    }

    @Override
    protected Component getDefaultName() {
        return defaultName;
    }

    @Override
    public int getMaximumWidth() {
        return Math.min(CompactStorage.config().compactBarrelMaxWidth, 21);
    }

    @Override
    public int getMaximumHeight() {
        return Math.min(CompactStorage.config().compactBarrelMaxHeight, 12);
    }

    @Override
    public int getDefaultWidth() {
        return Math.min(CompactStorage.config().compactBarrelDefaultWidth, 21);
    }

    @Override
    public int getDefaultHeight() {
        return Math.min(CompactStorage.config().compactBarrelDefaultHeight, 12);
    }
}
