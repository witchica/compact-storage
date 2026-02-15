package com.witchica.compactstorage.block.entity;

import com.witchica.compactstorage.menu.GenericCompactStorageMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import util.CompactStorageOpeningSource;

import java.util.Optional;

public class CompactChestBlockEntity extends BaseCompactStorageBlockEntity {
    private Component defaultName = Component.translatable("block.compact_storage.compact_chest");

    public CompactChestBlockEntity(BlockPos pos, BlockState blockState) {
        super(CompactStorageBlockEntities.COMPACT_CHEST_ENTITY.value(), pos, blockState);
        defaultName = blockState.getBlock().getName();
    }

    @Override
    protected Component getDefaultName() {
        return defaultName;
    }
}
