package com.tabithastrong.compactstorage.item;

import com.tabithastrong.compactstorage.CompactStorage;
import com.tabithastrong.compactstorage.inventory.BackpackInventory;
import com.tabithastrong.compactstorage.inventory.BackpackInventoryHandlerFactory;
import com.tabithastrong.compactstorage.util.CompactStorageUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BackpackItem extends Item {
    public BackpackItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) {
            InteractionHand oppositeHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
            ItemStack oppositeHandStack = player.getItemInHand(oppositeHand);
            ItemStack heldItem = player.getItemInHand(hand);

            if(!heldItem.hasTag()) {
                CompactStorage.LOGGER.warn("no nbt");
                heldItem.setTag(new CompoundTag());
            }

            if(!oppositeHandStack.isEmpty()) {
                Item oppositeHandItem = oppositeHandStack.getItem();
                BackpackInventory inventory = new BackpackInventory(heldItem.getTag().getCompound("Backpack"), hand, player);

                if(oppositeHandItem == CompactStorage.UPGRADE_ROW_ITEM.get()) {
                    if(inventory.increaseSize(1, 0)) {
                        player.getItemInHand(oppositeHand).shrink(1);
                        heldItem.getTag().put("Backpack", inventory.toTag());

                        player.displayClientMessage(Component.translatable("text.compact_storage.upgrade_success").withStyle(ChatFormatting.GREEN), true);
                        player.playNotifySound(SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 1f, 1f);
                        return InteractionResultHolder.pass(heldItem);
                    } else {
                        player.playNotifySound(SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1f, 1f);
                        player.displayClientMessage(Component.translatable("text.compact_storage.upgrade_fail_maxsize").withStyle(ChatFormatting.RED), true);
                        return InteractionResultHolder.fail(heldItem);
                    }
                } else if(oppositeHandItem == CompactStorage.UPGRADE_COLUMN_ITEM.get()) {
                    if(inventory.increaseSize(0, 1)) {
                        player.getItemInHand(oppositeHand).shrink(1);
                        heldItem.getTag().put("Backpack", inventory.toTag());

                        player.displayClientMessage(Component.translatable("text.compact_storage.upgrade_success").withStyle(ChatFormatting.GREEN), true);
                        player.playNotifySound(SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 1f, 1f);
                        return InteractionResultHolder.pass(heldItem);
                    } else {
                        player.playNotifySound(SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1f, 1f);
                        player.displayClientMessage(Component.translatable("text.compact_storage.upgrade_fail_maxsize").withStyle(ChatFormatting.RED), true);
                        return InteractionResultHolder.fail(heldItem);
                    }
                } else if(oppositeHandItem instanceof DyeItem dyeItem) {
                    ItemStack newStack = new ItemStack(CompactStorage.DYE_COLOR_TO_BACKPACK_MAP.get(dyeItem.getDyeColor()).get(), 1);
                    newStack.setTag(heldItem.getTag());

                    player.playNotifySound(SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 1f, 1f);
                    player.getItemInHand(oppositeHand).shrink(1);
                    return InteractionResultHolder.pass(newStack);
                }
            }

            BackpackInventoryHandlerFactory backpackInventoryHandlerFactory = new BackpackInventoryHandlerFactory(player, hand);
            NetworkHooks.openScreen((ServerPlayer) player, backpackInventoryHandlerFactory, backpackInventoryHandlerFactory::writeScreenOpeningData);
        }
        return super.use(world, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        super.appendHoverText(stack, world, tooltip, context);
        CompactStorageUtil.appendTooltip(stack, world, tooltip, context, true);
    }
}
