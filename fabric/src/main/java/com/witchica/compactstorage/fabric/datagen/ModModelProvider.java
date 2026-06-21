package com.witchica.compactstorage.fabric.datagen;

import com.witchica.compactstorage.fabric.datagen.providers.BackpackPackFrameModelProvider;
import com.witchica.compactstorage.fabric.datagen.providers.BarrelModelProvider;
import com.witchica.compactstorage.item.ModItems;
import com.witchica.compactstorage.upgrades.ModUpgrades;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import com.witchica.compactstorage.block.ModBlocks;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import com.witchica.compactstorage.data.StorageType;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        for(StorageType type : StorageType.values()) {
            blockStateModelGenerator.createChest(ModBlocks.compactChests.get(type).asBlock(), type.getRecipeData().particle(), Identifier.fromNamespaceAndPath("compact_storage", type.getName() +"_chest"), false);
            BarrelModelProvider.registerCompactBarrel(blockStateModelGenerator,
                    ModBlocks.compactBarrels.get(type).asBlock(),
                    BarrelModelProvider.compactBarrelTextureMapping(new Material(Identifier.fromNamespaceAndPath("compact_storage", "block/barrel/" + type.getName() + "_barrel")), new Material(ModelLocationUtils.getModelLocation(type.getRecipeData().particle()))));

            BarrelModelProvider.registerItemDrum(blockStateModelGenerator,
                    ModBlocks.itemDrums.get(type).asBlock(),
                    BarrelModelProvider.itemDrumTextureMapping(new Material(Identifier.fromNamespaceAndPath("compact_storage", "block/drum/" + type.getName() + "_drum")), new Material(ModelLocationUtils.getModelLocation(type.getRecipeData().particle())))
                );
        }

    }


    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        ModUpgrades.values().forEach((deferredItem) -> {
            itemModelGenerator.generateFlatItem(deferredItem.getItem().asItem(), ModelTemplates.FLAT_ITEM);
        });


        for(StorageType type : StorageType.values()) {
            if(type.isWooden()) {
                BackpackPackFrameModelProvider.registerPackFrameModel(itemModelGenerator,
                        ModItems.BACKPACK_ITEMS.get(type).asItem(),
                        BackpackPackFrameModelProvider.packFrameTextureMapping(TextureMapping.getBlockTexture(type.backpackModelInfo().planks()),
                                TextureMapping.logColumn(type.backpackModelInfo().log()).get(TextureSlot.SIDE), TextureMapping.getBlockTexture(type.backpackModelInfo().wool()), TextureMapping.getBlockTexture(type.getRecipeData().particle())));
            } else {
                BackpackPackFrameModelProvider.registerBackpackModel(itemModelGenerator,
                        ModItems.BACKPACK_ITEMS.get(type).asItem(),
                        BackpackPackFrameModelProvider.backpackTextureMapping(TextureMapping.getBlockTexture(type.backpackModelInfo().wool()), TextureMapping.getBlockTexture(type.backpackModelInfo().straps()), TextureMapping.getBlockTexture(type.getRecipeData().particle())));
            }
        }
    }
}
