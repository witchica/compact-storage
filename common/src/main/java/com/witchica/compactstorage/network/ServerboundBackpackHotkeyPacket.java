package com.witchica.compactstorage.network;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.item.BackpackItem;
import net.blay09.mods.balm.Balm;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public class ServerboundBackpackHotkeyPacket implements CustomPacketPayload {
    public static final ServerboundBackpackHotkeyPacket INSTANCE = new ServerboundBackpackHotkeyPacket();
    public static final Type<ServerboundBackpackHotkeyPacket> TYPE = new Type<>(CompactStorage.id("backpack_open"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundBackpackHotkeyPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ServerPlayer serverPlayer, ServerboundBackpackHotkeyPacket serverboundBackpackHotkeyPacket) {
        boolean hasBackpack = Balm.modSupport().trinkets().isEquipped(serverPlayer, itemStack -> {
            return itemStack.getItem() instanceof BackpackItem;
        });

        if(hasBackpack && !serverPlayer.hasContainerOpen()) {
            Balm.networking().openMenu(serverPlayer, new BackpackItem.CuriosBackpackMenuProvider(serverPlayer));
        }
    }
}
