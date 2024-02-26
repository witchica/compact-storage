package com.witchica.compactstorage.forge.block;

import com.mojang.serialization.MapCodec;
import com.witchica.compactstorage.common.block.CompactBarrelBlock;
import com.witchica.compactstorage.common.block.CompactChestBlock;
import com.witchica.compactstorage.forge.block.entity.ForgeCompactBarrelBlockEntity;
import com.witchica.compactstorage.forge.block.entity.ForgeCompactChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class ForgeCompactBarrelBlock extends CompactBarrelBlock {
    public ForgeCompactBarrelBlock(Properties settings) {
        super(settings);
    }

    @Override
    public void openMenu(Level level, Player player, BlockPos pos, BlockState state, InteractionHand hand) {
        MenuProvider screenHandlerFactory = state.getMenuProvider(level, pos);

        if (screenHandlerFactory != null && player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(serverPlayer, screenHandlerFactory, buf -> {
                buf.writeInt(0);
                buf.writeBlockPos(pos);
            });
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ForgeCompactBarrelBlockEntity(pos, state);
    }
}
