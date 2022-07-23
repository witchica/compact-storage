package com.tabithastrong.compactstorage.block;

import com.tabithastrong.compactstorage.CompactStorage;
import com.tabithastrong.compactstorage.block.entity.CompactChestBlockEntity;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.sound.Sound;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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

                if(blockEntity instanceof CompactChestBlockEntity compactChestBlockEntity) {
                    Item heldItem = player.getStackInHand(hand).getItem();

                    if(heldItem == CompactStorage.UPGRADE_ROW_ITEM) {
                        if(compactChestBlockEntity.increaseSize(1, 0)) {
                            player.getStackInHand(hand).decrement(1);
                            player.sendMessage(Text.translatable("text.compact_storage.upgrade_success").formatted(Formatting.GREEN), true);
                            player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1f, 1f);
                            return ActionResult.CONSUME_PARTIAL;
                        } else {
                            player.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1f, 1f);
                            player.sendMessage(Text.translatable("text.compact_storage.upgrade_fail_maxsize").formatted(Formatting.RED), true);
                            return ActionResult.FAIL;
                        }
                    } else if(heldItem == CompactStorage.UPGRADE_COLUMN_ITEM) {
                        if(compactChestBlockEntity.increaseSize(0, 1)) {
                            player.getStackInHand(hand).decrement(1);
                            player.sendMessage(Text.translatable("text.compact_storage.upgrade_success").formatted(Formatting.GREEN), true);
                            player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1f, 1f);
                            return ActionResult.CONSUME_PARTIAL;
                        } else {
                            player.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1f, 1f);
                            player.sendMessage(Text.translatable("text.compact_storage.upgrade_fail_maxsize").formatted(Formatting.RED), true);
                            return ActionResult.FAIL;
                        }
                    } else if(heldItem instanceof DyeItem dyeItem) {
                        Block newBlock = CompactStorage.DYE_COLOR_TO_COMPACT_CHEST_MAP.get(dyeItem.getColor());
                        world.setBlockState(pos, newBlock.getDefaultState().with(FACING, state.get(FACING)));
                        player.playSound(SoundEvents.BLOCK_SLIME_BLOCK_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                        player.getStackInHand(hand).decrement(1);
                        return ActionResult.CONSUME_PARTIAL;
                     }
                }

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
                // TranslatableText("compact_storage.text.too_many_rows"), true);//Todo: what
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
                // TranslatableText("compact_storage.text.too_many_columns"), true);
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
        if(newState.getBlock() instanceof CompactChestBlock && newState.get(FACING) == state.get(FACING)) {
            return;
        }

        BlockEntity blockEntity = world.getBlockEntity(pos);

        if(blockEntity instanceof CompactChestBlockEntity compactChestBlockEntity) {
            ItemStack chestStack = new ItemStack(this, 1);

            NbtCompound chestTag = new NbtCompound();
            chestTag.putInt("inventory_width", compactChestBlockEntity.inventoryWidth);
            chestTag.putInt("inventory_height", compactChestBlockEntity.inventoryHeight);
            chestStack.setNbt(chestTag);

            ItemScatterer.spawn(world, pos, (Inventory) compactChestBlockEntity);
            ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), chestStack);
            world.updateComparators(pos, this);
        }

        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);
        int inventoryX = 9;
        int inventoryY = 3;

        if(stack.hasNbt() && stack.getNbt().contains("inventory_width")) {
            inventoryX = stack.getNbt().getInt("inventory_width");
            inventoryY = stack.getNbt().getInt("inventory_height");
        }

        int slots = inventoryX * inventoryY;

        tooltip.add(Text.translatable("text.compact_storage.tooltip.size_x", inventoryX).formatted(Formatting.GRAY, Formatting.ITALIC));
        tooltip.add(Text.translatable("text.compact_storage.tooltip.size_y", inventoryY).formatted(Formatting.GRAY, Formatting.ITALIC));
        tooltip.add(Text.translatable("text.compact_storage.tooltip.slots", slots).formatted(Formatting.DARK_PURPLE, Formatting.ITALIC));
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
