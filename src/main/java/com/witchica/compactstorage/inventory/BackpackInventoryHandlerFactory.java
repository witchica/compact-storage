package com.witchica.compactstorage.inventory;

import com.witchica.compactstorage.screen.CompactChestScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;

public class BackpackInventoryHandlerFactory implements ExtendedScreenHandlerFactory {
    public Hand hand;
    public ItemStack backpackStack;

    public BackpackInventoryHandlerFactory(PlayerEntity player, Hand hand) {
        this.hand = hand;
        this.backpackStack = player.getStackInHand(hand);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeInt(1);
        buf.writeInt(hand.ordinal());
    }

    @Override
    public Text getDisplayName() {
        return backpackStack.hasCustomName() ? backpackStack.getName() : Text.translatable("container.compact_storage.backpack");
    }

    public static BackpackInventory getBackpackInventory(PlayerEntity player, Hand hand) {
        ItemStack backpackStack = player.getStackInHand(hand);
        boolean isInOffhand = hand == Hand.OFF_HAND;

        if(backpackStack.hasNbt() && backpackStack.getNbt().contains("Backpack")) {
            NbtCompound backpackTag = backpackStack.getNbt().getCompound("Backpack");
            return new BackpackInventory(backpackTag, player, isInOffhand);
        } else {
            return new BackpackInventory(new NbtCompound(), player, isInOffhand);
        }
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        BackpackInventory backpackInventory = getBackpackInventory(player, hand);
        return new CompactChestScreenHandler(syncId, inv, writeToByteBuf());
    }

    private PacketByteBuf writeToByteBuf() {
        PacketByteBuf packetByteBuf = PacketByteBufs.create();
        packetByteBuf.writeInt(1);
        packetByteBuf.writeInt(hand == Hand.MAIN_HAND ? 0 : 1);

        return packetByteBuf;
    }
}
