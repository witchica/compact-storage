package com.witchica.compactstorage.block.base;

import com.witchica.compactstorage.api.StorageTypeProvider;
import com.witchica.compactstorage.data.StorageUpgrade;
import com.witchica.compactstorage.block.entity.base.BaseItemDrumBlockEntity;
import com.witchica.compactstorage.data.StorageType;
import com.witchica.compactstorage.inventory.DrumInventory;
import com.witchica.compactstorage.mod.CompactStorageBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public abstract class BaseItemDrumBlock extends BaseEntityBlock implements StorageTypeProvider {
    private final StorageType storageType;

    public static final EnumProperty<Direction> FACING = EnumProperty.create("facing", Direction.class);
    public static final BooleanProperty RETAINING = BaseCompactStorageBlock.RETAINING;

    protected BaseItemDrumBlock(StorageType storageType, Properties properties) {
        super(properties);
        this.storageType = storageType;
        registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(RETAINING, false));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(FACING, context.getNearestLookingDirection().getOpposite());
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, RETAINING);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BaseItemDrumBlockEntity(blockPos, blockState);
    }

    @Override
    public @NonNull StorageType getStorageType() {
        return this.storageType;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }
    
    public boolean extractItem(Level world, BlockPos pos, Player player) {
        BaseItemDrumBlockEntity drumBlockEntity = (BaseItemDrumBlockEntity) world.getBlockEntity(pos);
        DrumInventory inventory = drumBlockEntity.getDrumInventory();

        ItemStack extracted = inventory.removeItemNoUpdate(0);

        if(!extracted.isEmpty()) {
            world.addFreshEntity(new ItemEntity(world, player.getBlockX(), player.getBlockY(), player.getBlockZ(), extracted));
            world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
            return true;
        }

        return false;
    }

    public boolean insertItem(Level world, BlockPos pos, Player player, InteractionHand hand) {
        BaseItemDrumBlockEntity drumBlockEntity = (BaseItemDrumBlockEntity) world.getBlockEntity(pos);
        DrumInventory inventory = drumBlockEntity.getDrumInventory();

        boolean completed = false;

        if(player.getItemInHand(hand).isEmpty() && drumBlockEntity.hasAnyItems()) {
            Container playerInventory = player.getInventory();

            for(int i = 0; i < playerInventory.getContainerSize(); i++) {
                ItemStack itemStack = playerInventory.getItem(i);
                if(inventory.canPlaceItem(0, itemStack)) {
                    ItemStack returned = inventory.addItem(itemStack);

                    if(itemStack.getCount() != returned.getCount()) {
                        playerInventory.setItem(i, returned);
                        completed = true;
                        break;
                    }
                }
            }
        } else {
            ItemStack itemStack = player.getItemInHand(hand);

            if(inventory.canPlaceItem(0, itemStack)) {
                ItemStack returned = inventory.addItem(itemStack);

                if(itemStack.getCount() != returned.getCount()) {
                    player.setItemInHand(hand, returned);
                    completed = true;
                }
            }
        }

        if(completed) {
            world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
        }

        return completed;
    }

    @Override
    protected void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if(!level.isClientSide()) {
            extractItem(level, pos, player);
        }
        super.attack(state, level, pos, player);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(!level.isClientSide()) {
            if(player.isShiftKeyDown()) {
                if(extractItem(level, pos, player)) {
                    return InteractionResult.CONSUME;
                }
            } else {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                InteractionResult upgradeResult = StorageUpgrade.applyToBlockEntity(stack, blockEntity, player, level);

                if(upgradeResult != InteractionResult.PASS) {
                    return upgradeResult;
                }

                if (storageType.canDye() && stack.getItem() instanceof DyeItem) {
                    StorageType newType = StorageType.fromDye(stack.get(DataComponents.DYE));

                    if(newType != storageType) {
                        level.setBlock(pos, getBlockStateOnRedye(state, newType.getDyeColor()), 3);
                        stack.setCount(stack.getCount() - 1);
                        level.playSound(null, pos, SoundEvents.SLIME_SQUISH, SoundSource.BLOCKS, 1f, 1f);
                        return InteractionResult.CONSUME;
                    }
                }

                if(!insertItem(level, pos, player, hand)) {
                    if(player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && player.getItemInHand(InteractionHand.OFF_HAND).isEmpty()) {
                        return InteractionResult.TRY_WITH_EMPTY_HAND;
                    }
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    private BlockState getBlockStateOnRedye(BlockState state, DyeColor dyeColor) {
        return CompactStorageBlocks.compactChests.get(StorageType.fromDye(dyeColor)).defaultBlockState().setValue(RETAINING, state.getValue(RETAINING)).setValue(FACING, state.getValue(FACING));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(!level.isClientSide()) {
            if(extractItem(level, pos, player)) {
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.SUCCESS;
    }
}
