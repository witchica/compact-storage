package com.witchica.compactstorage.common.inventory;

import com.witchica.compactstorage.common.screen.CompactChestScreenHandler;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class BackpackInventoryHandlerFactory implements MenuProvider {
    public ItemStack backpackStack;

    public BackpackInventoryHandlerFactory(Player player, ItemStack backpackStack) {
        this.backpackStack = backpackStack;
    }

    @Override
    public Component getDisplayName() {
        return backpackStack.getHoverName();
    }

    public static BackpackInventory getBackpackInventory(Player player, ItemStack backpackStack) {
        if(backpackStack.hasTag() && backpackStack.getTag().contains("Backpack")) {
            CompoundTag backpackTag = backpackStack.getTag().getCompound("Backpack");
            return new BackpackInventory(backpackTag, backpackStack);
        } else {
            return new BackpackInventory(new CompoundTag(), player);
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
        BackpackInventory backpackInventory = getBackpackInventory(player, hand);
        return new CompactChestScreenHandler(syncId, inv, writeToByteBuf(new FriendlyByteBuf(Unpooled.buffer())));
    }

    public FriendlyByteBuf writeToByteBuf(FriendlyByteBuf buf) {
        buf.writeInt(1);
        buf.writeInt(hand == InteractionHand.MAIN_HAND ? 0 : 1);
        return buf;
    }
}
