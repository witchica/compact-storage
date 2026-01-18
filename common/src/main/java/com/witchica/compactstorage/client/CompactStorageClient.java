package com.witchica.compactstorage.client;

import com.witchica.compactstorage.block.entity.ModBlockEntities;
import com.witchica.compactstorage.client.renderer.CompactChestBlockEntityRenderer;
import net.blay09.mods.balm.client.BalmClientRegistrars;

public class CompactStorageClient {

    public static void initialize(BalmClientRegistrars registrars) {
        ModKeyMappings.initialize();
        registrars.blockEntityRenderers(balmBlockEntityRendererRegistrar -> {
            balmBlockEntityRendererRegistrar.register(ModBlockEntities.COMPACT_CHEST_ENTITY, CompactChestBlockEntityRenderer::new);
        });
    }

}
