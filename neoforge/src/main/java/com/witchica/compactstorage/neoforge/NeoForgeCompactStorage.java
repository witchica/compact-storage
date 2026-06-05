package com.witchica.compactstorage.neoforge;

import com.witchica.compactstorage.CompactStorage;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.neoforge.platform.runtime.NeoForgeLoadContext;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(CompactStorage.MOD_ID)
public class NeoForgeCompactStorage {

    public NeoForgeCompactStorage(ModContainer modContainer, IEventBus modEventBus) {
        final var context = new NeoForgeLoadContext(modContainer, modEventBus);
        Balm.initializeMod(CompactStorage.MOD_ID, context, CompactStorage::initialize);
    }
}
