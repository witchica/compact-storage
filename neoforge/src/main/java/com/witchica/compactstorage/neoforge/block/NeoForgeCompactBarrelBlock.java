package com.witchica.compactstorage.neoforge.block;

import com.mojang.serialization.MapCodec;
import com.witchica.compactstorage.common.block.CompactBarrelBlock;
import com.witchica.compactstorage.neoforge.block.entity.NeoForgeCompactBarrelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class NeoForgeCompactBarrelBlock extends CompactBarrelBlock {
    public static MapCodec<NeoForgeCompactBarrelBlock> CODEC = simpleCodec(NeoForgeCompactBarrelBlock::new);

    public NeoForgeCompactBarrelBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NeoForgeCompactBarrelBlockEntity(pos, state);
    }
}
