package me.tobystrong.compactstorage.block;

import net.minecraft.block.Block;

// import javax.annotation.Nullable;

public class BarrelBlock extends Block
{
    public BarrelBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }
//    public static final DirectionProperty FACING = DirectionProperty.of("facing", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
//
//    public BarrelBlock(Block.Settings settings) {
//        super(settings);
//        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH));
//    }
//
//    @Override
//    public BlockState getPlacementState(ItemPlacementContext ctx) {
//        return getStateManager().getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
//    }
//
//    @Override
//    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
//        builder.add(FACING);
//    }
//
//    @Override
//    public BlockEntity createBlockEntity(BlockView view) {
//        return new BarrelBlockEntity();
//    }
//
//    @Override
//    public BlockRenderType getRenderType(BlockState state) {
//        return BlockRenderType.MODEL;
//    }
//
//    @Override
//    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
//        if(!world.isClient && world.getBlockEntity(pos) instanceof BarrelBlockEntity) {
//            BarrelBlockEntity blockEntity = (BarrelBlockEntity) world.getBlockEntity(pos);
//
//            if(player.isSneaking()) {
//                for(int i = 0; i < blockEntity.getInvSize(); i++) {
//                    ItemStack stack = blockEntity.getInvStack(i);
//
//
//                    if(!stack.isEmpty()) {
//                        player.sendMessage(stack.getName());
//                    }
//                }
//            }
//
//            if(!player.getStackInHand(hand).isEmpty()) {
//                player.setStackInHand(hand, blockEntity.insertItem(player.getStackInHand(hand)));
//                return ActionResult.SUCCESS;
//            }
//        }
//
//        return ActionResult.SUCCESS;
//    }
//
//    @Override
//    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
//        if(!world.isClient && world.getBlockEntity(pos) instanceof BarrelBlockEntity) {
//            BarrelBlockEntity blockEntity = (BarrelBlockEntity) world.getBlockEntity(pos);
//            player.giveItemStack(blockEntity.dropItem());
//        }
//
//        super.onBlockBreakStart(state, world, pos, player);
//    }
//
//    @Override
//    public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
//        //if the block is actually gone
//        if(state.getBlock() != newState.getBlock()) {
//            BlockEntity entity = world.getBlockEntity(pos);
//
//            //and is our block then scatter items and update comparators
//            if(entity instanceof  BarrelBlockEntity) {
//                BarrelBlockEntity blockEntity = (BarrelBlockEntity) entity;
//                ItemScatterer.spawn(world, pos, blockEntity.barrel_items);
//                world.updateHorizontalAdjacent(pos, this);
//            }
//        }
//        super.onBlockRemoved(state, world, pos, newState, moved);
//    }
}
