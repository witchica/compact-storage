package com.witchica.compactstorage.neoforge.client;

import com.witchica.compactstorage.CompactStorage;
import net.blay09.mods.balm.client.BalmClient;
import net.blay09.mods.balm.neoforge.platform.runtime.NeoForgeLoadContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import com.witchica.compactstorage.client.CompactStorageClient;

@Mod(value = CompactStorage.MOD_ID, dist = Dist.CLIENT)
public class NeoForgeCompactStorageClient {

    public NeoForgeCompactStorageClient(IEventBus modEventBus) {
        final var context = new NeoForgeLoadContext(modEventBus);
        BalmClient.initializeMod(CompactStorage.MOD_ID, context, CompactStorageClient::initialize);
    }
}
