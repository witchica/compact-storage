package com.witchica.compactstorage.common;

import com.mojang.blaze3d.platform.InputConstants;
import com.witchica.compactstorage.common.client.entity.CompactChestBlockEntityRenderer;
import com.witchica.compactstorage.common.client.entity.DrumBlockEntityRenderer;
import com.witchica.compactstorage.common.client.screen.CompactChestScreen;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import io.netty.buffer.Unpooled;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import org.lwjgl.glfw.GLFW;

public class CompactStorageCommonClient {
    public static final KeyMapping KEY_BINDING_BACKPACK = new KeyMapping("key.compact_storage.backpack", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_U, "key.categories.compact_storage");

    public static void clientSetupEvent(Minecraft minecraft) {
        MenuRegistry.registerScreenFactory(CompactStorage.COMPACT_CHEST_SCREEN_HANDLER.get(), CompactChestScreen::new);
        BlockEntityRendererRegistry.register(CompactStorage.COMPACT_CHEST_ENTITY_TYPE.get(), CompactChestBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(CompactStorage.DRUM_ENTITY_TYPE.get(), DrumBlockEntityRenderer::new);
    }

    public static void clientTickEvent(Minecraft minecraft) {
        if(KEY_BINDING_BACKPACK.consumeClick()) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            NetworkManager.sendToServer(CompactStorage.COMPACT_STORAGE_BACKPACK_KEY, buf);
        }
    }
}
