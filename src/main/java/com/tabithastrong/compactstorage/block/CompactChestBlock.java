package com.tabithastrong.compactstorage.block;

import com.tabithastrong.compactstorage.CompactStorage;
import com.tabithastrong.compactstorage.block.entity.CompactChestBlockEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CompactChestBlock extends BlockWithEntity {
    public static final DirectionProperty FACING = DirectionProperty.of("facing", Direction.NORTH, Direction.EAST,
            Direction.SOUTH, Direction.WEST);
    public static final VoxelShape CHEST_SHAPE = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);

    public CompactChestBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getStateManager().getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        if (itemStack.hasCustomName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof CompactChestBlockEntity) {
                ((CompactChestBlockEntity) blockEntity).setCustomName(itemStack.getName());
            }
        }

        if (!world.isClient && itemStack.hasNbt()) {
            NbtCompound nbt = itemStack.getNbt();

            if (nbt.contains("inventory_width") && nbt.contains("inventory_height")) {
                BlockEntity blockEntity = world.getBlockEntity(pos);

                if (blockEntity instanceof CompactChestBlockEntity) {
                    CompactChestBlockEntity compactChestBlockEntity = (CompactChestBlockEntity) blockEntity;
                    compactChestBlockEntity.inventoryWidth = nbt.getInt("inventory_width");
                    compactChestBlockEntity.inventoryHeight = nbt.getInt("inventory_height");
                    compactChestBlockEntity.resizeInventory(false);
                    compactChestBlockEntity.markDirty();
                }
            }
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hit) {
        if (!world.isClient) {
            if (!world.isClient) {
                BlockEntity blockEntity = world.getBlockEntity(pos);

                // if(blockEntity instanceof CompactChestBlockEntity) {
                // CompactChestBlockEntity compactChestBlockEntity = (CompactChestBlockEntity)
                // blockEntity;
                // Item held_item = player.getStackInHand(hand).getItem();

                // if(held_item == CompactStorage.CHEST_UPGRADE_ROW &&
                // compactChestBlockEntity.inventory_width < 24) {
                // compactChestBlockEntity.inventory_width += 1;
                // player.getStackInHand(hand).decrement(1);
                // player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                // SoundCategory.BLOCKS, 1f, 1f);

                // compactChestBlockEntity.resizeInventory(true);
                // compactChestBlockEntity.markDirty();
                // compactChestBlockEntity.sync();

                // return ActionResult.SUCCESS;
                // } else if (held_item == CompactStorage.CHEST_UPGRADE_ROW) {
                // player.sendMessage(new
                // TranslatableText("compact-storage.text.too_many_rows"), true);//Todo: what
                // }

                // if(held_item == CompactStorage.CHEST_UPGRADE_COLUMN &&
                // compactChestBlockEntity.inventory_height < 12) {
                // compactChestBlockEntity.inventory_height += 1;
                // player.getStackInHand(hand).decrement(1);
                // player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                // SoundCategory.BLOCKS, 1f, 1f);

                // compactChestBlockEntity.resizeInventory(true);
                // compactChestBlockEntity.markDirty();
                // compactChestBlockEntity.sync();

                // return ActionResult.SUCCESS;
                // } else if (held_item == CompactStorage.CHEST_UPGRADE_COLUMN) {
                // player.sendMessage(new
                // TranslatableText("compact-storage.text.too_many_columns"), true);
                // }

                NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);

                if (screenHandlerFactory != null) {
                    player.openHandledScreen(screenHandlerFactory);
                }
            }
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return CHEST_SHAPE;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CompactChestBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, CompactStorage.COMPACT_CHEST_ENTITY_TYPE, (world1, pos, state1, be) -> CompactChestBlockEntity.tick(world1, pos, state1, be));
    }
}
