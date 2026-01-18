package com.witchica.compactstorage.fabric.client;

import com.witchica.compactstorage.CompactStorage;
import net.blay09.mods.balm.client.BalmClient;
import net.blay09.mods.balm.fabric.platform.runtime.FabricLoadContext;
import net.fabricmc.api.ClientModInitializer;
import com.witchica.compactstorage.client.CompactStorageClient;

public class FabricCompactStorageClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BalmClient.initializeMod(CompactStorage.MOD_ID, FabricLoadContext.INSTANCE, CompactStorageClient::initialize);
    }
}
