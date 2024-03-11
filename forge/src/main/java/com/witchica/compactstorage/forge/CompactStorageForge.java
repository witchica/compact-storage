package com.witchica.compactstorage.forge;

import com.witchica.compactstorage.CompactStorage;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static com.witchica.compactstorage.CompactStorage.MOD_ID;

@Mod(MOD_ID)
public class CompactStorageForge {

    public CompactStorageForge() {
        EventBuses.registerModEventBus(MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        CompactStorage.onInitialize();
    }
}
