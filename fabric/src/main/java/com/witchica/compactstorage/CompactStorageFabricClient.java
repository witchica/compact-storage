package com.witchica.compactstorage;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;

import java.util.Arrays;

@Environment(EnvType.CLIENT)
public class CompactStorageFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Arrays.stream(CompactStorage.COMPACT_BARREL_BLOCKS).forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block.get(), RenderType.cutout()));
        Arrays.stream(CompactStorage.DRUM_BLOCKS).forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block.get(), RenderType.cutout()));
    }
    
}
