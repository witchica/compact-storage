package com.tabithastrong.compactstorage;

import com.tabithastrong.compactstorage.client.entity.CompactChestBlockEntityRenderer;
import com.tabithastrong.compactstorage.client.screen.CompactChestScreen;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

@Environment(EnvType.CLIENT)
public class CompactStorageClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(CompactStorage.COMPACT_CHEST_SCREEN_HANDLER, CompactChestScreen::new);
        BlockEntityRendererRegistry.register(CompactStorage.COMPACT_CHEST_ENTITY_TYPE, CompactChestBlockEntityRenderer::new);
    }
    
}
