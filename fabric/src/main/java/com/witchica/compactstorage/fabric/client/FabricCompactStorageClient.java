package com.witchica.compactstorage.fabric.client;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.client.BackpackFeatureRenderer;
import com.witchica.compactstorage.client.CompactStorageClient;
import net.blay09.mods.balm.client.BalmClient;
import net.blay09.mods.balm.fabric.platform.runtime.FabricLoadContext;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;

public class FabricCompactStorageClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BalmClient.initializeMod(CompactStorage.MOD_ID, FabricLoadContext.INSTANCE, CompactStorageClient::initialize);

        LivingEntityRenderLayerRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if(entityRenderer instanceof AvatarRenderer) {
                registrationHelper.register(new BackpackFeatureRenderer((RenderLayerParent<AvatarRenderState, PlayerModel>) entityRenderer, context.getItemModelResolver()));
            }
        });
    }
}
