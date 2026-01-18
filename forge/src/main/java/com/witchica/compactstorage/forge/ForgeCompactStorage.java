package com.witchica.compactstorage.forge;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.client.CompactStorageClient;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.client.BalmClient;
import net.blay09.mods.balm.forge.platform.runtime.ForgeLoadContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(CompactStorage.MOD_ID)
public class ForgeCompactStorage {

    public ForgeCompactStorage(FMLJavaModLoadingContext context) {
        final var loadContext = new ForgeLoadContext(context.getModBusGroup());
        Balm.initializeMod(CompactStorage.MOD_ID, loadContext, CompactStorage::initialize);
        if (FMLEnvironment.dist.isClient()) {
            BalmClient.initializeMod(CompactStorage.MOD_ID, loadContext, CompactStorageClient::initialize);
        }
    }

}
