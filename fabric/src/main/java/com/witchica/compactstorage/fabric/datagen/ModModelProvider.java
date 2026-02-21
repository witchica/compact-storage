package com.witchica.compactstorage.fabric.datagen;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.block.CompactBarrelBlock;
import com.witchica.compactstorage.fabric.datagen.providers.BarrelModelProvider;
import com.witchica.compactstorage.mod.CompactStorageItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import com.witchica.compactstorage.mod.CompactStorageBlocks;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import com.witchica.compactstorage.data.StorageType;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        for(StorageType type : StorageType.values()) {
            blockStateModelGenerator.createChest(CompactStorageBlocks.compactChests.get(type).asBlock(), type.getRecipeData().particle(), Identifier.fromNamespaceAndPath("compact_storage", type.getName() +"_chest"), false);
            BarrelModelProvider.registerCompactBarrel(blockStateModelGenerator,
                    CompactStorageBlocks.compactBarrels.get(type).asBlock(),
                    BarrelModelProvider.compactBarrelTextureMapping(Identifier.fromNamespaceAndPath("compact_storage", "block/barrel/" + type.getName() + "_barrel"), type.getRecipeData().particle()));

            BarrelModelProvider.registerItemDrum(blockStateModelGenerator,
                    CompactStorageBlocks.itemDrums.get(type).asBlock(),
                    BarrelModelProvider.itemDrumTextureMapping(Identifier.fromNamespaceAndPath("compact_storage", "block/drum/" + type.getName() + "_drum"), type.getRecipeData().particle())
                    );
        }

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(CompactStorageItems.UPGRADE_WIDTH.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CompactStorageItems.UPGRADE_HEIGHT.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CompactStorageItems.UPGRADE_RETAINING.asItem(), ModelTemplates.FLAT_ITEM);
    }
}
