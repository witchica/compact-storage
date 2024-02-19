package com.witchica.compactstorage.fabric.block;

import com.mojang.serialization.MapCodec;
import com.witchica.compactstorage.common.block.CompactChestBlock;
import com.witchica.compactstorage.fabric.block.entity.FabricCompactChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FabricCompactChestBlock extends CompactChestBlock {

    public static MapCodec<FabricCompactChestBlock> CODEC = simpleCodec(FabricCompactChestBlock::new);
    public FabricCompactChestBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FabricCompactChestBlockEntity(pos, state);
    }

    @Override
    public void openMenu(Level level, Player player, BlockPos pos, BlockState state, InteractionHand hand) {
        MenuProvider screenHandlerFactory = state.getMenuProvider(level, pos);

        if (screenHandlerFactory != null) {
            player.openMenu(screenHandlerFactory);
        }
    }
}
