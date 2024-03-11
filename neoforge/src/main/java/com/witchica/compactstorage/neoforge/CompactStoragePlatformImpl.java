package com.witchica.compactstorage.neoforge;

import com.witchica.compactstorage.common.block.entity.CompactBarrelBlockEntity;
import com.witchica.compactstorage.common.block.entity.CompactChestBlockEntity;
import com.witchica.compactstorage.common.block.entity.DrumBlockEntity;
import com.witchica.compactstorage.common.inventory.BackpackInventoryHandlerFactory;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CompactStoragePlatformImpl {
    public static BlockEntityType.BlockEntitySupplier<CompactChestBlockEntity> compactChestBlockEntitySupplier() {
        return CompactChestBlockEntity::new;
    }

    public static BlockEntityType.BlockEntitySupplier<DrumBlockEntity> drumBlockEntitySupplier() {
        return DrumBlockEntity::new;
    }

    public static BlockEntityType.BlockEntitySupplier<CompactBarrelBlockEntity> compactBarrelBlockEntitySupplier() {
        return CompactBarrelBlockEntity::new;
    }
}
