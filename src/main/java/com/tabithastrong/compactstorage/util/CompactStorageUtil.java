package com.tabithastrong.compactstorage.util;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CompactStorageUtil {

    public static void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options, boolean isBackpack) {
        int inventoryX = 9;
        int inventoryY = 6;

        NbtCompound compound = stack.getNbt();

        if(isBackpack && compound != null) {
            compound = compound.getCompound("Backpack");
        }

        if(compound != null && compound.contains("inventory_width")) {
            inventoryX = compound.getInt("inventory_width");
            inventoryY = compound.getInt("inventory_height");
        }

        int slots = inventoryX * inventoryY;

        tooltip.add(Text.translatable("text.compact_storage.tooltip.size_x").formatted(Formatting.WHITE).append(Text.literal("" + inventoryX).formatted(Formatting.DARK_PURPLE)));
        tooltip.add(Text.translatable("text.compact_storage.tooltip.size_y").formatted(Formatting.WHITE).append(Text.literal("" + inventoryY).formatted(Formatting.DARK_PURPLE)));
        tooltip.add(Text.translatable("text.compact_storage.tooltip.slots", slots).formatted(Formatting.DARK_PURPLE, Formatting.ITALIC));
    }
}
