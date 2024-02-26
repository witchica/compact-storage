package com.witchica.compactstorage.fabric;

import com.witchica.compactstorage.CompactStoragePlatform;
import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.common.inventory.BackpackInventoryHandlerFactory;
import com.witchica.compactstorage.inventory.FabricBackpackInventoryFactory;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CompactStoragePlatformImpl {

    public static Block getCompactChestBlock(int index) {
        return CompactStorage.COMPACT_CHEST_BLOCKS[index].get();
    }
    public static Block getCompactBarrelBlock(int index) {
        return CompactStorage.COMPACT_BARREL_BLOCKS[index].get();
    }
    public static Block getDrumBlock(int index) {
        return CompactStorage.DRUM_BLOCKS[index].get();
    }
    public static Item getBackpackItem(int index) {
        return CompactStorage.BACKPACK_ITEMS[index].get();
    }
    public static Item getStorageRowUpgradeItem() {
        return CompactStorage.UPGRADE_ROW_ITEM.get();
    }
    public static Item getStorageColumnUpgradeItem() {
        return CompactStorage.UPGRADE_COLUMN_ITEM.get();
    }
    public static Block getCompactChestFromDyeColor(DyeColor dyeColor) {
        return CompactStorage.DYE_COLOR_TO_COMPACT_CHEST_MAP.get(dyeColor).get();
    }
    public static Block getCompactBarrelFromDyeColor(DyeColor dyeColor) {
        return CompactStorage.DYE_COLOR_TO_COMPACT_BARREL_MAP.get(dyeColor).get();
    }
    public static Item getBackpackFromDyeColor(DyeColor dyeColor) {
        return CompactStorage.DYE_COLOR_TO_BACKPACK_MAP.get(dyeColor).get();
    }
    public static MenuType<?> getCompactStorageScreenHandler() {
        return CompactStorage.COMPACT_CHEST_SCREEN_HANDLER;
    }
    public static BlockEntityType<?> getCompactChestBlockEntityType() {
        return CompactStorage.COMPACT_CHEST_ENTITY_TYPE;
    }
    public static BlockEntityType<?> getCompactBarrelBlockEntityType() {
        return CompactStorage.COMPACT_BARREL_ENTITY_TYPE;
    }

    public static BlockEntityType<?> getDrumBlockEntityType() {
        return CompactStorage.DRUM_BLOCK_ENTITY_TYPE.get();
    }

    public static BackpackInventoryHandlerFactory getBackpackInventoryHandlerFactory(Player player, InteractionHand hand) {
        return new FabricBackpackInventoryFactory(player, hand);
    }

    public static Item getStorageRetainerUpgradeItem() {
        return CompactStorage.UPGRADE_RETAINER_ITEM.get();
    }
}
