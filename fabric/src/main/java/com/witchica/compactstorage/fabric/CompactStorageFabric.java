package com.witchica.compactstorage.fabric;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.fabric.block.entity.FabricCompactBarrelBlockEntity;
import com.witchica.compactstorage.fabric.block.entity.FabricCompactChestBlockEntity;
import com.witchica.compactstorage.fabric.block.entity.FabricDrumBlockEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;

public class CompactStorageFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		CompactStorage.onInitialize();

		ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> {
			return ((FabricCompactChestBlockEntity) blockEntity).inventoryStorage;
		}, CompactStorage.COMPACT_CHEST_ENTITY_TYPE.get());

		ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> {
			return ((FabricCompactBarrelBlockEntity) blockEntity).inventoryStorage;
		}, CompactStorage.COMPACT_BARREL_ENTITY_TYPE.get());

		ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> {
			return ((FabricDrumBlockEntity) blockEntity).inventoryWrapper;
		}, CompactStorage.DRUM_ENTITY_TYPE.get());
	}
}
