package com.witchica.compactstorage.network;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.menu.GenericCompactStorageMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public record ServerboundSortStoragePacket(int containerId, int sortKeyOrdinal, int arrangementOrdinal) implements CustomPacketPayload {
    public static final Type<ServerboundSortStoragePacket> TYPE = new Type<>(CompactStorage.id("sort_storage"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundSortStoragePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ServerboundSortStoragePacket::containerId,
            ByteBufCodecs.VAR_INT, ServerboundSortStoragePacket::sortKeyOrdinal,
            ByteBufCodecs.VAR_INT, ServerboundSortStoragePacket::arrangementOrdinal,
            ServerboundSortStoragePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ServerPlayer serverPlayer, ServerboundSortStoragePacket packet) {
        if(serverPlayer.containerMenu.containerId != packet.containerId() || !(serverPlayer.containerMenu instanceof GenericCompactStorageMenu menu)) {
            return;
        }

        GenericCompactStorageMenu.SortKey[] keys = GenericCompactStorageMenu.SortKey.values();
        GenericCompactStorageMenu.SortArrangement[] arrangements = GenericCompactStorageMenu.SortArrangement.values();

        GenericCompactStorageMenu.SortKey key = keys[Math.clamp(packet.sortKeyOrdinal(), 0, keys.length - 1)];
        GenericCompactStorageMenu.SortArrangement arrangement = arrangements[Math.clamp(packet.arrangementOrdinal(), 0, arrangements.length - 1)];

        menu.sort(key, arrangement);
    }
}
