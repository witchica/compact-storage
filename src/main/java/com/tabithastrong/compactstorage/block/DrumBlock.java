package com.tabithastrong.compactstorage.block;

import com.tabithastrong.compactstorage.block.entity.DrumBlockEntity;
import com.tabithastrong.compactstorage.inventory.DrumItemStackHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DrumBlock extends Block implements EntityBlock {
    public DrumBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockBlockStateBuilder) {
        super.createBlockStateDefinition(blockBlockStateBuilder);
        blockBlockStateBuilder.add(DirectionalBlock.FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(DirectionalBlock.FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if(!level.isClientSide) {

            if(player.isCrouching()) {
                extractItemFromBlockEntity(level, pos, player);
            } else {
                insertItemInHand(level, pos, player, hand);
            }
            return InteractionResult.CONSUME;
        } else {
            return InteractionResult.CONSUME;
        }
    }
    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if(!level.isClientSide) {
            extractItemFromBlockEntity(level, pos, player);
        }
    }

    private static void extractItemFromBlockEntity(Level level, BlockPos pos, Player player) {
        DrumBlockEntity drumBlockEntity = (DrumBlockEntity) level.getBlockEntity(pos);
        DrumItemStackHandler itemHandler = drumBlockEntity.getDrumItemStackHandler();
        ItemStack extracted = itemHandler.extractItem(0, itemHandler.getLastSlotStackSize(), false);
        if(!extracted.isEmpty()) {
            level.addFreshEntity(new ItemEntity(level, player.getBlockX(), player.getBlockY(), player.getBlockZ(), extracted));
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
        }
    }

    private static void insertItemInHand(Level level, BlockPos pos, Player player, InteractionHand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);

        DrumBlockEntity drumBlockEntity = (DrumBlockEntity) level.getBlockEntity(pos);
        DrumItemStackHandler itemHandler = drumBlockEntity.getDrumItemStackHandler();

        ItemStack returnedItem = null;
        
        if(itemInHand.isEmpty()) {
            ItemStack toMatch = itemHandler.getDisplayStack();
            Inventory inventory = player.getInventory();
            int slot = 0;
            boolean attempting = false;
            
            for(int i = 0; i < inventory.items.size(); i++) {
                slot = i;
                if(inventory.getItem(i).getItem() == toMatch.getItem()) {
                    returnedItem = itemHandler.insertItem(itemHandler.getFirstStackableSlot(), inventory.getItem(i), false);
                    attempting = true;
                    break;
                }
            }
            
            if(attempting && !returnedItem.equals(inventory.getItem(slot))) {
                inventory.setItem(slot, returnedItem);
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
            }
        } else {
            returnedItem = itemHandler.insertItem(itemHandler.getFirstStackableSlot(), itemInHand, false);

            if(!returnedItem.equals(player.getItemInHand(hand), true)) {
                player.setItemInHand(hand, returnedItem);
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DrumBlockEntity(pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean exp) {
        DrumItemStackHandler itemStackHandler = ((DrumBlockEntity) level.getBlockEntity(pos)).getDrumItemStackHandler();

        for(int i = 0; i < 64; i++) {
            ItemStack slot = itemStackHandler.getStackInSlot(i);

            if(!slot.isEmpty()) {
                level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), slot));
            }
        }
        super.onRemove(state, level, pos, newState, exp);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter getter, List<Component> tags, TooltipFlag flag) {
        super.appendHoverText(stack, getter, tags, flag);

        tags.add(Component.translatable("text.compact_storage.drum.tooltip_1").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        tags.add(Component.translatable("text.compact_storage.drum.tooltip_2").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }
}
