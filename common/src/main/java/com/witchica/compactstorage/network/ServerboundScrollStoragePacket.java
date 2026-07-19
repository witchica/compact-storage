package com.witchica.compactstorage.network;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.menu.GenericCompactStorageMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public record ServerboundScrollStoragePacket(int containerId, int scrollX, int scrollY) implements CustomPacketPayload {
    public static final Type<ServerboundScrollStoragePacket> TYPE = new Type<>(CompactStorage.id("scroll_storage"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundScrollStoragePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ServerboundScrollStoragePacket::containerId,
            ByteBufCodecs.VAR_INT, ServerboundScrollStoragePacket::scrollX,
            ByteBufCodecs.VAR_INT, ServerboundScrollStoragePacket::scrollY,
            ServerboundScrollStoragePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ServerPlayer serverPlayer, ServerboundScrollStoragePacket packet) {
        if(serverPlayer.containerMenu.containerId == packet.containerId() && serverPlayer.containerMenu instanceof GenericCompactStorageMenu menu) {
            menu.setScroll(packet.scrollX(), packet.scrollY());
        }
    }
}
