package com.witchica.compactstorage.client;

import com.witchica.compactstorage.block.entity.CompactStorageBlockEntities;
import com.witchica.compactstorage.client.renderer.CompactChestBlockEntityRenderer;
import com.witchica.compactstorage.client.screens.GenericCompactStorageMenuScreen;
import com.witchica.compactstorage.menu.CompactStorageMenuTypes;
import com.witchica.compactstorage.menu.GenericCompactStorageMenu;
import net.blay09.mods.balm.client.BalmClientRegistrars;
import net.minecraft.client.Minecraft;
import net.minecraft.data.AtlasIds;

public class CompactStorageClient {

    public static void initialize(BalmClientRegistrars registrars) {
        CompactStorageKeyMappings.initialize();
        registrars.blockEntityRenderers(balmBlockEntityRendererRegistrar -> {
            balmBlockEntityRendererRegistrar.register(CompactStorageBlockEntities.COMPACT_CHEST_ENTITY, CompactChestBlockEntityRenderer::new);
        });

        registrars.menuScreens(balmMenuScreenRegistrar -> {
            balmMenuScreenRegistrar.register(CompactStorageMenuTypes.COMPACT_STORAGE_MENU, GenericCompactStorageMenuScreen::new);
        });
    }

}
