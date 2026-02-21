package com.witchica.compactstorage.util;

import com.witchica.compactstorage.block.entity.base.BaseCompactStorageBlockEntity;
import com.witchica.compactstorage.menu.GenericCompactStorageMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;

public class CompactStorageContainerOpenerCounter extends ContainerOpenersCounter {
    private final BaseCompactStorageBlockEntity compactStorageBlockEntity;

    public CompactStorageContainerOpenerCounter(BaseCompactStorageBlockEntity compactStorageBlockEntity) {
        super();
        this.compactStorageBlockEntity = compactStorageBlockEntity;
    }

    @Override
    protected void onOpen(Level level, BlockPos blockPos, BlockState blockState) {
        compactStorageBlockEntity.onContainerOpened(level, blockPos, blockState);
    }

    @Override
    protected void onClose(Level level, BlockPos blockPos, BlockState blockState) {
        compactStorageBlockEntity.onContainerClosed(level, blockPos, blockState);
    }

    @Override
    protected void openerCountChanged(Level level, BlockPos blockPos, BlockState blockState, int i, int i1) {
        compactStorageBlockEntity.signalOpenCount(level, blockPos, blockState, i, i1);
    }

    @Override
    public boolean isOwnContainer(Player player) {
        if (!(player.containerMenu instanceof GenericCompactStorageMenu)) {
            return false;
        } else {
            Container container = ((GenericCompactStorageMenu) player.containerMenu).container;
            return container == compactStorageBlockEntity || container instanceof CompoundContainer && ((CompoundContainer) container).contains(compactStorageBlockEntity);
        }
    }
}
