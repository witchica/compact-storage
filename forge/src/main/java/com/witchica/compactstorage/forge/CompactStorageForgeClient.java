package com.witchica.compactstorage.forge;

import com.witchica.compactstorage.common.CompactStorage;
import com.witchica.compactstorage.common.CompactStorageCommonClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = CompactStorage.MOD_ID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class CompactStorageForgeClient {

    @SubscribeEvent
    public static void onKeyBindRegister(RegisterKeyMappingsEvent event) {
        event.register(CompactStorageCommonClient.KEY_BINDING_BACKPACK);
    }
}
