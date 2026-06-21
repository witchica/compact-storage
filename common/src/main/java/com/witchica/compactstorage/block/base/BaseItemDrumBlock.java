package com.witchica.compactstorage.block.base;

import com.witchica.compactstorage.api.RedyeableBlock;
import com.witchica.compactstorage.api.StorageTypeProvider;
import com.witchica.compactstorage.block.entity.base.BaseItemDrumBlockEntity;
import com.witchica.compactstorage.data.StorageType;
import com.witchica.compactstorage.inventory.DrumInventory;
import com.witchica.compactstorage.block.ModBlocks;
import com.witchica.compactstorage.util.CompactStorageUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
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

public abstract class BaseItemDrumBlock extends BaseEntityBlock implements StorageTypeProvider, RedyeableBlock {
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


    public boolean extractItem(Level world, BlockPos pos, Player player, int count) {
        BaseItemDrumBlockEntity drumBlockEntity = (BaseItemDrumBlockEntity) world.getBlockEntity(pos);

        DrumInventory inventory = drumBlockEntity.getDrumInventory();
        ItemStack extracted = inventory.removeItem(0, count);

        if(!extracted.isEmpty()) {
            world.addFreshEntity(new ItemEntity(world, player.getBlockX(), player.getBlockY(), player.getBlockZ(), extracted));
            world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
            return true;
        }

        return false;
    }
    
    public boolean extractItem(Level world, BlockPos pos, Player player) {
        BaseItemDrumBlockEntity drumBlockEntity = (BaseItemDrumBlockEntity) world.getBlockEntity(pos);
        DrumInventory inventory = drumBlockEntity.getDrumInventory();
        return extractItem(world, pos, player, inventory.getMaxStackSize());
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
            // Check if is an axe or pickaxe and do not extract, stops drops when mining it.
            if(!player.getItemInHand(InteractionHand.MAIN_HAND).is(storageType.isWooden() ? ItemTags.AXES : ItemTags.PICKAXES)) {
                if(player.isShiftKeyDown()) {
                    extractItem(level, pos, player);
                } else {
                    extractItem(level, pos, player, 1);
                }
            }
        }
        super.attack(state, level, pos, player);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(!level.isClientSide()) {
            // Extract whole stacks on shift
            if(player.isShiftKeyDown()) {
                if(extractItem(level, pos, player)) {
                    return InteractionResult.CONSUME;
                }
            } else {
                // Upgrades and dyeing
                InteractionResult result = CompactStorageUtil.onUseWithItem(stack, level, pos, state, player, getStorageType(), this);

                if(result != InteractionResult.PASS) {
                    return result;
                }

                // If not upgrade or dye, try and do an insert
                if(!insertItem(level, pos, player, hand)) {
                    // If both hands are NOT the stored item, try extract via the use function
                    BaseItemDrumBlockEntity drumBlockEntity = (BaseItemDrumBlockEntity) level.getBlockEntity(pos);
                    DrumInventory inventory = drumBlockEntity.getDrumInventory();

                    if(!player.getItemInHand(InteractionHand.MAIN_HAND).is(inventory.getItemType().getItem()) && !player.getItemInHand(InteractionHand.OFF_HAND).is(inventory.getItemType().getItem())) {
                        return InteractionResult.TRY_WITH_EMPTY_HAND;
                    }
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState getBlockStateOnRedye(BlockState state, DyeColor dyeColor) {
        return ModBlocks.compactChests.get(StorageType.fromDye(dyeColor)).defaultBlockState().setValue(RETAINING, state.getValue(RETAINING)).setValue(FACING, state.getValue(FACING));
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
