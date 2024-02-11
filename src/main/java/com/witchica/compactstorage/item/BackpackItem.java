package com.witchica.compactstorage.item;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.inventory.BackpackInventory;
import com.witchica.compactstorage.inventory.BackpackInventoryHandlerFactory;
import com.witchica.compactstorage.screen.CompactChestScreenHandler;
import com.witchica.compactstorage.util.CompactStorageUtil;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BackpackItem extends Item {
    public BackpackItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient) {
            boolean isInOffhand = hand == Hand.OFF_HAND;

            ItemStack heldItemStack = player.getStackInHand(hand);
            Hand oppositeHand = (hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND);
            ItemStack oppositeItemStack = player.getStackInHand(oppositeHand);

            if(!heldItemStack.hasNbt()) {
                heldItemStack.setNbt(new NbtCompound());
            }

            if(!oppositeItemStack.isEmpty()) {
                Item oppositeItem = oppositeItemStack.getItem();
                BackpackInventory inventory = new BackpackInventory(heldItemStack.getNbt().getCompound("Backpack"), player, isInOffhand);

                if(hand == Hand.MAIN_HAND && oppositeItem instanceof BackpackItem) {
                    return super.use(world, player, hand);
                }

                if(oppositeItem == CompactStorage.UPGRADE_ROW_ITEM.get()) {
                    if(inventory.increaseSize(1, 0)) {
                        player.getStackInHand(oppositeHand).decrement(1);
                        heldItemStack.getNbt().put("Backpack", inventory.toTag());

                        player.sendMessage(Text.translatable("text.compact_storage.upgrade_success").formatted(Formatting.GREEN), true);
                        player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1f, 1f);
                        return TypedActionResult.pass(heldItemStack);
                    } else {
                        player.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1f, 1f);
                        player.sendMessage(Text.translatable("text.compact_storage.upgrade_fail_maxsize").formatted(Formatting.RED), true);
                        return TypedActionResult.fail(heldItemStack);
                    }
                } else if(oppositeItem == CompactStorage.UPGRADE_COLUMN_ITEM.get()) {
                    if(inventory.increaseSize(0, 1)) {
                        player.getStackInHand(oppositeHand).decrement(1);
                        heldItemStack.getNbt().put("Backpack", inventory.toTag());

                        player.sendMessage(Text.translatable("text.compact_storage.upgrade_success").formatted(Formatting.GREEN), true);
                        player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1f, 1f);
                        return TypedActionResult.pass(heldItemStack);
                    } else {
                        player.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1f, 1f);
                        player.sendMessage(Text.translatable("text.compact_storage.upgrade_fail_maxsize").formatted(Formatting.RED), true);
                        return TypedActionResult.fail(heldItemStack);
                    }
                } else if(oppositeItem instanceof DyeItem dyeItem) {
                    Item newBackpackItem = CompactStorage.DYE_COLOR_TO_BACKPACK_MAP.get(dyeItem.getColor()).get();

                    if(newBackpackItem != heldItemStack.getItem()) {
                        ItemStack newStack = new ItemStack(newBackpackItem, 1);
                        newStack.setNbt(heldItemStack.getNbt());

                        player.playSound(SoundEvents.BLOCK_SLIME_BLOCK_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                        player.getStackInHand(oppositeHand).decrement(1);
                        return TypedActionResult.pass(newStack);
                    }
                }
            }

            if(player.currentScreenHandler instanceof CompactChestScreenHandler) {
                ((ServerPlayerEntity) player).closeHandledScreen();
                return super.use(world, player, hand);
            } else {
                player.openHandledScreen(new BackpackInventoryHandlerFactory(player, hand));
            }
        }
        return super.use(world, player, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        CompactStorageUtil.appendTooltip(stack, world, tooltip, context, true);
    }
}
