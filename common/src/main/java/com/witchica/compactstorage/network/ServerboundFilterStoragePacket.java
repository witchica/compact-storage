package com.witchica.compactstorage.network;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.menu.GenericCompactStorageMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public record ServerboundFilterStoragePacket(int containerId, String filter) implements CustomPacketPayload {
    public static final Type<ServerboundFilterStoragePacket> TYPE = new Type<>(CompactStorage.id("filter_storage"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundFilterStoragePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ServerboundFilterStoragePacket::containerId,
            ByteBufCodecs.stringUtf8(64), ServerboundFilterStoragePacket::filter,
            ServerboundFilterStoragePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ServerPlayer serverPlayer, ServerboundFilterStoragePacket packet) {
        if(serverPlayer.containerMenu.containerId == packet.containerId() && serverPlayer.containerMenu instanceof GenericCompactStorageMenu menu) {
            menu.setFilter(packet.filter());
        }
    }
}
