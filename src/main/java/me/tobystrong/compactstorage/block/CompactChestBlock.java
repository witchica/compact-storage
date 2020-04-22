package me.tobystrong.compactstorage.block;

import me.tobystrong.compactstorage.CompactStorage;
import me.tobystrong.compactstorage.block.entity.CompactChestBlockEntity;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.Container;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CompactChestBlock extends BlockWithEntity {
    public static final DirectionProperty FACING = DirectionProperty.of("facing", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
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
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return new CompactChestBlockEntity();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        //if the item has a custom name then name the entity the same name
        if(itemStack.hasCustomName()) {
            //get the entity
            BlockEntity blockEntity = world.getBlockEntity(pos);

            //cast and set the name
            if(blockEntity instanceof  CompactChestBlockEntity) {
                ((CompactChestBlockEntity) blockEntity).setCustomName(itemStack.getName());
            }
        }

        if(itemStack.hasTag() && itemStack.getTag().contains("inventory_width") && itemStack.getTag().contains("inventory_height") && !world.isClient) {
            //get the entity
            BlockEntity blockEntity = world.getBlockEntity(pos);

            //cast and set the name
            if(blockEntity instanceof  CompactChestBlockEntity) {
                ((CompactChestBlockEntity) blockEntity).inventory_width = itemStack.getTag().getInt("inventory_width");
                ((CompactChestBlockEntity) blockEntity).inventory_height = itemStack.getTag().getInt("inventory_height");
                ((CompactChestBlockEntity) blockEntity).resizeInventory(false);
                blockEntity.markDirty();
                ((CompactChestBlockEntity) blockEntity).sync();
            }
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if(blockEntity instanceof CompactChestBlockEntity) {
                CompactChestBlockEntity compactChestBlockEntity = (CompactChestBlockEntity) blockEntity;
                Item held_item = player.getStackInHand(hand).getItem();

                if(held_item == CompactStorage.CHEST_UPGRADE_ROW && compactChestBlockEntity.inventory_width < 24) {
                    compactChestBlockEntity.inventory_width += 1;
                    player.getStackInHand(hand).decrement(1);
                    player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1f, 1f);

                    compactChestBlockEntity.resizeInventory(true);
                    compactChestBlockEntity.markDirty();
                    compactChestBlockEntity.sync();

                    return ActionResult.SUCCESS;
                } else if (held_item == CompactStorage.CHEST_UPGRADE_ROW && compactChestBlockEntity.inventory_width >= 24) {
                    player.sendMessage(new TranslatableText("compact-storage.text.too_many_rows"));
                }

                if(held_item == CompactStorage.CHEST_UPGRADE_COLUMN && compactChestBlockEntity.inventory_height < 12) {
                    compactChestBlockEntity.inventory_height += 1;
                    player.getStackInHand(hand).decrement(1);
                    player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1f, 1f);

                    compactChestBlockEntity.resizeInventory(true);
                    compactChestBlockEntity.markDirty();
                    compactChestBlockEntity.sync();

                    return ActionResult.SUCCESS;
                } else if (held_item == CompactStorage.CHEST_UPGRADE_COLUMN && compactChestBlockEntity.inventory_height >= 12) {
                    player.sendMessage(new TranslatableText("compact-storage.text.too_many_columns"));
                }

                ContainerProviderRegistry.INSTANCE.openContainer(CompactStorage.COMPACT_CHEST_IDENTIFIER, player, buf -> buf.writeBlockPos(pos));
            }
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext context) {
        return CHEST_SHAPE;
    }

    @Override
    public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        //if the block is actually gone
        if(state.getBlock() != newState.getBlock()) {
            BlockEntity entity = world.getBlockEntity(pos);

            //and is our block then scatter items and update comparators
            if(entity instanceof  CompactChestBlockEntity) {
                CompactChestBlockEntity chestBlockEntity = (CompactChestBlockEntity) entity;
                ItemScatterer.spawn(world, pos, (Inventory) chestBlockEntity);

                ItemStack chest_stack = new ItemStack(CompactStorage.COMPACT_CHEST, 1);

                CompoundTag tag = new CompoundTag();
                tag.putInt("inventory_width", chestBlockEntity.inventory_width);
                tag.putInt("inventory_height", chestBlockEntity.inventory_height);

                chest_stack.setTag(tag);
                ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), chest_stack);
                world.updateHorizontalAdjacent(pos, this);
            }
        }
        super.onBlockRemoved(state, world, pos, newState, moved);
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return Container.calculateComparatorOutput(world.getBlockEntity(pos));
    }
}
