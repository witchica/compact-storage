package com.witchica.compactstorage.fabric.client;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.client.BackpackFeatureRenderer;
import net.blay09.mods.balm.client.BalmClient;
import net.blay09.mods.balm.fabric.platform.runtime.FabricLoadContext;
import net.fabricmc.api.ClientModInitializer;
import com.witchica.compactstorage.client.CompactStorageClient;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.social.PlayerEntry;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

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
