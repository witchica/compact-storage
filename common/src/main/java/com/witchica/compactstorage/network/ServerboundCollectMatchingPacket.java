package com.witchica.compactstorage.network;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.menu.GenericCompactStorageMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

/**
 * Shift+double-click is vanilla's own "move every matching stack" gesture but resolves it purely
 * client-side by looping the fixed viewport Slot list. It never learns about matching stacks
 * scrolled out of view, since those have no Slot at all. Vanilla's own sweep runs unmodified (it's
 * exactly correct for the visible portion); this packet runs afterward to also sweep the real
 * container's off-screen slots for the same item type - a no-op for anything vanilla already
 * moved, so this doesn't need to know the on-screen/off-screen boundary itself.
 */
public record ServerboundCollectMatchingPacket(int containerId, ItemStack itemType) implements CustomPacketPayload {
    public static final Type<ServerboundCollectMatchingPacket> TYPE = new Type<>(CompactStorage.id("collect_matching"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundCollectMatchingPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ServerboundCollectMatchingPacket::containerId,
            ItemStack.STREAM_CODEC, ServerboundCollectMatchingPacket::itemType,
            ServerboundCollectMatchingPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ServerPlayer serverPlayer, ServerboundCollectMatchingPacket packet) {
        if(serverPlayer.containerMenu.containerId == packet.containerId() && serverPlayer.containerMenu instanceof GenericCompactStorageMenu menu) {
            menu.collectMatchingIntoPlayer(packet.itemType());
        }
    }
}
