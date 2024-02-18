package com.witchica.compactstorage.fabric.block.entity;

import com.witchica.compactstorage.common.block.entity.DrumBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class FabricDrumBlockEntity extends DrumBlockEntity {
    public final InventoryStorage inventoryWrapper = InventoryStorage.of(inventory, null);
    public FabricDrumBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }
}
