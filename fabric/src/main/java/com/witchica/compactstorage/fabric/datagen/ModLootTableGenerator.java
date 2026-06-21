package com.witchica.compactstorage.fabric.datagen;

import com.witchica.compactstorage.data.StorageType;
import com.witchica.compactstorage.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.concurrent.CompletableFuture;

public class ModLootTableGenerator extends FabricBlockLootSubProvider {

    protected ModLootTableGenerator(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture);
    }

    @Override
    public void generate() {

        for(StorageType storageType : StorageType.values()) {
            createStorageDrop(ModBlocks.compactChests.get(storageType).asBlock());
            createStorageDrop(ModBlocks.compactBarrels.get(storageType).asBlock());
            createStorageDrop(ModBlocks.itemDrums.get(storageType).asBlock());
        }
    }

    public void createStorageDrop(Block block) {
        add(block, LootTable.lootTable()
                .withPool((LootPool.Builder)this.applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(block)
                                .apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY))))));
    }
}
