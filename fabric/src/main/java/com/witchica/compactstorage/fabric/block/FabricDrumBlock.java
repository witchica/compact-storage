package com.witchica.compactstorage.fabric.block;

import com.witchica.compactstorage.common.block.DrumBlock;
import com.witchica.compactstorage.fabric.block.entity.FabricDrumBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FabricDrumBlock extends DrumBlock {
    public FabricDrumBlock(Properties settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FabricDrumBlockEntity(pos, state);
    }
}
