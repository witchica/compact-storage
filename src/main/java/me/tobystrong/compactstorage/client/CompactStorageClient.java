//package me.tobystrong.compactstorage.client;
//
//import me.tobystrong.compactstorage.CompactStorage;
//import me.tobystrong.compactstorage.client.gui.CompactChestScreen;
//import me.tobystrong.compactstorage.client.render.BarrelBlockEntityRenderer;
//import me.tobystrong.compactstorage.client.render.CompactChestBlockEntityRenderer;
//import me.tobystrong.compactstorage.container.CompactChestContainer;
//import net.fabricmc.api.ClientModInitializer;
//import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
//import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
//import net.minecraft.block.entity.ChestBlockEntity;
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.text.TranslatableText;
//
//public class CompactStorageClient implements ClientModInitializer {
//    @Override
//    public void onInitializeClient() {
//        ScreenProviderRegistry.INSTANCE.<CompactChestContainer>registerFactory(CompactStorage.COMPACT_CHEST_IDENTIFIER, (container -> new CompactChestScreen(container, MinecraftClient.getInstance().player.inventory, new TranslatableText(CompactStorage.COMPACT_CHEST_TRANSLATION_KEY))));
//        ScreenProviderRegistry.INSTANCE.<CompactChestContainer>registerFactory(CompactStorage.BACKPACK_IDENTIFIER, (container -> new CompactChestScreen(container, MinecraftClient.getInstance().player.inventory, new TranslatableText(CompactStorage.BACKPACK_TRANSLATION_KEY))));
//
//        BlockEntityRendererRegistry.INSTANCE.register(CompactStorage.BARREL_ENTITY_TYPE, BarrelBlockEntityRenderer::new);
//        BlockEntityRendererRegistry.INSTANCE.register(CompactStorage.COMPACT_CHEST_ENTITY_TYPE, CompactChestBlockEntityRenderer::new);
//
//        //BlockRenderLayerMap.INSTANCE.putBlock(CompactStorage.BARREL, RenderLayer.get);
//    }
//}
