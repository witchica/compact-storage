package me.tobystrong.compactstorage.block;

import me.tobystrong.compactstorage.block.entity.BarrelBlockEntity;
import me.tobystrong.compactstorage.block.entity.BarrelBlockEntity.InsertItemsResult;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

// import javax.annotation.Nullable;

public class BarrelBlock extends BlockWithEntity
{
    public static final DirectionProperty FACING = DirectionProperty.of("facing", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);

    public BarrelBlock(Block.Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getStateManager().getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return new BarrelBlockEntity();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!world.isClient && world.getBlockEntity(pos) instanceof BarrelBlockEntity) {
            BarrelBlockEntity blockEntity = (BarrelBlockEntity) world.getBlockEntity(pos);

            if(!player.getStackInHand(hand).isEmpty()) {
                InsertItemsResult result = blockEntity.insertItems(player.getStackInHand(hand), player, false);

                if(result.success) {
                    player.setStackInHand(hand, result.returned);
                    return ActionResult.SUCCESS;
                }
            }
        }

        return ActionResult.SUCCESS;
    }
    
    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        if(!world.isClient && world.getBlockEntity(pos) instanceof BarrelBlockEntity) {
            BarrelBlockEntity blockEntity = (BarrelBlockEntity) world.getBlockEntity(pos);
            player.giveItemStack(blockEntity.dropItems(player, false));
        }

        super.onBlockBreakStart(state, world, pos, player);
    }

    @Override
    public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        //if the block is actually gone
        if(state.getBlock() != newState.getBlock()) {
            BlockEntity entity = world.getBlockEntity(pos);

            //and is our block then scatter items and update comparators
            if(entity instanceof  BarrelBlockEntity) {
                BarrelBlockEntity blockEntity = (BarrelBlockEntity) entity;
                ItemScatterer.spawn(world, pos, blockEntity.getItemsAsList());
                world.updateHorizontalAdjacent(pos, this);
            }
        }
        super.onBlockRemoved(state, world, pos, newState, moved);
    }
}
