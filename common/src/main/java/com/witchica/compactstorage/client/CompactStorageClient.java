package com.witchica.compactstorage.client;

import com.witchica.compactstorage.block.entity.ModBlockEntities;
import com.witchica.compactstorage.client.renderer.CompactChestBlockEntityRenderer;
import com.witchica.compactstorage.client.renderer.DrumBlockEntityRenderer;
import com.witchica.compactstorage.client.screens.GenericCompactStorageMenuScreen;
import com.witchica.compactstorage.menu.ModMenuTypes;
import net.blay09.mods.balm.client.BalmClientRegistrars;

public class CompactStorageClient {

    public static void initialize(BalmClientRegistrars registrars) {
        CompactStorageKeyMappings.initialize();
        registrars.blockEntityRenderers(balmBlockEntityRendererRegistrar -> {
            balmBlockEntityRendererRegistrar.register(ModBlockEntities.COMPACT_CHEST_ENTITY, CompactChestBlockEntityRenderer::new);
            balmBlockEntityRendererRegistrar.register(ModBlockEntities.DRUM_BLOCK_ENTITY, DrumBlockEntityRenderer::new);
        });

        registrars.menuScreens(balmMenuScreenRegistrar -> {
            balmMenuScreenRegistrar.register(ModMenuTypes.COMPACT_STORAGE_MENU, GenericCompactStorageMenuScreen::new);
        });
    }

}
