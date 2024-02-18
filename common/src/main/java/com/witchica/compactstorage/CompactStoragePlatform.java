package com.witchica.compactstorage;

import com.witchica.compactstorage.common.inventory.BackpackInventoryHandlerFactory;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CompactStoragePlatform {
    @ExpectPlatform
    public static Block getCompactChestBlock(int index) {
        throw new AssertionError("Method not implemented on this platform.");
    }
    @ExpectPlatform
    public static Block getCompactBarrelBlock(int index) {
        throw new AssertionError("Method not implemented on this platform.");
    }
    @ExpectPlatform
    public static Block getDrumBlock(int index) {
        throw new AssertionError("Method not implemented on this platform.");
    }
    @ExpectPlatform
    public static Item getBackpackItem(int index) {
        throw new AssertionError("Method not implemented on this platform.");
    }
    @ExpectPlatform
    public static Item getStorageRowUpgradeItem() {
        throw new AssertionError("Method not implemented on this platform.");
    }
    @ExpectPlatform
    public static Item getStorageColumnUpgradeItem() {
        throw new AssertionError("Method not implemented on this platform.");
    }
    @ExpectPlatform
    public static Block getCompactChestFromDyeColor(DyeColor dyeColor) {
        throw new AssertionError("Method not implemented on this platform.");
    }
    @ExpectPlatform
    public static Block getCompactBarrelFromDyeColor(DyeColor dyeColor) {
        throw new AssertionError("Method not implemented on this platform.");
    }
    @ExpectPlatform
    public static Item getBackpackFromDyeColor(DyeColor dyeColor) {
        throw new AssertionError("Method not implemented on this platform.");
    }
    @ExpectPlatform
    public static MenuType<?> getCompactStorageScreenHandler() {
        throw new AssertionError("Method not implemented on this platform.");
    }
    @ExpectPlatform
    public static BlockEntityType<?> getCompactChestBlockEntityType() {
        throw new AssertionError("Method not implemented on this platform.");
    }
    @ExpectPlatform
    public static BlockEntityType<?> getCompactBarrelBlockEntityType() {
        throw new AssertionError("Method not implemented on this platform.");
    }

    @ExpectPlatform
    public static BlockEntityType<?> getDrumBlockEntityType() {
        throw new AssertionError("Method not implemented on this platform.");
    }

    @ExpectPlatform
    public static BackpackInventoryHandlerFactory getBackpackInventoryHandlerFactory(Player player, InteractionHand hand) {
        throw new AssertionError("Method not implemented on this platform.");
    }
}
