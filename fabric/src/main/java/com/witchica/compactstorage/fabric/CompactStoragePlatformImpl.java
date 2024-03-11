package com.witchica.compactstorage.fabric;

import com.witchica.compactstorage.common.block.entity.CompactBarrelBlockEntity;
import com.witchica.compactstorage.common.block.entity.CompactChestBlockEntity;
import com.witchica.compactstorage.common.block.entity.DrumBlockEntity;
import com.witchica.compactstorage.fabric.block.entity.FabricCompactBarrelBlockEntity;
import com.witchica.compactstorage.fabric.block.entity.FabricCompactChestBlockEntity;
import com.witchica.compactstorage.fabric.block.entity.FabricDrumBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CompactStoragePlatformImpl {

    public static BlockEntityType.BlockEntitySupplier<CompactChestBlockEntity> compactChestBlockEntitySupplier() {
        return FabricCompactChestBlockEntity::new;
    }

    public static BlockEntityType.BlockEntitySupplier<DrumBlockEntity> drumBlockEntitySupplier() {
        return FabricDrumBlockEntity::new;
    }

    public static BlockEntityType.BlockEntitySupplier<CompactBarrelBlockEntity> compactBarrelBlockEntitySupplier() {
        return FabricCompactBarrelBlockEntity::new;
    }
}
