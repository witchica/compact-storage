package com.tabithastrong.compactstorage.util;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;

public class CompactStorageUtil {

    public static void appendTooltip(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag options, boolean isBackpack) {
        int inventoryX = 9;
        int inventoryY = 6;

        CompoundTag compound = stack.getTag();

        if(isBackpack && compound != null) {
            compound = compound.getCompound("Backpack");
        }

        if(compound != null && compound.contains("inventory_width")) {
            inventoryX = compound.getInt("inventory_width");
            inventoryY = compound.getInt("inventory_height");
        }

        int slots = inventoryX * inventoryY;

        tooltip.add(new TranslatableComponent("text.compact_storage.tooltip.size_x").withStyle(ChatFormatting.WHITE).append(new TextComponent("" + inventoryX).withStyle(ChatFormatting.DARK_PURPLE)));
        tooltip.add(new TranslatableComponent("text.compact_storage.tooltip.size_y").withStyle(ChatFormatting.WHITE).append(new TextComponent("" + inventoryY).withStyle(ChatFormatting.DARK_PURPLE)));
        tooltip.add(new TranslatableComponent("text.compact_storage.tooltip.slots", slots).withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));
    }
}
