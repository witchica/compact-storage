package com.witchica.compactstorage.neoforge.client;

import com.google.common.eventbus.Subscribe;
import com.witchica.compactstorage.common.CompactStorage;
import com.witchica.compactstorage.common.CompactStorageCommonClient;
import com.witchica.compactstorage.common.client.entity.BackpackFeatureRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = CompactStorage.MOD_ID, value = Dist.CLIENT)
public class CompactStorageClientNeoForge {
    @SubscribeEvent
    public static void onKeyBindRegister(RegisterKeyMappingsEvent event) {
        event.register(CompactStorageCommonClient.KEY_BINDING_BACKPACK);
    }

    @SubscribeEvent
    public static void onPlayer(EntityRenderersEvent.AddLayers event) {
        for(PlayerSkin.Model skin : event.getSkins()) {
            PlayerRenderer renderer =  event.getSkin(skin);
            if(renderer != null) {
                renderer.addLayer(new BackpackFeatureRenderer(renderer, event.getContext().getItemInHandRenderer()));
            }
        }
    }
}
