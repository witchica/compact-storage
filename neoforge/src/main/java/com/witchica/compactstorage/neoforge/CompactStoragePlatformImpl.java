package com.witchica.compactstorage.neoforge;

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
        return CompactStorageNeoForge.COMPACT_CHEST_BLOCKS[index].get();
    }
    public static Block getCompactBarrelBlock(int index) {
        return CompactStorageNeoForge.COMPACT_BARREL_BLOCKS[index].get();
    }
    public static Block getDrumBlock(int index) {
        return CompactStorageNeoForge.DRUM_BLOCKS[index].get();
    }
    public static Item getBackpackItem(int index) {
        return CompactStorageNeoForge.BACKPACK_ITEMS[index].get();
    }
    public static Item getStorageRowUpgradeItem() {
        return CompactStorageNeoForge.UPGRADE_ROW_ITEM.get();
    }
    public static Item getStorageColumnUpgradeItem() {
        return CompactStorageNeoForge.UPGRADE_COLUMN_ITEM.get();
    }
    public static Block getCompactChestFromDyeColor(DyeColor dyeColor) {
        return CompactStorageNeoForge.DYE_COLOR_TO_COMPACT_CHEST_MAP.get(dyeColor).get();
    }
    public static Block getCompactBarrelFromDyeColor(DyeColor dyeColor) {
        return CompactStorageNeoForge.DYE_COLOR_TO_COMPACT_BARREL_MAP.get(dyeColor).get();
    }
    public static Item getBackpackFromDyeColor(DyeColor dyeColor) {
        return CompactStorageNeoForge.DYE_COLOR_TO_BACKPACK_MAP.get(dyeColor).get();
    }
    public static MenuType<?> getCompactStorageScreenHandler() {
        return CompactStorageNeoForge.COMPACT_CHEST_SCREEN_HANDLER.get();
    }
    public static BlockEntityType<?> getCompactChestBlockEntityType() {
        return CompactStorageNeoForge.COMPACT_CHEST_ENTITY_TYPE.get();
    }
    public static BlockEntityType<?> getCompactBarrelBlockEntityType() {
        return CompactStorageNeoForge.COMPACT_BARREL_ENTITY_TYPE.get();
    }

    public static BlockEntityType<?> getDrumBlockEntityType() {
        return CompactStorageNeoForge.DRUM_ENTITY_TYPE.get();
    }

    public static BackpackInventoryHandlerFactory getBackpackInventoryHandlerFactory(Player player, InteractionHand hand) {
        return new BackpackInventoryHandlerFactory(player, hand);
    }
    public static Item getStorageRetainerUpgradeItem() {
        return CompactStorageNeoForge.UPGRADE_RETAINER_ITEM.get();
    }
}
