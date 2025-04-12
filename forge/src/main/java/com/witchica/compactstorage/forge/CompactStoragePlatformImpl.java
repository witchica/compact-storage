package com.witchica.compactstorage.forge;

import com.witchica.compactstorage.common.block.entity.CompactBarrelBlockEntity;
import com.witchica.compactstorage.common.block.entity.CompactChestBlockEntity;
import com.witchica.compactstorage.common.block.entity.DrumBlockEntity;
import com.witchica.compactstorage.common.inventory.BackpackInventoryHandlerFactory;
import com.witchica.compactstorage.forge.block.entity.ForgeCompactBarrelBlockEntity;
import com.witchica.compactstorage.forge.block.entity.ForgeCompactChestBlockEntity;
import com.witchica.compactstorage.forge.block.entity.ForgeDrumBlockEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CompactStoragePlatformImpl {
    public static BlockEntityType.BlockEntitySupplier<CompactChestBlockEntity> compactChestBlockEntityProvider() {
       return ForgeCompactChestBlockEntity::new;
    }
    public static BlockEntityType.BlockEntitySupplier<CompactBarrelBlockEntity> compactBarrelBlockEntityProvider() {
        return ForgeCompactBarrelBlockEntity::new;
    }
    public static BlockEntityType.BlockEntitySupplier<DrumBlockEntity> drumBlockEntityProvider() {
        return ForgeDrumBlockEntity::new;
    }
}
