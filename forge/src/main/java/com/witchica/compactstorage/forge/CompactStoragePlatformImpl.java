package com.witchica.compactstorage.forge;

import com.witchica.compactstorage.common.inventory.BackpackInventoryHandlerFactory;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CompactStoragePlatformImpl {
    public static Block getCompactChestBlock(int index) {
        return CompactStorageForge.COMPACT_CHEST_BLOCKS[index].get();
    }
    public static Block getCompactBarrelBlock(int index) {
        return CompactStorageForge.COMPACT_BARREL_BLOCKS[index].get();
    }
    public static Block getDrumBlock(int index) {
        return CompactStorageForge.DRUM_BLOCKS[index].get();
    }
    public static Item getBackpackItem(int index) {
        return CompactStorageForge.BACKPACK_ITEMS[index].get();
    }
    public static Item getStorageRowUpgradeItem() {
        return CompactStorageForge.UPGRADE_ROW_ITEM.get();
    }
    public static Item getStorageColumnUpgradeItem() {
        return CompactStorageForge.UPGRADE_COLUMN_ITEM.get();
    }
    public static Block getCompactChestFromDyeColor(DyeColor dyeColor) {
        return CompactStorageForge.DYE_COLOR_TO_COMPACT_CHEST_MAP.get(dyeColor).get();
    }
    public static Block getCompactBarrelFromDyeColor(DyeColor dyeColor) {
        return CompactStorageForge.DYE_COLOR_TO_COMPACT_BARREL_MAP.get(dyeColor).get();
    }
    public static Item getBackpackFromDyeColor(DyeColor dyeColor) {
        return CompactStorageForge.DYE_COLOR_TO_BACKPACK_MAP.get(dyeColor).get();
    }
    public static MenuType<?> getCompactStorageScreenHandler() {
        return CompactStorageForge.COMPACT_CHEST_SCREEN_HANDLER.get();
    }
    public static BlockEntityType<?> getCompactChestBlockEntityType() {
        return CompactStorageForge.COMPACT_CHEST_ENTITY_TYPE.get();
    }
    public static BlockEntityType<?> getCompactBarrelBlockEntityType() {
        return CompactStorageForge.COMPACT_BARREL_ENTITY_TYPE.get();
    }

    public static BlockEntityType<?> getDrumBlockEntityType() {
        return CompactStorageForge.DRUM_ENTITY_TYPE.get();
    }

    public static BackpackInventoryHandlerFactory getBackpackInventoryHandlerFactory(Player player, InteractionHand hand) {
        return new BackpackInventoryHandlerFactory(player, hand);
    }
    public static Item getStorageRetainerUpgradeItem() {
        return CompactStorageForge.UPGRADE_RETAINER_ITEM.get();
    }
}
