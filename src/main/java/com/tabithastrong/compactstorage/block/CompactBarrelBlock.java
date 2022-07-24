package com.tabithastrong.compactstorage.block;

import com.tabithastrong.compactstorage.CompactStorage;
import com.tabithastrong.compactstorage.block.entity.CompactBarrelBlockEntity;
import com.tabithastrong.compactstorage.block.entity.CompactChestBlockEntity;
import com.tabithastrong.compactstorage.util.CompactStorageUtil;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
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
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
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

public class CompactBarrelBlock extends BlockWithEntity {
    public static final DirectionProperty FACING = DirectionProperty.of("facing");
    public static final BooleanProperty OPEN = BooleanProperty.of("open");

    public CompactBarrelBlock(AbstractBlock.Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(OPEN, false));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getStateManager().getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite()).with(OPEN, false);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        if (itemStack.hasCustomName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof CompactBarrelBlockEntity) {
                ((CompactBarrelBlockEntity) blockEntity).setCustomName(itemStack.getName());
            }
        }

        if (!world.isClient && itemStack.hasNbt()) {
            NbtCompound nbt = itemStack.getNbt();

            if (nbt.contains("inventory_width") && nbt.contains("inventory_height")) {
                BlockEntity blockEntity = world.getBlockEntity(pos);

                if (blockEntity instanceof CompactBarrelBlockEntity) {
                    CompactBarrelBlockEntity compactBarrelBlockEntity = (CompactBarrelBlockEntity) blockEntity;
                    compactBarrelBlockEntity.inventoryWidth = nbt.getInt("inventory_width");
                    compactBarrelBlockEntity.inventoryHeight = nbt.getInt("inventory_height");
                    compactBarrelBlockEntity.resizeInventory(false);
                    compactBarrelBlockEntity.markDirty();
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

                if(blockEntity instanceof CompactBarrelBlockEntity compactBarrelBlockEntity) {
                    Item heldItem = player.getStackInHand(hand).getItem();

                    if(heldItem == CompactStorage.UPGRADE_ROW_ITEM) {
                        if(compactBarrelBlockEntity.increaseSize(1, 0)) {
                            player.getStackInHand(hand).decrement(1);
                            player.sendMessage(new TranslatableText("text.compact_storage.upgrade_success").formatted(Formatting.GREEN), true);
                            player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1f, 1f);
                            return ActionResult.CONSUME_PARTIAL;
                        } else {
                            player.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1f, 1f);
                            player.sendMessage(new TranslatableText("text.compact_storage.upgrade_fail_maxsize").formatted(Formatting.RED), true);
                            return ActionResult.FAIL;
                        }
                    } else if(heldItem == CompactStorage.UPGRADE_COLUMN_ITEM) {
                        if(compactBarrelBlockEntity.increaseSize(0, 1)) {
                            player.getStackInHand(hand).decrement(1);
                            player.sendMessage(new TranslatableText("text.compact_storage.upgrade_success").formatted(Formatting.GREEN), true);
                            player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1f, 1f);
                            return ActionResult.CONSUME_PARTIAL;
                        } else {
                            player.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1f, 1f);
                            player.sendMessage(new TranslatableText("text.compact_storage.upgrade_fail_maxsize").formatted(Formatting.RED), true);
                            return ActionResult.FAIL;
                        }
                    } else if(heldItem instanceof DyeItem dyeItem) {
                        Block newBlock = CompactStorage.DYE_COLOR_TO_COMPACT_BARREL_MAP.get(dyeItem.getColor());
                        world.setBlockState(pos, newBlock.getDefaultState().with(FACING, state.get(FACING)));
                        player.playSound(SoundEvents.BLOCK_SLIME_BLOCK_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                        player.getStackInHand(hand).decrement(1);
                        return ActionResult.CONSUME_PARTIAL;
                    }
                }

                NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);

                if (screenHandlerFactory != null) {
                    player.openHandledScreen(screenHandlerFactory);
                }
            }
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if(newState.getBlock() instanceof CompactBarrelBlock && newState.get(FACING) == state.get(FACING)) {
            return;
        }

        BlockEntity blockEntity = world.getBlockEntity(pos);

        if(blockEntity instanceof CompactBarrelBlockEntity compactBarrelBlockEntity) {
            ItemStack chestStack = new ItemStack(this, 1);

            NbtCompound chestTag = new NbtCompound();
            chestTag.putInt("inventory_width", compactBarrelBlockEntity.inventoryWidth);
            chestTag.putInt("inventory_height", compactBarrelBlockEntity.inventoryHeight);

            if(compactBarrelBlockEntity.inventoryWidth != 9 || compactBarrelBlockEntity.inventoryHeight != 6) {
                chestStack.setNbt(chestTag);
            }


            if(compactBarrelBlockEntity.hasCustomName()) {
                chestStack.setCustomName(compactBarrelBlockEntity.getCustomName());
            }

            ItemScatterer.spawn(world, pos, (Inventory) compactBarrelBlockEntity);
            ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), chestStack);
            world.updateComparators(pos, this);
        }

        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);
        CompactStorageUtil.appendTooltip(stack, world, tooltip, options, false);
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
        return new CompactBarrelBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, CompactStorage.COMPACT_BARREL_ENTITY_TYPE, (world1, pos, state1, be) -> CompactBarrelBlockEntity.tick(world1, pos, state1, be));
    }
}
