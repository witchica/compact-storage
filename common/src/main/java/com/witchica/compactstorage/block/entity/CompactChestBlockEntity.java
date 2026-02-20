package com.witchica.compactstorage.block.entity;

import com.witchica.compactstorage.block.CompactChestBlock;
import com.witchica.compactstorage.menu.GenericCompactStorageMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

import java.util.List;

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
        if (id == 1) {
            this.chestLidController.shouldBeOpen(type > 0);
            return true;
        }

        return super.triggerEvent(id, type);
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
}
