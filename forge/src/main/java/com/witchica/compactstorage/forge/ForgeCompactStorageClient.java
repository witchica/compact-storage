package com.witchica.compactstorage.forge;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.client.BackpackFeatureRenderer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = CompactStorage.MOD_ID)
public class ForgeCompactStorageClient {
    @SubscribeEvent
    public static void onPlayerLayer(EntityRenderersEvent.AddLayers event) {
        for(PlayerModelType playerModelType : event.getModelTypes()) {
            AvatarRenderer<AbstractClientPlayer> renderer = event.getPlayerRenderer(playerModelType);

            if(renderer != null) {
                renderer.addLayer(new BackpackFeatureRenderer(renderer, event.getContext().getItemModelResolver()));
            }
        }
    }
}
