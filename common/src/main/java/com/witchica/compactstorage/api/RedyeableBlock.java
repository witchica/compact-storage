package com.witchica.compactstorage.api;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;

public interface RedyeableBlock {
    public BlockState getBlockStateOnRedye(BlockState state, DyeColor dyeColor);
}
