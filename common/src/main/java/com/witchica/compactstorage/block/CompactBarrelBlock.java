package com.witchica.compactstorage.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.witchica.compactstorage.block.base.BaseCompactStorageBlock;
import com.witchica.compactstorage.block.entity.CompactBarrelBlockEntity;
import com.witchica.compactstorage.data.StorageType;
import com.witchica.compactstorage.mod.CompactStorageBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jspecify.annotations.Nullable;

public class CompactBarrelBlock extends BaseCompactStorageBlock {
    public static BooleanProperty OPEN = BooleanProperty.create("open");
    public static EnumProperty<Direction> FACING = EnumProperty.create("facing", Direction.class);
    public static BooleanProperty RETAINING = BooleanProperty.create("retaining");

    public static final MapCodec<CompactBarrelBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(StorageType.CODEC.fieldOf("storageType").forGetter(CompactBarrelBlock::getStorageType), propertiesCodec()).apply(instance, CompactBarrelBlock::new));

    public CompactBarrelBlock(StorageType storageType, Properties properties) {
        super(storageType, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(OPEN, Boolean.FALSE).setValue(FACING, Direction.NORTH).setValue(RETAINING, false));
    }

    @Override
    protected BlockState getBlockStateOnRedye(BlockState state, DyeItem dyeItem) {
        return CompactStorageBlocks.compactBarrels.get(StorageType.fromDye(dyeItem.getDyeColor())).defaultBlockState().setValue(FACING, state.getValue(FACING)).setValue(OPEN, state.getValue(OPEN)).setValue(RETAINING, state.getValue(RETAINING));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CompactBarrelBlockEntity(blockPos, blockState);
    }
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos blockPos, Direction direction) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(blockPos));
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OPEN, FACING, RETAINING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }
}
