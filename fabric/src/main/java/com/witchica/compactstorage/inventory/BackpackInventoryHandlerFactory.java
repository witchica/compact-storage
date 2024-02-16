package com.witchica.compactstorage.inventory;

import com.witchica.compactstorage.screen.CompactChestScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class BackpackInventoryHandlerFactory implements ExtendedScreenHandlerFactory {
    public InteractionHand hand;
    public ItemStack backpackStack;

    public BackpackInventoryHandlerFactory(Player player, InteractionHand hand) {
        this.hand = hand;
        this.backpackStack = player.getItemInHand(hand);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
        buf.writeInt(1);
        buf.writeInt(hand.ordinal());
    }

    @Override
    public Component getDisplayName() {
        return backpackStack.hasCustomHoverName() ? backpackStack.getHoverName() : Component.translatable("container.compact_storage.backpack");
    }

    public static BackpackInventory getBackpackInventory(Player player, InteractionHand hand) {
        ItemStack backpackStack = player.getItemInHand(hand);
        boolean isInOffhand = hand == InteractionHand.OFF_HAND;

        if(backpackStack.hasTag() && backpackStack.getTag().contains("Backpack")) {
            CompoundTag backpackTag = backpackStack.getTag().getCompound("Backpack");
            return new BackpackInventory(backpackTag, player, isInOffhand);
        } else {
            return new BackpackInventory(new CompoundTag(), player, isInOffhand);
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
        BackpackInventory backpackInventory = getBackpackInventory(player, hand);
        return new CompactChestScreenHandler(syncId, inv, writeToByteBuf());
    }

    private FriendlyByteBuf writeToByteBuf() {
        FriendlyByteBuf packetByteBuf = PacketByteBufs.create();
        packetByteBuf.writeInt(1);
        packetByteBuf.writeInt(hand == InteractionHand.MAIN_HAND ? 0 : 1);

        return packetByteBuf;
    }
}
