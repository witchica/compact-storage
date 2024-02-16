package com.witchica.compactstorage.util;

import com.witchica.compactstorage.block.entity.CompactBarrelBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;

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

        tooltip.add(Component.translatable("text.compact_storage.tooltip.size_x").withStyle(ChatFormatting.WHITE).append(Component.literal("" + inventoryX).withStyle(ChatFormatting.DARK_PURPLE)));
        tooltip.add(Component.translatable("text.compact_storage.tooltip.size_y").withStyle(ChatFormatting.WHITE).append(Component.literal("" + inventoryY).withStyle(ChatFormatting.DARK_PURPLE)));
        tooltip.add(Component.translatable("text.compact_storage.tooltip.slots", slots).withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));
    }

    public static void dropContents(Level world, BlockPos pos, Block block, Player player) {
        if(world.isClientSide) {
            return;
        }

        BlockEntity blockEntity = world.getBlockEntity(pos);

        if(blockEntity instanceof CompactStorageInventoryImpl inventory) {
            ItemStack chestStack = new ItemStack(block, 1);

            CompoundTag chestTag = new CompoundTag();
            chestTag.putInt("inventory_width", inventory.getInventoryWidth());
            chestTag.putInt("inventory_height", inventory.getInventoryHeight());

            if(inventory.getInventoryWidth() != 9 || inventory.getInventoryHeight() != 6) {
                chestStack.setTag(chestTag);
            }


            if(inventory instanceof RandomizableContainerBlockEntity lootableContainerBlockEntity) {
                if(lootableContainerBlockEntity.hasCustomName()) {
                    chestStack.setHoverName(lootableContainerBlockEntity.getCustomName());
                }
            }

            Containers.dropContents(world, pos, (Container) inventory);

            if(player == null || !player.isCreative()) {
                Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), chestStack);
            }

            world.updateNeighbourForOutputSignal(pos, block);
        }
    }
}
