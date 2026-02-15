package com.witchica.compactstorage.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import util.CompactStorageOpeningSource;

import java.util.Optional;

public record CompactStorageMenuData(CompactStorageOpeningSource source, Optional<BlockPos> pos, Optional<Integer> hotbarSlot) {
    public static StreamCodec<RegistryFriendlyByteBuf, CompactStorageMenuData> STREAM_CODEC =
            StreamCodec.of((o, data) -> {
                o.writeInt(data.source.ordinal());
                switch(data.source) {
                    case BLOCK: {
                        o.writeBlockPos(data.pos.orElse(BlockPos.ZERO));
                        break;
                    } case  BACKPACK_IN_HAND: {
                        o.writeInt(data.hotbarSlot.orElse(-1));
                        break;
                    } default: {
                        break;
                    }
                }
            }, registryFriendlyByteBuf -> {
                CompactStorageOpeningSource source = CompactStorageOpeningSource.values()[registryFriendlyByteBuf.readInt()];

                switch(source) {
                    case BLOCK: {
                        return CompactStorageMenuData.ofBlock(registryFriendlyByteBuf.readBlockPos());
                    } case BACKPACK_HOT_KEY: {
                        return ofHeldBackpack(registryFriendlyByteBuf.readInt());
                    } default: {
                        return ofCuriosBackpack();
                    }
                }
            });

    public static CompactStorageMenuData ofBlock(BlockPos pos) {
        return new CompactStorageMenuData(CompactStorageOpeningSource.BLOCK, Optional.of(pos), Optional.empty());
    }

    public static CompactStorageMenuData ofHeldBackpack(int hotbarSlot) {
        return new CompactStorageMenuData(CompactStorageOpeningSource.BACKPACK_IN_HAND,Optional.empty(), Optional.of(hotbarSlot));
    }

    public static CompactStorageMenuData ofCuriosBackpack() {
        return new CompactStorageMenuData(CompactStorageOpeningSource.BACKPACK_HOT_KEY,Optional.empty(), Optional.empty());
    }
}
