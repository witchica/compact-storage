package com.tabithastrong.compactstorage;

import com.tabithastrong.compactstorage.client.entity.CompactChestBlockEntityRenderer;
import com.tabithastrong.compactstorage.client.screen.CompactChestScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = CompactStorage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CompactStorageClient {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent fmlClientSetupEvent) {
        MenuScreens.register(CompactStorage.COMPACT_CHEST_SCREEN_HANDLER.get(), CompactChestScreen::new);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers registerRenderers) {
        registerRenderers.registerBlockEntityRenderer(CompactStorage.COMPACT_CHEST_ENTITY_TYPE.get(), CompactChestBlockEntityRenderer::new);
    }
}
