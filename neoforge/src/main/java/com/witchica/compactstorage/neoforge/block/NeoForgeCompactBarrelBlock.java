package com.witchica.compactstorage.neoforge.block;

import com.mojang.serialization.MapCodec;
import com.witchica.compactstorage.common.block.CompactBarrelBlock;
import com.witchica.compactstorage.neoforge.block.entity.NeoForgeCompactBarrelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class NeoForgeCompactBarrelBlock extends CompactBarrelBlock {

    public NeoForgeCompactBarrelBlock(Properties settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity m_142194_(BlockPos pos, BlockState state) {
        return new NeoForgeCompactBarrelBlockEntity(pos, state);
    }

    @Override
    public void openMenu(Level level, Player player, BlockPos pos, BlockState state, InteractionHand hand) {
        MenuProvider screenHandlerFactory = state.m_60750_(level, pos);

        if (screenHandlerFactory != null && player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(serverPlayer, screenHandlerFactory, buf -> {
                buf.writeInt(0);
                buf.m_130064_(pos);
            });
        }
    }
}
