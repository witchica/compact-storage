package com.witchica.compactstorage.common.screen;

import com.witchica.compactstorage.CompactStorage;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

public class CompactStorageMenuProvider implements ExtendedMenuProvider {
    private final Consumer<FriendlyByteBuf> dataWriter;
    private final Component name;

    public static CompactStorageMenuProvider ofBackpack(InteractionHand hand) {
        return new CompactStorageMenuProvider(friendlyByteBuf -> {
            friendlyByteBuf.writeInt(1);
            friendlyByteBuf.writeInt(hand.ordinal());
        }, Component.translatable("container.compact_storage.backpack"));
    }

    public static CompactStorageMenuProvider ofBlock(BlockPos pos, Component name) {
        return new CompactStorageMenuProvider(friendlyByteBuf -> {
            friendlyByteBuf.writeInt(0);
            friendlyByteBuf.writeBlockPos(pos);
        }, name);
    }

    protected CompactStorageMenuProvider(Consumer<FriendlyByteBuf> dataWriter, Component name) {
        this.dataWriter = dataWriter;
        this.name = name;
    }

    @Override
    public void saveExtraData(FriendlyByteBuf buf) {
        this.dataWriter.accept(buf);
    }

    @Override
    public Component getDisplayName() {
        return name;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        saveExtraData(buf);
        return new CompactChestScreenHandler(i, inventory, buf);
    }
}
