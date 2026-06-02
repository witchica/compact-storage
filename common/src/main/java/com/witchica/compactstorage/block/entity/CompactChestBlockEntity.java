package com.witchica.compactstorage.block.entity;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.block.CompactChestBlock;
import com.witchica.compactstorage.block.entity.base.BaseCompactStorageBlockEntity;
import com.witchica.compactstorage.mod.CompactStorageBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CompactChestBlockEntity extends BaseCompactStorageBlockEntity implements LidBlockEntity {
    private Component defaultName = Component.translatable("block.compact_storage.compact_chest");
    private ChestLidController chestLidController;

    public CompactChestBlockEntity(BlockPos pos, BlockState blockState) {
        super(CompactStorageBlockEntities.COMPACT_CHEST_ENTITY.value(), pos, blockState);
        defaultName = blockState.getBlock().getName();
        this.chestLidController = new ChestLidController();
    }

    @Override
    public void onContainerOpened(Level level, BlockPos blockPos, BlockState blockState) {
        super.onContainerOpened(level, blockPos, blockState);
        if(blockState.getBlock() instanceof CompactChestBlock chestBlock) {
            playSound(level, blockPos, chestBlock.getStorageType().chestOpenSound);
        }
    }

    @Override
    public void onContainerClosed(Level level, BlockPos blockPos, BlockState blockState) {
        super.onContainerClosed(level, blockPos, blockState);
        if(blockState.getBlock() instanceof CompactChestBlock chestBlock) {
            playSound(level, blockPos, chestBlock.getStorageType().chestCloseSound);
        }
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if (id == 2) {
            this.chestLidController.shouldBeOpen(type > 0);
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
    public float getOpenNess(float v) {
        return chestLidController.getOpenness(v);
    }

    @Override
    public void tick() {
        chestLidController.tickLid();
    }

    @Override
    public int getMaximumWidth() {
        return CompactStorage.config().constrainWidth(CompactStorage.config().compactChestMaxWidth);
    }

    @Override
    public int getMaximumHeight() {
        return CompactStorage.config().constrainHeight(CompactStorage.config().compactChestMaxHeight);
    }

    @Override
    public int getDefaultWidth() {
        return CompactStorage.config().constrainWidth(CompactStorage.config().compactBarrelDefaultWidth);
    }

    @Override
    public int getDefaultHeight() {
        return CompactStorage.config().constrainHeight(CompactStorage.config().compactBarrelDefaultHeight);
    }
}
