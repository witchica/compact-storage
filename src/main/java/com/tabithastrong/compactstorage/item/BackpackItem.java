package com.tabithastrong.compactstorage.item;

import com.tabithastrong.compactstorage.CompactStorage;
import com.tabithastrong.compactstorage.inventory.BackpackInventory;
import com.tabithastrong.compactstorage.inventory.BackpackInventoryHandlerFactory;
import com.tabithastrong.compactstorage.screen.CompactChestScreenHandler;
import com.tabithastrong.compactstorage.util.CompactStorageUtil;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
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

            Hand oppositeHand = hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
            ItemStack oppositeHandStack = player.getStackInHand(oppositeHand);
            ItemStack heldItem = player.getStackInHand(hand);

            if(!heldItem.hasNbt()) {
                heldItem.setNbt(new NbtCompound());
            }

            if(!oppositeHandStack.isEmpty()) {
                Item oppositeHandItem = oppositeHandStack.getItem();

                if(hand == Hand.MAIN_HAND && oppositeHandItem instanceof BackpackItem) {
                    return super.use(world, player, hand);
                }
                BackpackInventory inventory = new BackpackInventory(heldItem.getNbt().getCompound("Backpack"), player, isInOffhand);

                if(oppositeHandItem == CompactStorage.UPGRADE_ROW_ITEM) {
                    if(inventory.increaseSize(1, 0)) {
                        player.getStackInHand(oppositeHand).decrement(1);
                        heldItem.getNbt().put("Backpack", inventory.toTag());

                        player.sendMessage(new TranslatableText("text.compact_storage.upgrade_success").formatted(Formatting.GREEN), true);
                        player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1f, 1f);
                        return TypedActionResult.pass(heldItem);
                    } else {
                        player.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1f, 1f);
                        player.sendMessage(new TranslatableText("text.compact_storage.upgrade_fail_maxsize").formatted(Formatting.RED), true);
                        return TypedActionResult.fail(heldItem);
                    }
                } else if(oppositeHandItem == CompactStorage.UPGRADE_COLUMN_ITEM) {
                    if(inventory.increaseSize(0, 1)) {
                        player.getStackInHand(oppositeHand).decrement(1);
                        heldItem.getNbt().put("Backpack", inventory.toTag());

                        player.sendMessage(new TranslatableText("text.compact_storage.upgrade_success").formatted(Formatting.GREEN), true);
                        player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1f, 1f);
                        return TypedActionResult.pass(heldItem);
                    } else {
                        player.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1f, 1f);
                        player.sendMessage(new TranslatableText("text.compact_storage.upgrade_fail_maxsize").formatted(Formatting.RED), true);
                        return TypedActionResult.fail(heldItem);
                    }
                } else if(oppositeHandItem instanceof DyeItem dyeItem) {
                    Item newBackpackItem = CompactStorage.DYE_COLOR_TO_BACKPACK_MAP.get(dyeItem.getColor());

                    if(newBackpackItem != heldItem.getItem()) {
                        ItemStack newStack = new ItemStack(CompactStorage.DYE_COLOR_TO_BACKPACK_MAP.get(dyeItem.getColor()), 1);
                        newStack.setNbt(heldItem.getNbt());

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
