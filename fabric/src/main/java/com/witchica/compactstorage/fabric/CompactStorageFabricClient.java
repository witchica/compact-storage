package com.witchica.compactstorage.fabric;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.common.client.entity.CompactChestBlockEntityRenderer;
import com.witchica.compactstorage.common.client.entity.DrumBlockEntityRenderer;
import com.witchica.compactstorage.common.client.screen.CompactChestScreen;

import com.witchica.compactstorage.fabric.CompactStorageFabric;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

@Environment(EnvType.CLIENT)
public class CompactStorageFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
    }
    
}
