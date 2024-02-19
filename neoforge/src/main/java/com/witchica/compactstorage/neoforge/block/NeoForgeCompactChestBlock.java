package com.witchica.compactstorage.neoforge.block;

import com.mojang.serialization.MapCodec;
import com.witchica.compactstorage.common.block.CompactBarrelBlock;
import com.witchica.compactstorage.common.block.CompactChestBlock;
import com.witchica.compactstorage.neoforge.block.entity.NeoForgeCompactBarrelBlockEntity;
import com.witchica.compactstorage.neoforge.block.entity.NeoForgeCompactChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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

    @Override
    public void openMenu(Level level, Player player, BlockPos pos, BlockState state, InteractionHand hand) {
        MenuProvider screenHandlerFactory = state.getMenuProvider(level, pos);

        if (screenHandlerFactory != null) {
            player.openMenu(screenHandlerFactory, buf -> {
                buf.writeInt(0);
                buf.writeBlockPos(pos);
            });
        }
    }
}
