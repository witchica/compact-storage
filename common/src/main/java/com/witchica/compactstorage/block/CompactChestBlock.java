package com.witchica.compactstorage.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.witchica.compactstorage.block.entity.BaseCompactStorageBlockEntity;
import com.witchica.compactstorage.block.entity.CompactChestBlockEntity;
import com.witchica.compactstorage.block.entity.CompactStorageBlockEntities;
import com.witchica.compactstorage.item.StorageUpgradeItem;
import com.witchica.compactstorage.api.StorageTypeProvider;
import com.witchica.compactstorage.data.UpgradeType;
import net.blay09.mods.balm.Balm;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import com.witchica.compactstorage.data.StorageType;

import java.util.List;

public class CompactChestBlock extends BaseEntityBlock implements SimpleWaterloggedBlock, StorageTypeProvider {
    public static final EnumProperty<@NotNull Direction> FACING = EnumProperty.create("facing", Direction.class, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private final StorageType storageType;
    public static final MapCodec<CompactChestBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(StorageType.CODEC.fieldOf("storageType").forGetter(CompactChestBlock::getStorageType), propertiesCodec()).apply(instance, CompactChestBlock::new));
    private static VoxelShape SHAPE = Block.column((double)14.0F, (double)0.0F, (double)14.0F);

    protected CompactChestBlock(StorageType storageType, Properties blockProperties) {
        super(blockProperties);
        this.storageType = storageType;
        this.registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    public @NonNull StorageType getStorageType() {
        return this.storageType;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if(state.getValue(WATERLOGGED)) {
            scheduledTickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, level, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState state = context.getLevel().getFluidState(context.getClickedPos());
        return super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, state.getType() == Fluids.WATER);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? createTickerHelper(blockEntityType, CompactStorageBlockEntities.COMPACT_CHEST_ENTITY.value(), BaseCompactStorageBlockEntity::ticker) : null;
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CompactChestBlockEntity(blockPos, blockState);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    public static boolean isChestBlockedAt(LevelAccessor level, BlockPos pos) {
        return isBlockedChestByBlock(level, pos) || isCatSittingOnChest(level, pos);
    }

    private static boolean isBlockedChestByBlock(BlockGetter level, BlockPos pos) {
        BlockPos blockpos = pos.above();
        return level.getBlockState(blockpos).isRedstoneConductor(level, blockpos);
    }

    private static boolean isCatSittingOnChest(LevelAccessor level, BlockPos pos) {
        List<Cat> list = level.getEntitiesOfClass(Cat.class, new AABB((double)pos.getX(), (double)(pos.getY() + 1), (double)pos.getZ(), (double)(pos.getX() + 1), (double)(pos.getY() + 2), (double)(pos.getZ() + 1)));
        if (!list.isEmpty()) {
            for(Cat cat : list) {
                if (cat.isInSittingPose()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    protected boolean triggerEvent(BlockState state, Level level, BlockPos pos, int id, int param) {
        return super.triggerEvent(state, level, pos, id, param);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(!level.isClientSide()) {
            Balm.networking().openMenu(player, getMenuProvider(state, level, pos));
            return InteractionResult.CONSUME;
        }

        return InteractionResult.CONSUME;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(!level.isClientSide()) {
            if(stack.getItem() instanceof StorageUpgradeItem upgradeItem) {
                UpgradeType upgradeType = upgradeItem.getUpgradeType();
                if(level.getBlockEntity(pos) instanceof BaseCompactStorageBlockEntity baseCompactStorageBlockEntity) {
                    boolean applied = baseCompactStorageBlockEntity.applyUpgrade(upgradeType);

                    if(applied) {
                        stack.setCount(stack.getCount() - 1);
                        level.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 1f, 1f);
                        return InteractionResult.CONSUME;
                    } else {
                        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1f, 1f);
                        return InteractionResult.FAIL;
                    }
                }
            } else if (storageType.canDye() && stack.getItem() instanceof DyeItem dyeItem) {
                StorageType newType = StorageType.fromDye(dyeItem.getDyeColor());

                if(newType != storageType) {
                    level.setBlock(pos, CompactStorageBlocks.compactChests.get(newType).defaultBlockState().setValue(FACING, state.getValue(FACING)).setValue(WATERLOGGED, state.getValue(WATERLOGGED)), 1);
                    stack.setCount(stack.getCount() - 1);
                    level.playSound(null, pos, SoundEvents.SLIME_SQUISH, SoundSource.BLOCKS, 1f, 1f);
                    return InteractionResult.CONSUME;
                }
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }


}
