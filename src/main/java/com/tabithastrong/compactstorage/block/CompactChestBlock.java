package com.tabithastrong.compactstorage.block;

import com.tabithastrong.compactstorage.CompactStorage;
import com.tabithastrong.compactstorage.block.entity.CompactChestBlockEntity;
import com.tabithastrong.compactstorage.util.CompactStorageUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CompactChestBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.NORTH, Direction.EAST,
            Direction.SOUTH, Direction.WEST);
    public static final VoxelShape CHEST_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);

    public CompactChestBlock(Properties settings) {
        super(settings);
        registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return getStateDefinition().any().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, placer, itemStack);

        if (itemStack.hasCustomHoverName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof CompactChestBlockEntity) {
                ((CompactChestBlockEntity) blockEntity).setCustomName(itemStack.getHoverName());
            }
        }

        if (!world.isClientSide && itemStack.hasTag()) {
            CompoundTag nbt = itemStack.getTag();

            if (nbt.contains("inventory_width") && nbt.contains("inventory_height")) {
                BlockEntity blockEntity = world.getBlockEntity(pos);

                if (blockEntity instanceof CompactChestBlockEntity) {
                    CompactChestBlockEntity compactChestBlockEntity = (CompactChestBlockEntity) blockEntity;
                    compactChestBlockEntity.inventoryWidth = nbt.getInt("inventory_width");
                    compactChestBlockEntity.inventoryHeight = nbt.getInt("inventory_height");
                    compactChestBlockEntity.resizeInventory();
                    compactChestBlockEntity.setChanged();
                }
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
            BlockHitResult hit) {
        if (!world.isClientSide) {
            if (!world.isClientSide) {
                BlockEntity blockEntity = world.getBlockEntity(pos);

                if(blockEntity instanceof CompactChestBlockEntity compactChestBlockEntity) {
                    Item heldItem = player.getItemInHand(hand).getItem();

                    if(heldItem == CompactStorage.UPGRADE_ROW_ITEM.get()) {
                        if(compactChestBlockEntity.increaseSize(1, 0)) {
                            player.getItemInHand(hand).shrink(1);
                            player.displayClientMessage(new TranslatableComponent("text.compact_storage.upgrade_success").withStyle(ChatFormatting.GREEN), true);
                            player.playNotifySound(SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 1f, 1f);
                            return InteractionResult.CONSUME_PARTIAL;
                        } else {
                            player.playNotifySound(SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1f, 1f);
                            player.displayClientMessage(new TranslatableComponent("text.compact_storage.upgrade_fail_maxsize").withStyle(ChatFormatting.RED), true);
                            return InteractionResult.FAIL;
                        }
                    } else if(heldItem == CompactStorage.UPGRADE_COLUMN_ITEM.get()) {
                        if(compactChestBlockEntity.increaseSize(0, 1)) {
                            player.getItemInHand(hand).shrink(1);
                            player.displayClientMessage(new TranslatableComponent("text.compact_storage.upgrade_success").withStyle(ChatFormatting.GREEN), true);
                            player.playNotifySound(SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 1f, 1f);
                            return InteractionResult.CONSUME_PARTIAL;
                        } else {
                            player.playNotifySound(SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1f, 1f);
                            player.displayClientMessage(new TranslatableComponent("text.compact_storage.upgrade_fail_maxsize").withStyle(ChatFormatting.RED), true);
                            return InteractionResult.FAIL;
                        }
                    } else if(heldItem instanceof DyeItem dyeItem) {
                        Block newBlock = CompactStorage.DYE_COLOR_TO_COMPACT_CHEST_MAP.get(dyeItem.getDyeColor()).get();
                        world.setBlockAndUpdate(pos, newBlock.defaultBlockState().setValue(FACING, state.getValue(FACING)));
                        player.playNotifySound(SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 1f, 1f);
                        player.getItemInHand(hand).shrink(1);
                        return InteractionResult.CONSUME_PARTIAL;
                     }

                    MenuProvider screenHandlerFactory = state.getMenuProvider(world, pos);
                    NetworkHooks.openGui((ServerPlayer) player, screenHandlerFactory, compactChestBlockEntity::writeScreenOpeningData);
                }
            }
        }

        return InteractionResult.SUCCESS;
    }



    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return CHEST_SHAPE;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if(newState.getBlock() instanceof CompactChestBlock && newState.getValue(FACING) == state.getValue(FACING)) {
            return;
        }

        BlockEntity blockEntity = world.getBlockEntity(pos);

        if(blockEntity instanceof CompactChestBlockEntity compactChestBlockEntity) {
            ItemStack chestStack = new ItemStack(this, 1);

            CompoundTag chestTag = new CompoundTag();
            chestTag.putInt("inventory_width", compactChestBlockEntity.inventoryWidth);
            chestTag.putInt("inventory_height", compactChestBlockEntity.inventoryHeight);

            if(compactChestBlockEntity.inventoryWidth != 9 || compactChestBlockEntity.inventoryHeight != 6) {
                chestStack.setTag(chestTag);
            }

            if(compactChestBlockEntity.hasCustomName()) {
                chestStack.setHoverName(compactChestBlockEntity.getCustomName());
            }

            Containers.dropContents(world, pos, (Container) compactChestBlockEntity);
            Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), chestStack);
            world.updateNeighbourForOutputSignal(pos, this);
        }

        super.onRemove(state, world, pos, newState, moved);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag options) {
        super.appendHoverText(stack, world, tooltip, options);
        CompactStorageUtil.appendTooltip(stack, world, tooltip, options, false);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CompactChestBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, CompactStorage.COMPACT_CHEST_ENTITY_TYPE.get(), (world1, pos, state1, be) -> CompactChestBlockEntity.tick(world1, pos, state1, be));
    }
}
