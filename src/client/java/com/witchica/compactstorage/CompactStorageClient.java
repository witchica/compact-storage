package com.witchica.compactstorage;

import com.witchica.compactstorage.client.entity.CompactChestBlockEntityRenderer;
import com.witchica.compactstorage.client.entity.DrumBlockEntityRenderer;
import com.witchica.compactstorage.client.screen.CompactChestScreen;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

@Environment(EnvType.CLIENT)
public class CompactStorageClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(CompactStorage.COMPACT_CHEST_SCREEN_HANDLER, CompactChestScreen::new);
        BlockEntityRendererFactories.register(CompactStorage.COMPACT_CHEST_ENTITY_TYPE, CompactChestBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(CompactStorage.DRUM_BLOCK_ENTITY_TYPE.get(), DrumBlockEntityRenderer::new);
    }
    
}
