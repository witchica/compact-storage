package com.witchica.compactstorage.neoforge.client;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.client.BackpackFeatureRenderer;
import net.blay09.mods.balm.client.BalmClient;
import net.blay09.mods.balm.neoforge.platform.runtime.NeoForgeLoadContext;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.world.entity.player.PlayerModelType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import com.witchica.compactstorage.client.CompactStorageClient;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@Mod(value = CompactStorage.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(value=Dist.CLIENT, modid = CompactStorage.MOD_ID)
public class NeoForgeCompactStorageClient {

    public NeoForgeCompactStorageClient(IEventBus modEventBus) {
        final var context = new NeoForgeLoadContext(modEventBus);
        BalmClient.initializeMod(CompactStorage.MOD_ID, context, CompactStorageClient::initialize);
    }

    @SubscribeEvent
    public static void onPlayerLayer(EntityRenderersEvent.AddLayers event) {
        for(PlayerModelType playerModelType : event.getSkins()) {
            AvatarRenderer<AbstractClientPlayer> renderer = event.getPlayerRenderer(playerModelType);

            if(renderer != null) {
                renderer.addLayer(new BackpackFeatureRenderer(renderer, event.getContext().getItemModelResolver()));
            }
        }
    }
}
