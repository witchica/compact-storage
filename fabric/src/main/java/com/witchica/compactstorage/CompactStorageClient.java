package com.witchica.compactstorage;

import com.witchica.compactstorage.common.client.entity.CompactChestBlockEntityRenderer;
import com.witchica.compactstorage.common.client.entity.DrumBlockEntityRenderer;
import com.witchica.compactstorage.common.client.screen.CompactChestScreen;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

import java.util.Arrays;

@Environment(EnvType.CLIENT)
public class CompactStorageClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.register(CompactStorage.COMPACT_CHEST_SCREEN_HANDLER, CompactChestScreen::new);
        BlockEntityRenderers.register(CompactStorage.COMPACT_CHEST_ENTITY_TYPE, CompactChestBlockEntityRenderer::new);
        BlockEntityRenderers.register(CompactStorage.DRUM_BLOCK_ENTITY_TYPE.get(), DrumBlockEntityRenderer::new);

        Arrays.stream(CompactStorage.COMPACT_BARREL_BLOCKS).forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block.get(), RenderType.cutout()));
        Arrays.stream(CompactStorage.WOODEN_COMPACT_BARREL_BLOCKS).forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block.get(), RenderType.cutout()));
        Arrays.stream(CompactStorage.DRUM_BLOCKS).forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block.get(), RenderType.cutout()));
    }
    
}
