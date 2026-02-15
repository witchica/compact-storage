package com.witchica.compactstorage.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.witchica.compactstorage.CompactStorage;
import net.blay09.mods.kuma.api.InputBinding;
import net.blay09.mods.kuma.api.Kuma;
import net.blay09.mods.kuma.api.ManagedKeyMapping;

import static com.witchica.compactstorage.CompactStorage.id;

public class CompactStorageKeyMappings {

    public static ManagedKeyMapping yourKey;

    public static void initialize() {
        yourKey = Kuma.createKeyMapping(id("your_key"))
                .withDefault(InputBinding.key(InputConstants.KEY_B))
                .handleScreenInput(event -> {
                    CompactStorage.logger.info("B was pressed - " + CompactStorage.MOD_ID);
                    return true;
                })
                .build();
    }
}
