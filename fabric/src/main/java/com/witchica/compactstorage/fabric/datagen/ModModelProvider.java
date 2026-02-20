package com.witchica.compactstorage.fabric.datagen;

import com.witchica.compactstorage.item.CompactStorageItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import com.witchica.compactstorage.block.CompactStorageBlocks;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.resources.Identifier;
import com.witchica.compactstorage.data.StorageType;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        for(StorageType type : StorageType.values()) {
            blockStateModelGenerator.createChest(CompactStorageBlocks.compactChests.get(type).asBlock(), type.getRecipeData().particle(), Identifier.fromNamespaceAndPath("compact_storage", type.getName() +"_chest"), false);
        }

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(CompactStorageItems.UPGRADE_WIDTH.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CompactStorageItems.UPGRADE_HEIGHT.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CompactStorageItems.UPGRADE_RETAINING.asItem(), ModelTemplates.FLAT_ITEM);
    }

}
