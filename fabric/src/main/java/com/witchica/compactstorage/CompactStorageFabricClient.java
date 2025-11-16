package com.witchica.compactstorage;

import com.witchica.compactstorage.common.CompactStorage;
import com.witchica.compactstorage.common.CompactStorageCommonClient;
import com.witchica.compactstorage.common.client.entity.BackpackFeatureRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

import java.util.Arrays;

@Environment(EnvType.CLIENT)
public class CompactStorageFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Arrays.stream(CompactStorage.COMPACT_BARREL_BLOCKS).forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block.get(), RenderType.cutout()));
        Arrays.stream(CompactStorage.DRUM_BLOCKS).forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block.get(), RenderType.cutout()));
        KeyBindingHelper.registerKeyBinding(CompactStorageCommonClient.KEY_BINDING_BACKPACK);

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, livingEntityRenderer, registrationHelper, context) -> {
            if(!(livingEntityRenderer instanceof PlayerRenderer playerRenderer)) {
                return;
            }

            registrationHelper.register(new BackpackFeatureRenderer(playerRenderer, context.getItemInHandRenderer()));
        });
    }
    
}
