package com.witchica.compactstorage.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.witchica.compactstorage.CompactStorage;
import net.blay09.mods.kuma.api.InputBinding;
import net.blay09.mods.kuma.api.Kuma;
import net.blay09.mods.kuma.api.ManagedKeyMapping;

import static com.witchica.compactstorage.CompactStorage.id;

public class CompactStorageKeyMappings {

    public static ManagedKeyMapping backpackOpenKey;

    public static void initialize() {
        backpackOpenKey = Kuma.createKeyMapping(id("backpack_open"))
                .withDefault(InputBinding.key(InputConstants.KEY_B))
                .handleScreenInput(event -> {
                    CompactStorage.logger.info("B was pressed - " + CompactStorage.MOD_ID);
                    return true;
                })
                .build();
    }
}
