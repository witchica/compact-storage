package com.witchica.compactstorage.forge;

import com.witchica.compactstorage.common.CompactStorage;
import com.witchica.compactstorage.common.CompactStorageCommonClient;
import com.witchica.compactstorage.common.client.entity.BackpackFeatureRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = CompactStorage.MOD_ID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class CompactStorageForgeClient {

    @SubscribeEvent
    public static void onKeyBindRegister(RegisterKeyMappingsEvent event) {
        event.register(CompactStorageCommonClient.KEY_BINDING_BACKPACK);
    }

    @SubscribeEvent
    public static void onPlayer(EntityRenderersEvent.AddLayers event) {
        for(String skin : event.getSkins()) {
            PlayerRenderer renderer =  event.getSkin(skin);
            if(renderer != null) {
                renderer.addLayer(new BackpackFeatureRenderer(renderer, event.getContext().getItemInHandRenderer()));
            }
        }
    }
}
