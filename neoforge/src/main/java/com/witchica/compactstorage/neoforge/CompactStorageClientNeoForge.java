package com.witchica.compactstorage.neoforge;

import com.witchica.compactstorage.CompactStoragePlatform;
import com.witchica.compactstorage.common.client.entity.CompactChestBlockEntityRenderer;
import com.witchica.compactstorage.common.client.entity.DrumBlockEntityRenderer;
import com.witchica.compactstorage.common.client.screen.CompactChestScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@Mod.EventBusSubscriber(modid = CompactStorageNeoForge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CompactStorageClientNeoForge {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent fmlClientSetupEvent) {
        MenuScreens.register(CompactStorageNeoForge.COMPACT_CHEST_SCREEN_HANDLER.get(), CompactChestScreen::new);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers registerRenderers) {
        registerRenderers.registerBlockEntityRenderer(CompactStorageNeoForge.COMPACT_CHEST_ENTITY_TYPE.get(), CompactChestBlockEntityRenderer::new);
        registerRenderers.registerBlockEntityRenderer(CompactStorageNeoForge.DRUM_ENTITY_TYPE.get(), DrumBlockEntityRenderer::new);
    }
}
