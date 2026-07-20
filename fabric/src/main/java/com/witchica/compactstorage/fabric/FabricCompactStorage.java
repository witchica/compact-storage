package com.witchica.compactstorage.fabric;

import com.witchica.compactstorage.CompactStorage;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.fabric.platform.runtime.FabricLoadContext;
import net.fabricmc.api.ModInitializer;

public class FabricCompactStorage implements ModInitializer {
    @Override
    public void onInitialize() {
        Balm.initializeMod(CompactStorage.MOD_ID, FabricLoadContext.INSTANCE, CompactStorage::initialize);
    }
}
