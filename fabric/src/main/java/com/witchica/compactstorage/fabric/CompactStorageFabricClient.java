package com.witchica.compactstorage.fabric;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.common.client.entity.CompactChestBlockEntityRenderer;
import com.witchica.compactstorage.common.client.entity.DrumBlockEntityRenderer;
import com.witchica.compactstorage.common.client.screen.CompactChestScreen;

import com.witchica.compactstorage.fabric.CompactStorageFabric;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

import java.util.Arrays;

@Environment(EnvType.CLIENT)
public class CompactStorageFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Arrays.stream(CompactStorage.COMPACT_BARREL_BLOCKS).forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block.get(), RenderType.cutout()));
        Arrays.stream(CompactStorage.COMPACT_BARREL_WOOD_BLOCKS).forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block.get(), RenderType.cutout()));
        Arrays.stream(CompactStorage.DRUM_BLOCKS).forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block.get(), RenderType.cutout()));
    }
    
}
