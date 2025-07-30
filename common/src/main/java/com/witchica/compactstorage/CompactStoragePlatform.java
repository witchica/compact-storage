package com.witchica.compactstorage;

import com.mojang.datafixers.FunctionType;
import com.witchica.compactstorage.common.block.entity.CompactBarrelBlockEntity;
import com.witchica.compactstorage.common.block.entity.CompactChestBlockEntity;
import com.witchica.compactstorage.common.block.entity.DrumBlockEntity;
import com.witchica.compactstorage.common.inventory.BackpackInventoryHandlerFactory;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Optional;
import java.util.function.Supplier;

public class CompactStoragePlatform {
    @ExpectPlatform
    public static BlockEntityType.BlockEntitySupplier<CompactChestBlockEntity> compactChestBlockEntityProvider() {
        throw new AssertionError("Method not implemented on this platform.");
    }
    @ExpectPlatform
    public static BlockEntityType.BlockEntitySupplier<CompactBarrelBlockEntity> compactBarrelBlockEntityProvider() {
        throw new AssertionError("Method not implemented on this platform.");
    }
    @ExpectPlatform
    public static BlockEntityType.BlockEntitySupplier<DrumBlockEntity> drumBlockEntityProvider() {
        throw new AssertionError("Method not implemented on this platform.");
    }
    @ExpectPlatform
    public static Optional<ItemStack> getAdditionalSlotBackpack(Player player) {
        throw new AssertionError("Method not implemented on this platform.");
    }
}
