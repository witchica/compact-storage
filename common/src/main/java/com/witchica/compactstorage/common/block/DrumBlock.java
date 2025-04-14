package com.witchica.compactstorage.common.block;

import com.witchica.compactstorage.common.CompactStorage;
import com.witchica.compactstorage.common.block.entity.DrumBlockEntity;
import com.witchica.compactstorage.common.util.CompactStorageUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.*;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DrumBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = DirectionProperty.create("facing");
    public static final BooleanProperty RETAINING = BooleanProperty.create("retaining");
    private final CompactStorageUtil.StorageVisualTypes type;

    public DrumBlock(CompactStorageUtil.StorageVisualTypes type) {
        super(type.isWooden() ? Properties.copy(Blocks.BARREL) : Properties.copy(Blocks.BARREL).strength(2f, 5f));
        this.type = type;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        boolean retaining = ctx.getItemInHand().hasTag() ? ctx.getItemInHand().getTag().getBoolean("Retaining") : false;
        return super.getStateForPlacement(ctx).setValue(FACING, ctx.getNearestLookingDirection().getOpposite()).setValue(RETAINING, retaining);
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return super.mirror(state, mirror).setValue(FACING, state.getValue(FACING).getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return super.rotate(state, rotation).setValue(FACING, state.getValue(FACING).getClockWise());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, RETAINING);
    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag options) {
        super.appendHoverText(stack, world, tooltip, options);

        tooltip.add(Component.translatable("text.compact_storage.drum.tooltip_1").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        tooltip.add(Component.translatable("text.compact_storage.drum.tooltip_2").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));

        if(stack.hasTag() && stack.getTag().contains("Retaining") && stack.getTag().getBoolean("Retaining")) {
            tooltip.add(Component.translatable("tooltip.compact_storage.retaining").withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD));
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DrumBlockEntity(pos, state);
    }

    public void extractItem(Level world, BlockPos pos, Player player) {
        DrumBlockEntity drumBlockEntity = (DrumBlockEntity) world.getBlockEntity(pos);
        SimpleContainer inventory = drumBlockEntity.inventory;

        ItemStack extracted = inventory.removeItemNoUpdate(0);

        if(!extracted.isEmpty()) {
            world.addFreshEntity(new ItemEntity(world, player.getBlockX(), player.getBlockY(), player.getBlockZ(), extracted));
            world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
        }
    }

    public void insertItem(Level world, BlockPos pos, Player player, InteractionHand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);

        DrumBlockEntity drum = (DrumBlockEntity) world.getBlockEntity(pos);
        SimpleContainer itemHandler = drum.inventory;

        boolean completed = false;

        if(itemInHand.isEmpty() && drum.hasAnyItems()) {
            Container playerInventory = player.getInventory();

            for(int i = 0; i < playerInventory.getContainerSize(); i++) {
                ItemStack itemStack = playerInventory.getItem(i);
                if(itemHandler.canPlaceItem(0, itemStack)) {
                    ItemStack returned = itemHandler.addItem(itemStack);

                    if(itemStack.getCount() != returned.getCount()) {
                        playerInventory.setItem(i, returned);
                        completed = true;
                        break;
                    }
                }
            }
        } else {
            ItemStack itemStack = player.getItemInHand(hand);

            if(itemHandler.canPlaceItem(0, itemStack)) {
                ItemStack returned = itemHandler.addItem(itemStack);

                if(itemStack.getCount() != returned.getCount()) {
                    player.setItemInHand(hand, returned);
                    completed = true;
                }
            }
        }

        if(completed) {
            world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if(!world.isClientSide) {
            if(player.getItemInHand(hand).getItem() == CompactStorage.UPGRADE_RETAINER_ITEM.get()) {
                if (world.getBlockEntity(pos) instanceof DrumBlockEntity drumBlockEntity) {
                    drumBlockEntity.setRetaining();
                    player.getItemInHand(hand).shrink(1);
                    return InteractionResult.CONSUME_PARTIAL;
                }
            } else {
                if(!type.isWooden() && player.getItemInHand(hand).getItem() instanceof DyeItem dyeItem) {
                    if (dyeItem.getDyeColor() != type.getAssociatedDyeColor()) {
                        Block newBlock = CompactStorage.getDrumFromDyeColor(dyeItem.getDyeColor());
                        world.setBlockAndUpdate(pos, newBlock.defaultBlockState().setValue(FACING, state.getValue(FACING)).setValue(RETAINING, state.getValue(RETAINING)));
                        player.playNotifySound(SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 1f, 1f);
                        player.getItemInHand(hand).shrink(1);
                        return InteractionResult.CONSUME_PARTIAL;
                    }
                }
                if(player.isShiftKeyDown()) {
                    extractItem(world, pos, player);
                } else {
                    insertItem(world, pos, player, hand);
                }
            }
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public void attack(BlockState state, Level world, BlockPos pos, Player player) {
        if(!world.isClientSide) {
            extractItem(world, pos, player);
        }

        super.attack(state, world, pos, player);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if(blockEntity instanceof DrumBlockEntity drumBlock) {
            int totalItemCount = drumBlock.getTotalItemCount();
            int stackSize = drumBlock.getStoredType().getMaxStackSize();
            int output = Mth.floor(((totalItemCount / (float) stackSize) / 64f) * 15f);
            return output;
        }

        return 0;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if(!level.isClientSide) {
            if(stack.hasTag() && stack.getTag().contains("Retaining")) {
                if(level.getBlockEntity(pos) instanceof DrumBlockEntity drumBlock) {
                    drumBlock.load(stack.getTag());
                    drumBlock.setChanged();
                }
            }
        }

        super.setPlacedBy(level, pos, state, placer, stack);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock()) && !(newState.getBlock() instanceof DrumBlock)) {
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof DrumBlockEntity drumBlock) {
                if (!drumBlock.getRetaining()) {
                    Containers.dropContents(world, pos, drumBlock.inventory);
                }

                ItemStack itemStack = new ItemStack(this, 1);
                drumBlock.saveToItem(itemStack);
                Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemStack);
                world.updateNeighbourForOutputSignal(pos, state.getBlock());
            }
            world.removeBlockEntity(pos);
        }
    }

    public CompactStorageUtil.StorageVisualTypes getType() {
        return type;
    }
}
