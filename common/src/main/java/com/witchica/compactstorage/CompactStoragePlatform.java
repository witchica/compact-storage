package com.witchica.compactstorage;

import com.ibm.icu.impl.Assert;
import com.witchica.compactstorage.common.block.entity.CompactBarrelBlockEntity;
import com.witchica.compactstorage.common.block.entity.CompactChestBlockEntity;
import com.witchica.compactstorage.common.block.entity.DrumBlockEntity;
import com.witchica.compactstorage.common.inventory.BackpackInventoryHandlerFactory;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Arrays;
import java.util.Set;

public class CompactStoragePlatform {
    @ExpectPlatform
    public static BlockEntityType.BlockEntitySupplier<CompactChestBlockEntity> compactChestBlockEntitySupplier() {
        throw new AssertionError("Method not implemented on this platform.");
    }

    @ExpectPlatform
    public static BlockEntityType.BlockEntitySupplier<DrumBlockEntity> drumBlockEntitySupplier() {
        throw new AssertionError("Method not implemented on this platform.");
    }

    @ExpectPlatform
    public static BlockEntityType.BlockEntitySupplier<CompactBarrelBlockEntity> compactBarrelBlockEntitySupplier() {
        throw new AssertionError("Method compactBarrelBlockEntitySupplier not implemented on this platform.");
    }

    public static <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntityType.BlockEntitySupplier<T> supplier, RegistrySupplier<?>[] blocks) {
        return createBlockEntityType(supplier, Arrays.stream(blocks).map(s->(Block)s.get()).toArray(Block[]::new));
    }

    @ExpectPlatform
    public static <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntityType.BlockEntitySupplier<T> supplier, Block[] blocks) {
        throw new AssertionError("Method not implmeneted on this platform");
    }
}