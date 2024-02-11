package com.witchica.compactstorage.block;

import com.mojang.serialization.MapCodec;
import com.witchica.compactstorage.block.entity.DrumBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DrumBlock extends BlockWithEntity {
    public static final DirectionProperty FACING = DirectionProperty.of("facing");
    public static final MapCodec<DrumBlock> CODEC = createCodec(DrumBlock::new);
    public DrumBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(FACING, ctx.getPlayerLookDirection().getOpposite());
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return super.mirror(state, mirror).with(FACING, state.get(FACING).getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return super.rotate(state, rotation).with(FACING, state.get(FACING).rotateYClockwise());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }


    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);

        tooltip.add(Text.translatable("text.compact_storage.drum.tooltip_1").formatted(Formatting.GRAY, Formatting.ITALIC));
        tooltip.add(Text.translatable("text.compact_storage.drum.tooltip_2").formatted(Formatting.GRAY, Formatting.ITALIC));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DrumBlockEntity(pos, state);
    }

    public void extractItem(World world, BlockPos pos, PlayerEntity player) {
        DrumBlockEntity drumBlockEntity = (DrumBlockEntity) world.getBlockEntity(pos);
        SimpleInventory inventory = drumBlockEntity.inventory;

        ItemStack extracted = inventory.removeStack(0);

        if(!extracted.isEmpty()) {
            world.spawnEntity(new ItemEntity(world, player.getBlockX(), player.getBlockY(), player.getBlockZ(), extracted));
            world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1f, 1f);
        }
    }

    public void insertItem(World world, BlockPos pos, PlayerEntity player, Hand hand) {
        ItemStack itemInHand = player.getStackInHand(hand);

        DrumBlockEntity drum = (DrumBlockEntity) world.getBlockEntity(pos);
        InventoryStorage inventory = drum.inventoryWrapper;
        SimpleInventory itemHandler = drum.inventory;

        boolean completed = false;

        if(itemInHand.isEmpty() && drum.hasAnyItems()) {
            Inventory playerInventory = player.getInventory();

            for(int i = 0; i < playerInventory.size(); i++) {
                ItemStack itemStack = playerInventory.getStack(i);
                if(itemHandler.isValid(0, itemStack)) {
                    ItemStack returned = itemHandler.addStack(itemStack);

                    if(itemStack.getCount() != returned.getCount()) {
                        playerInventory.setStack(i, returned);
                        completed = true;
                        break;
                    }
                }
            }
        } else {
            ItemStack itemStack = player.getStackInHand(hand);

            if(itemHandler.isValid(0, itemStack)) {
                ItemStack returned = itemHandler.addStack(itemStack);

                if(itemStack.getCount() != returned.getCount()) {
                    player.setStackInHand(hand, returned);
                    completed = true;
                }
            }
        }

        if(completed) {
            world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1f, 1f);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!world.isClient) {
            if(player.isSneaking()) {
               extractItem(world, pos, player);
            } else {
                insertItem(world, pos, player, hand);
            }
        }

        return ActionResult.CONSUME;
    }

    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        if(!world.isClient) {
            extractItem(world, pos, player);
        }

        super.onBlockBreakStart(state, world, pos, player);
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if(blockEntity instanceof DrumBlockEntity drumBlock) {
            int totalItemCount = drumBlock.getTotalItemCount();
            int stackSize = drumBlock.getStoredType().getMaxCount();
            int output = MathHelper.floor(((totalItemCount / (float) stackSize) / 64f) * 15f);
            return output;
        }

        return 0;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if(!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if(blockEntity instanceof DrumBlockEntity drumBlock) {
                ItemScatterer.spawn(world, pos, drumBlock.inventory);
                world.updateComparators(pos, state.getBlock());
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }
}
