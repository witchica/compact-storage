package com.witchica.compactstorage.fabric.block;

import com.mojang.serialization.MapCodec;
import com.witchica.compactstorage.common.block.CompactBarrelBlock;
import com.witchica.compactstorage.common.block.CompactChestBlock;
import com.witchica.compactstorage.fabric.block.entity.FabricCompactBarrelBlockEntity;
import com.witchica.compactstorage.fabric.block.entity.FabricCompactChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FabricCompactBarrelBlock extends CompactBarrelBlock {

    public static MapCodec<FabricCompactBarrelBlock> CODEC = simpleCodec(FabricCompactBarrelBlock::new);
    public FabricCompactBarrelBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FabricCompactBarrelBlockEntity(pos, state);
    }
}
