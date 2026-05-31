package com.witchica.compactstorage.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.network.ServerboundBackpackHotkeyPacket;
import io.netty.buffer.Unpooled;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.client.BalmKeyMappingRegistrar;
import net.blay09.mods.kuma.api.InputBinding;
import net.blay09.mods.kuma.api.Kuma;
import net.blay09.mods.kuma.api.ManagedKeyMapping;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.FriendlyByteBuf;

import static com.witchica.compactstorage.CompactStorage.id;

public class CompactStorageKeyMappings {

    public static ManagedKeyMapping backpackOpenKey;

    public static void initialize() {
        backpackOpenKey = Kuma.createKeyMapping(id("backpack_open"))
                .withDefault(InputBinding.key(InputConstants.KEY_U))
                .handleWorldInput(event -> {
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    Balm.networking().sendToServer(ServerboundBackpackHotkeyPacket.INSTANCE);
                    return true;
                }).build();
    }
}
