package com.witchica.compactstorage.common.item;

import com.witchica.compactstorage.CompactStoragePlatform;
import com.witchica.compactstorage.common.inventory.BackpackInventory;
import com.witchica.compactstorage.common.screen.CompactChestScreenHandler;
import com.witchica.compactstorage.common.util.CompactStorageUtil;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BackpackItem extends Item {
    public BackpackItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) {
            boolean isInOffhand = hand == InteractionHand.OFF_HAND;

            ItemStack heldItemStack = player.getItemInHand(hand);
            InteractionHand oppositeHand = (hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
            ItemStack oppositeItemStack = player.getItemInHand(oppositeHand);

            if(!heldItemStack.hasTag()) {
                heldItemStack.setTag(new CompoundTag());
            }

            if(!oppositeItemStack.isEmpty()) {
                Item oppositeItem = oppositeItemStack.getItem();
                BackpackInventory inventory = new BackpackInventory(heldItemStack.getTag().getCompound("Backpack"), player, isInOffhand);

                if(hand == InteractionHand.MAIN_HAND && oppositeItem instanceof BackpackItem) {
                    return super.use(world, player, hand);
                }

                if(oppositeItem == CompactStoragePlatform.getStorageRowUpgradeItem()) {
                    if(inventory.increaseSize(1, 0)) {
                        player.getItemInHand(oppositeHand).shrink(1);
                        heldItemStack.getTag().put("Backpack", inventory.toTag());

                        player.displayClientMessage(Component.translatable("text.compact_storage.upgrade_success").withStyle(ChatFormatting.GREEN), true);
                        player.playNotifySound(SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 1f, 1f);
                        return InteractionResultHolder.pass(heldItemStack);
                    } else {
                        player.playNotifySound(SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1f, 1f);
                        player.displayClientMessage(Component.translatable("text.compact_storage.upgrade_fail_maxsize").withStyle(ChatFormatting.RED), true);
                        return InteractionResultHolder.fail(heldItemStack);
                    }
                } else if(oppositeItem == CompactStoragePlatform.getStorageColumnUpgradeItem()) {
                    if(inventory.increaseSize(0, 1)) {
                        player.getItemInHand(oppositeHand).shrink(1);
                        heldItemStack.getTag().put("Backpack", inventory.toTag());

                        player.displayClientMessage(Component.translatable("text.compact_storage.upgrade_success").withStyle(ChatFormatting.GREEN), true);
                        player.playNotifySound(SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 1f, 1f);
                        return InteractionResultHolder.pass(heldItemStack);
                    } else {
                        player.playNotifySound(SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1f, 1f);
                        player.displayClientMessage(Component.translatable("text.compact_storage.upgrade_fail_maxsize").withStyle(ChatFormatting.RED), true);
                        return InteractionResultHolder.fail(heldItemStack);
                    }
                } else if(oppositeItem instanceof DyeItem dyeItem) {
                    Item newBackpackItem = CompactStoragePlatform.getBackpackFromDyeColor(dyeItem.getDyeColor());

                    if(newBackpackItem != heldItemStack.getItem()) {
                        ItemStack newStack = new ItemStack(newBackpackItem, 1);
                        newStack.setTag(heldItemStack.getTag());

                        player.playNotifySound(SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 1f, 1f);
                        player.getItemInHand(oppositeHand).shrink(1);
                        return InteractionResultHolder.pass(newStack);
                    }
                }
            }

            if(player.containerMenu instanceof CompactChestScreenHandler) {
                ((ServerPlayer) player).closeContainer();
                return super.use(world, player, hand);
            } else {
                openMenu(player, hand);
            }
        }
        return super.use(world, player, hand);
    }

    public abstract void openMenu(Player player, InteractionHand hand);

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        super.appendHoverText(stack, world, tooltip, context);
        CompactStorageUtil.appendTooltip(stack, world, tooltip, context, true);
    }
}
