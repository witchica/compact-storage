package com.witchica.compactstorage.fabric.datagen.providers;

import com.witchica.compactstorage.CompactStorage;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public class BackpackPackFrameModelProvider {
    public static final TextureSlot PRIMARY_TEXTURE = TextureSlot.create("primary", TextureSlot.TEXTURE);
    public static final TextureSlot STRAPS_TEXTURE = TextureSlot.create("straps", TextureSlot.TEXTURE);

    public static final TextureSlot LOG_TEXTURE = TextureSlot.create("log", TextureSlot.TEXTURE);
    public static final TextureSlot PLANKS_TEXTURE = TextureSlot.create("planks", TextureSlot.TEXTURE);
    public static final TextureSlot WOOL_TEXTURE = TextureSlot.create("wool", TextureSlot.TEXTURE);

    public static final ModelTemplate BACKPACK = item("backpack_base", PRIMARY_TEXTURE, STRAPS_TEXTURE);
    public static final ModelTemplate PACK_FRAME = item("pack_frame_base", LOG_TEXTURE, PLANKS_TEXTURE, WOOL_TEXTURE);

    //helper method for creating Models
    private static ModelTemplate item(String parent, TextureSlot... requiredTextureKeys) {
        return new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "item/" + parent)), Optional.empty(), requiredTextureKeys);
    }

    //helper method for creating Models with variants
    private static ModelTemplate item(String parent, String variant, TextureSlot... requiredTextureKeys) {
        return new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "item/" + parent)), Optional.of(variant), requiredTextureKeys);
    }

    public static void registerBackpackModel(ItemModelGenerators generator, Item backpackItem, TextureMapping textures) {
        Identifier model = BACKPACK.create(backpackItem, textures, generator.modelOutput);
        generator.itemModelOutput.accept(backpackItem, ItemModelUtils.plainModel(model));
    }

    public static void registerPackFrameModel(ItemModelGenerators generator, Item backpackItem, TextureMapping textures) {
        Identifier model = PACK_FRAME.create(backpackItem, textures, generator.modelOutput);
        generator.itemModelOutput.accept(backpackItem, ItemModelUtils.plainModel(model));
    }

    public static TextureMapping backpackTextureMapping(Identifier primaryTexture, Identifier strapsTexture) {
        TextureMapping mapping = TextureMapping.singleSlot(PRIMARY_TEXTURE, primaryTexture);
        mapping.put(STRAPS_TEXTURE, strapsTexture);
        return mapping;
    }
    public static TextureMapping packFrameTextureMapping(Identifier planks, Identifier log, Identifier wool) {
        TextureMapping mapping = TextureMapping.singleSlot(PLANKS_TEXTURE, planks);
        mapping.put(LOG_TEXTURE, log);
        mapping.put(WOOL_TEXTURE, wool);
        return mapping;
    }
}
