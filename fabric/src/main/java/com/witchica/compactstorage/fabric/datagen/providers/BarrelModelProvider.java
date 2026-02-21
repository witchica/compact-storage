package com.witchica.compactstorage.fabric.datagen.providers;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.block.CompactBarrelBlock;
import com.witchica.compactstorage.block.base.BaseItemDrumBlock;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public class BarrelModelProvider {
    public static final TextureSlot BARREL_TEXTURE = TextureSlot.create("barrel", TextureSlot.TEXTURE);
    public static final TextureSlot OVERLAY_TEXTURE = TextureSlot.create("overlay", TextureSlot.TEXTURE);

    public static final ModelTemplate BARREL = block("compact_barrel", BARREL_TEXTURE, TextureSlot.PARTICLE);
    public static final ModelTemplate BARREL_OPEN = block("compact_barrel_open", BARREL_TEXTURE, TextureSlot.PARTICLE);
    public static final ModelTemplate BARREL_RETAINING = block("compact_barrel_retaining", BARREL_TEXTURE, OVERLAY_TEXTURE, TextureSlot.PARTICLE);

    //helper method for creating Models
    private static ModelTemplate block(String parent, TextureSlot... requiredTextureKeys) {
        return new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "block/" + parent)), Optional.empty(), requiredTextureKeys);
    }

    //helper method for creating Models with variants
    private static ModelTemplate block(String parent, String variant, TextureSlot... requiredTextureKeys) {
        return new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "block/" + parent)), Optional.of(variant), requiredTextureKeys);
    }

    private static BlockModelDefinitionGenerator createCompactBarrelModel(Block barrelBlock, Identifier standard, Identifier open, Identifier retaining) {
        MultiVariant standardModel = BlockModelGenerators.plainVariant(standard);
        MultiVariant openModel = BlockModelGenerators.plainVariant(open);
        MultiVariant retainingModel = BlockModelGenerators.plainVariant(retaining);

        return MultiVariantGenerator.dispatch(barrelBlock).with(
                PropertyDispatch.initial(CompactBarrelBlock.FACING, CompactBarrelBlock.OPEN, CompactBarrelBlock.RETAINING)
                        .select(Direction.NORTH, false, false, standardModel.with(BlockModelGenerators.X_ROT_90))
                        .select(Direction.NORTH, true, false, openModel.with(BlockModelGenerators.X_ROT_90))
                        .select(Direction.NORTH, false, true, retainingModel.with(BlockModelGenerators.X_ROT_90))
                        .select(Direction.NORTH, true, true, openModel.with(BlockModelGenerators.X_ROT_90))

                        .select(Direction.EAST, false, false, standardModel.with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_90))
                        .select(Direction.EAST, true, false, openModel.with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_90))
                        .select(Direction.EAST, false, true, retainingModel.with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_90))
                        .select(Direction.EAST, true, true, openModel.with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_90))

                        .select(Direction.SOUTH, false, false, standardModel.with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_180))
                        .select(Direction.SOUTH, true, false, openModel.with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_180))
                        .select(Direction.SOUTH, false, true, retainingModel.with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_180))
                        .select(Direction.SOUTH, true, true, openModel.with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_180))

                        .select(Direction.WEST, false, false, standardModel.with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_270))
                        .select(Direction.WEST, true, false, openModel.with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_270))
                        .select(Direction.WEST, false, true, retainingModel.with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_270))
                        .select(Direction.WEST, true, true, openModel.with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_270))

                        .select(Direction.UP, false, false, standardModel)
                        .select(Direction.UP, true, false, openModel)
                        .select(Direction.UP, false, true, retainingModel)
                        .select(Direction.UP, true, true, openModel)

                        .select(Direction.DOWN, false, false, standardModel.with(BlockModelGenerators.X_ROT_180))
                        .select(Direction.DOWN, true, false, openModel.with(BlockModelGenerators.X_ROT_180))
                        .select(Direction.DOWN, false, true, retainingModel.with(BlockModelGenerators.X_ROT_180))
                        .select(Direction.DOWN, true, true, openModel.with(BlockModelGenerators.X_ROT_180))
        );
    }

    private static BlockModelDefinitionGenerator createItemDrumModel(Block barrelBlock, Identifier standard, Identifier retaining) {
        MultiVariant standardModel = BlockModelGenerators.plainVariant(standard);
        MultiVariant retainingModel = BlockModelGenerators.plainVariant(retaining);

        return MultiVariantGenerator.dispatch(barrelBlock).with(
                PropertyDispatch.initial(BaseItemDrumBlock.FACING, BaseItemDrumBlock.RETAINING)
                        .select(Direction.NORTH,  false, standardModel.with(BlockModelGenerators.X_ROT_90))
                        .select(Direction.NORTH,  true, retainingModel.with(BlockModelGenerators.X_ROT_90))

                        .select(Direction.EAST,  false, standardModel.with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_90))
                        .select(Direction.EAST,  true, retainingModel.with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_90))

                        .select(Direction.SOUTH,  false, standardModel.with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_180))
                        .select(Direction.SOUTH,  true, retainingModel.with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_180))

                        .select(Direction.WEST,  false, standardModel.with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_270))
                        .select(Direction.WEST,  true, retainingModel.with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_270))

                        .select(Direction.UP,  false, standardModel)
                        .select(Direction.UP,  true, retainingModel)

                        .select(Direction.DOWN,  false, standardModel.with(BlockModelGenerators.X_ROT_180))
                        .select(Direction.DOWN,  true, retainingModel.with(BlockModelGenerators.X_ROT_180))
        );
    }

    public static void registerCompactBarrel(BlockModelGenerators generator, Block compactBarrelBlock, TextureMapping textures) {
        Identifier normalModel = BARREL.create(compactBarrelBlock, textures, generator.modelOutput);
        Identifier openModel = BARREL_OPEN.createWithSuffix(compactBarrelBlock, "_open", textures, generator.modelOutput);
        Identifier retainingModel = BARREL_RETAINING.createWithSuffix(compactBarrelBlock, "_retaining", textures, generator.modelOutput);
        generator.blockStateOutput.accept(createCompactBarrelModel(compactBarrelBlock, normalModel, openModel, retainingModel));
        generator.registerSimpleItemModel(compactBarrelBlock, normalModel);
    }

    public static void registerItemDrum(BlockModelGenerators generator, Block itemDrumBlock, TextureMapping textures) {
        Identifier normalModel = BARREL.create(itemDrumBlock, textures, generator.modelOutput);
        Identifier retainingModel = BARREL_RETAINING.createWithSuffix(itemDrumBlock, "_retaining", textures, generator.modelOutput);
        generator.blockStateOutput.accept(createItemDrumModel(itemDrumBlock, normalModel, retainingModel));
        generator.registerSimpleItemModel(itemDrumBlock, normalModel);
    }

    public static TextureMapping compactBarrelTextureMapping(Identifier texture, Block particle) {
        TextureMapping mapping = TextureMapping.particle(particle);
        mapping.put(BARREL_TEXTURE, texture);
        mapping.put(OVERLAY_TEXTURE, Identifier.fromNamespaceAndPath("compact_storage", "block/barrel/barrel_retaining_overlay"));
        return mapping;
    }

    public static TextureMapping itemDrumTextureMapping(Identifier texture, Block particle) {
        TextureMapping mapping = TextureMapping.particle(particle);
        mapping.put(BARREL_TEXTURE, texture);
        mapping.put(OVERLAY_TEXTURE, Identifier.fromNamespaceAndPath("compact_storage", "block/barrel/drum_retaining_overlay"));
        return mapping;
    }
}
