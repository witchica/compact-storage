package com.witchica.compactstorage.network;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.menu.GenericCompactStorageMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public record ServerboundMoveStoragePacket(int containerId, boolean matchingOnly, int directionOrdinal, boolean includeHotbar, boolean topOffOnly) implements CustomPacketPayload {
    public static final Type<ServerboundMoveStoragePacket> TYPE = new Type<>(CompactStorage.id("move_storage"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundMoveStoragePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ServerboundMoveStoragePacket::containerId,
            ByteBufCodecs.BOOL, ServerboundMoveStoragePacket::matchingOnly,
            ByteBufCodecs.VAR_INT, ServerboundMoveStoragePacket::directionOrdinal,
            ByteBufCodecs.BOOL, ServerboundMoveStoragePacket::includeHotbar,
            ByteBufCodecs.BOOL, ServerboundMoveStoragePacket::topOffOnly,
            ServerboundMoveStoragePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ServerPlayer serverPlayer, ServerboundMoveStoragePacket packet) {
        if(serverPlayer.containerMenu.containerId != packet.containerId() || !(serverPlayer.containerMenu instanceof GenericCompactStorageMenu menu)) {
            return;
        }

        GenericCompactStorageMenu.MoveDirection[] directions = GenericCompactStorageMenu.MoveDirection.values();
        GenericCompactStorageMenu.MoveDirection direction = directions[Math.clamp(packet.directionOrdinal(), 0, directions.length - 1)];

        if(packet.matchingOnly()) {
            menu.moveMatching(direction, packet.includeHotbar(), packet.topOffOnly());
        } else {
            menu.moveAll(direction, packet.includeHotbar(), packet.topOffOnly());
        }
    }
}
