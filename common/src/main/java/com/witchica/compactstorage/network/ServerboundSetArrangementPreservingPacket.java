package com.witchica.compactstorage.network;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.menu.GenericCompactStorageMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public record ServerboundSetArrangementPreservingPacket(int containerId, boolean preservesArrangement) implements CustomPacketPayload {
    public static final Type<ServerboundSetArrangementPreservingPacket> TYPE = new Type<>(CompactStorage.id("set_arrangement_preserving"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundSetArrangementPreservingPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ServerboundSetArrangementPreservingPacket::containerId,
            ByteBufCodecs.BOOL, ServerboundSetArrangementPreservingPacket::preservesArrangement,
            ServerboundSetArrangementPreservingPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ServerPlayer serverPlayer, ServerboundSetArrangementPreservingPacket packet) {
        if(serverPlayer.containerMenu.containerId == packet.containerId() && serverPlayer.containerMenu instanceof GenericCompactStorageMenu menu) {
            menu.setPreservesArrangement(packet.preservesArrangement());
        }
    }
}
