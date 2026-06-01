package com.witchica.compactstorage.fabric.datagen;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.block.base.BaseCompactStorageBlock;
import com.witchica.compactstorage.data.StorageType;
import com.witchica.compactstorage.mod.CompactStorageBlocks;
import com.witchica.compactstorage.mod.CompactStorageComponents;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLootTableProvider;
import net.minecraft.advancements.criterion.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.predicates.DataComponentPredicates;
import net.minecraft.core.component.predicates.EnchantmentsPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ModLootTableGenerator extends FabricBlockLootTableProvider {
    protected ModLootTableGenerator(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {

        for(StorageType storageType : StorageType.values()) {
            createStorageDrop(CompactStorageBlocks.compactChests.get(storageType).asBlock());
            createStorageDrop(CompactStorageBlocks.compactBarrels.get(storageType).asBlock());
            createItemDrumDrop(CompactStorageBlocks.itemDrums.get(storageType).asBlock());
        }
    }

    public void createStorageDrop(Block block) {
        add(block, LootTable.lootTable()
                .withPool((LootPool.Builder)this.applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(block)
                                .apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
                                        .include(DataComponents.CONTAINER).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BaseCompactStorageBlock.RETAINING, true))))
                                .apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY).include(DataComponents.CUSTOM_DATA).include(DataComponents.LOCK).include(CompactStorageComponents.RETAINING_DATA.value()).include(CompactStorageComponents.RESIZABLE_INVENTORY_DATA.value()))))));
    }

    public void createItemDrumDrop(Block block) {
        add(block, LootTable.lootTable()
                .withPool((LootPool.Builder)this.applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(block)
                                .apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
                                        .include(DataComponents.CONTAINER).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BaseCompactStorageBlock.RETAINING, true))))
                                .apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY).include(DataComponents.CUSTOM_DATA).include(DataComponents.LOCK).include(CompactStorageComponents.RETAINING_DATA.value()))))));
    }
}
