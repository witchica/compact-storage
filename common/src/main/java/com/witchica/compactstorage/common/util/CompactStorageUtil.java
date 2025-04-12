package com.witchica.compactstorage.common.util;

import net.minecraft.world.item.DyeColor;
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
import org.joml.Vector2i;

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
         "warped",
         "bamboo"
    };

    public enum StorageVisualTypes {
        ACACIA("acacia", true, new Vector2i(1, 0), new Vector2i(1,0)),
        BIRCH("birch", true, new Vector2i(2, 0), new Vector2i(2,0)),
        CHERRY("cherry", true, new Vector2i(3, 0), new Vector2i(3,0)),
        CRIMSON("crimson", true, new Vector2i(4, 0), new Vector2i(4,0)),
        DARK_OAK("dark_oak", true, new Vector2i(5, 0), new Vector2i(5,0)),
        JUNGLE("jungle", true, new Vector2i(6, 0), new Vector2i(6,0)),
        MANGROVE("mangrove", true, new Vector2i(0, 1), new Vector2i(7,0)),
        OAK("oak", true, new Vector2i(1, 1), new Vector2i(0,1)),
        SPRUCE("spruce", true, new Vector2i(2, 1), new Vector2i(1,1)),
        WARPED("warped", true, new Vector2i(3, 1), new Vector2i(2,1)),
        BAMBOO("bamboo", true, new Vector2i(4, 1), new Vector2i(3,1)),
        WHITE("white", false, DyeColor.WHITE, new Vector2i(5, 3), new Vector2i(2,3)),
        ORANGE("orange", false, DyeColor.ORANGE, new Vector2i(1, 3), new Vector2i(6,2)),
        MAGENTA("magenta", false, DyeColor.MAGENTA, new Vector2i(0, 3), new Vector2i(5,2)),
        LIGHT_BLUE("light_blue", false, DyeColor.LIGHT_BLUE, new Vector2i(4, 2), new Vector2i(2,2)),
        YELLOW("yellow", false, DyeColor.YELLOW, new Vector2i(6, 3), new Vector2i(3,3)),
        LIME("lime", false, DyeColor.LIME, new Vector2i(6, 2), new Vector2i(4,2)),
        PINK("pink", false, DyeColor.PINK, new Vector2i(2, 3), new Vector2i(7,2)),
        GRAY("gray", false, DyeColor.GRAY, new Vector2i(2, 2), new Vector2i(0,2)),
        LIGHT_GRAY("light_gray", false, DyeColor.LIGHT_GRAY, new Vector2i(5, 2), new Vector2i(3,2)),
        CYAN("cyan", false, DyeColor.CYAN, new Vector2i(1, 2), new Vector2i(7,1)),
        PURPLE("purple", false, DyeColor.PURPLE, new Vector2i(3, 3), new Vector2i(0,3)),
        BLUE("blue", false, DyeColor.BLUE, new Vector2i(6, 1), new Vector2i(5,1)),
        BROWN("brown", false, DyeColor.BROWN, new Vector2i(0, 2), new Vector2i(6,1)),
        GREEN("green", false, DyeColor.GREEN, new Vector2i(3, 2), new Vector2i(1,2)),
        RED("red", false, DyeColor.RED, new Vector2i(4, 3), new Vector2i(1,3)),
        BLACK("black", false, DyeColor.BLACK, new Vector2i(5, 1), new Vector2i(4,1));

        final Vector2i slotOffset;
        final Vector2i backgroundOffset;
        final String type;
        final DyeColor associatedDye;
        final boolean wooden;

        StorageVisualTypes(String type, boolean wooden, DyeColor associatedDye, Vector2i slotOffset, Vector2i backgroundOffset) {
            this.type = type;
            this.wooden = wooden;
            this.associatedDye = associatedDye;
            this.slotOffset = slotOffset;
            this.backgroundOffset = backgroundOffset;
        }

        StorageVisualTypes(String name, boolean wooden, Vector2i slotOffset, Vector2i backgroundOffset) {
            this(name, wooden, null, slotOffset, backgroundOffset);
        }

        public String getType() {
            return this.type;
        }

        public boolean isWooden() {
            return wooden;
        }

        public DyeColor getAssociatedDyeColor() {
            return associatedDye;
        }

        public Vector2i getSlotOffset() {
            return slotOffset;
        }

        public Vector2i getBackgroundOffset() {
            return backgroundOffset;
        }
    }

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

        if(compound != null && compound.contains("retaining") && compound.getBoolean("retaining")) {
            tooltip.add(Component.translatable("tooltip.compact_storage.retaining").withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD));
        }
    }

    public static void dropContents(Level world, BlockPos pos, Block block, Player player) {
        if(world.isClientSide) {
            return;
        }

        BlockEntity blockEntity = world.getBlockEntity(pos);

        if(blockEntity instanceof CompactStorageInventoryImpl inventory) {
            ItemStack chestStack = new ItemStack(block, 1);
            blockEntity.saveToItem(chestStack);

            if(inventory instanceof RandomizableContainerBlockEntity lootableContainerBlockEntity) {
                if(lootableContainerBlockEntity.hasCustomName()) {
                    chestStack.setHoverName(lootableContainerBlockEntity.getCustomName());
                }
            }

            if(!inventory.getRetaining()) {
                Containers.dropContents(world, pos, (Container) inventory);
            }

            if(player == null || !player.isCreative() || inventory.getRetaining()) {
                Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), chestStack);
            }

            world.updateNeighbourForOutputSignal(pos, block);
        }
    }
}
