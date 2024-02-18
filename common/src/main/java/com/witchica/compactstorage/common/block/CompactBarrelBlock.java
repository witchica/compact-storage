package com.witchica.compactstorage.common.block;

import com.witchica.compactstorage.CompactStoragePlatform;
import com.witchica.compactstorage.common.block.entity.CompactBarrelBlockEntity;
import com.witchica.compactstorage.common.util.CompactStorageUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiConsumer;

public abstract class CompactBarrelBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = DirectionProperty.create("facing");
    public static final BooleanProperty OPEN = BooleanProperty.create("open");


    public CompactBarrelBlock(BlockBehaviour.Properties settings) {
        super(settings);
        registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(OPEN, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return getStateDefinition().any().setValue(FACING, ctx.getNearestLookingDirection().getOpposite()).setValue(OPEN, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, placer, itemStack);

        if (itemStack.hasCustomHoverName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof CompactBarrelBlockEntity) {
                ((CompactBarrelBlockEntity) blockEntity).setCustomName(itemStack.getHoverName());
            }
        }

        if (!world.isClientSide && itemStack.hasTag()) {
            CompoundTag nbt = itemStack.getTag();

            if (nbt.contains("inventory_width") && nbt.contains("inventory_height")) {
                BlockEntity blockEntity = world.getBlockEntity(pos);

                if (blockEntity instanceof CompactBarrelBlockEntity) {
                    CompactBarrelBlockEntity compactBarrelBlockEntity = (CompactBarrelBlockEntity) blockEntity;
                    compactBarrelBlockEntity.inventoryWidth = nbt.getInt("inventory_width");
                    compactBarrelBlockEntity.inventoryHeight = nbt.getInt("inventory_height");
                    compactBarrelBlockEntity.resizeInventory(false);
                    compactBarrelBlockEntity.setChanged();
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

                if(blockEntity instanceof CompactBarrelBlockEntity compactBarrelBlockEntity) {
                    Item heldItem = player.getItemInHand(hand).getItem();

                    if(heldItem == CompactStoragePlatform.getStorageRowUpgradeItem()) {
                        if(compactBarrelBlockEntity.increaseSize(1, 0)) {
                            player.getItemInHand(hand).shrink(1);
                            player.displayClientMessage(Component.translatable("text.compact_storage.upgrade_success").withStyle(ChatFormatting.GREEN), true);
                            player.playNotifySound(SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 1f, 1f);
                            return InteractionResult.CONSUME_PARTIAL;
                        } else {
                            player.playNotifySound(SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1f, 1f);
                            player.displayClientMessage(Component.translatable("text.compact_storage.upgrade_fail_maxsize").withStyle(ChatFormatting.RED), true);
                            return InteractionResult.FAIL;
                        }
                    } else if(heldItem == CompactStoragePlatform.getStorageColumnUpgradeItem()) {
                        if(compactBarrelBlockEntity.increaseSize(0, 1)) {
                            player.getItemInHand(hand).shrink(1);
                            player.displayClientMessage(Component.translatable("text.compact_storage.upgrade_success").withStyle(ChatFormatting.GREEN), true);
                            player.playNotifySound(SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 1f, 1f);
                            return InteractionResult.CONSUME_PARTIAL;
                        } else {
                            player.playNotifySound(SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1f, 1f);
                            player.displayClientMessage(Component.translatable("text.compact_storage.upgrade_fail_maxsize").withStyle(ChatFormatting.RED), true);
                            return InteractionResult.FAIL;
                        }
                    } else if(heldItem instanceof DyeItem dyeItem) {
                        Block newBlock = CompactStoragePlatform.getCompactBarrelFromDyeColor(dyeItem.getDyeColor());
                        world.setBlockAndUpdate(pos, newBlock.defaultBlockState().setValue(FACING, state.getValue(FACING)));
                        player.playNotifySound(SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 1f, 1f);
                        player.getItemInHand(hand).shrink(1);
                        return InteractionResult.CONSUME_PARTIAL;
                    }
                }

                MenuProvider screenHandlerFactory = state.getMenuProvider(world, pos);

                if (screenHandlerFactory != null) {
                    player.openMenu(screenHandlerFactory);
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        CompactStorageUtil.dropContents(world, pos, state.getBlock(), player);
        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public void onExplosionHit(BlockState state, Level world, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> stackMerger) {
        CompactStorageUtil.dropContents(world, pos, this, null);
        super.onExplosionHit(state, world, pos, explosion, stackMerger);
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
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        if(type == CompactStoragePlatform.getCompactBarrelBlockEntityType()) {
            return (world1, pos, state1, be) -> CompactBarrelBlockEntity.tick(world1, pos, state1, (CompactBarrelBlockEntity)  be);
        } else {
            return null;
        }
    }
}
