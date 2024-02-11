package com.witchica.compactstorage.util;

import com.witchica.compactstorage.block.entity.CompactBarrelBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CompactStorageUtil {

    public static final String[] DRUM_TYPES = new String[] {
         "acacia",
         "birch",
         "cherry",
         "crimson",
         "dark_oak",
         "jungle",
         "mangrove",
         "oak",
         "spruce",
         "warped"
    };

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

    public static void dropContents(World world, BlockPos pos, Block block, PlayerEntity player) {
        if(world.isClient) {
            return;
        }

        BlockEntity blockEntity = world.getBlockEntity(pos);

        if(blockEntity instanceof CompactStorageInventoryImpl inventory) {
            ItemStack chestStack = new ItemStack(block, 1);

            NbtCompound chestTag = new NbtCompound();
            chestTag.putInt("inventory_width", inventory.getInventoryWidth());
            chestTag.putInt("inventory_height", inventory.getInventoryHeight());

            if(inventory.getInventoryWidth() != 9 || inventory.getInventoryHeight() != 6) {
                chestStack.setNbt(chestTag);
            }


            if(inventory instanceof LootableContainerBlockEntity lootableContainerBlockEntity) {
                if(lootableContainerBlockEntity.hasCustomName()) {
                    chestStack.setCustomName(lootableContainerBlockEntity.getCustomName());
                }
            }

            ItemScatterer.spawn(world, pos, (Inventory) inventory);

            if(player == null || !player.isCreative()) {
                ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), chestStack);
            }

            world.updateComparators(pos, block);
        }
    }
}
