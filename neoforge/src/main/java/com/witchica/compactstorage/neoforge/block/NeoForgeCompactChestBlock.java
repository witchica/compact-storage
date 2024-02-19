package com.witchica.compactstorage.neoforge.block;

import com.mojang.serialization.MapCodec;
import com.witchica.compactstorage.common.block.CompactBarrelBlock;
import com.witchica.compactstorage.common.block.CompactChestBlock;
import com.witchica.compactstorage.neoforge.block.entity.NeoForgeCompactBarrelBlockEntity;
import com.witchica.compactstorage.neoforge.block.entity.NeoForgeCompactChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class NeoForgeCompactChestBlock extends CompactChestBlock {
    public static MapCodec<NeoForgeCompactChestBlock> CODEC = simpleCodec(NeoForgeCompactChestBlock::new);

    public NeoForgeCompactChestBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NeoForgeCompactChestBlockEntity(pos, state);
    }
}
