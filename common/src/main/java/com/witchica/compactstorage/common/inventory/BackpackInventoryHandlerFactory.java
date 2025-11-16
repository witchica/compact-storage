package com.witchica.compactstorage.common.inventory;

import com.witchica.compactstorage.CompactStoragePlatform;
import com.witchica.compactstorage.common.item.BackpackItem;
import com.witchica.compactstorage.common.screen.CompactChestScreenHandler;
import com.witchica.compactstorage.common.util.InventoryOpenSource;
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

import java.util.Optional;

public class BackpackInventoryHandlerFactory implements MenuProvider {
    private final Optional<InteractionHand> hand;
    public InventoryOpenSource openSource;
    public ItemStack backpackStack;

    public BackpackInventoryHandlerFactory(Player player, InventoryOpenSource openSource, Optional<InteractionHand> hand) {
        this.openSource = openSource;
        this.hand = hand;
        this.backpackStack = findBackpackStack(player, openSource, hand);
    }

    @Override
    public Component getDisplayName() {
        return backpackStack.getHoverName();
    }

    public static BackpackInventory getBackpackInventory(Player player, InventoryOpenSource openSource, Optional<InteractionHand> hand) {
        ItemStack backpackStack = findBackpackStack(player, openSource, hand);
        return new BackpackInventory(player, openSource, backpackStack, hand);
    }

    public static ItemStack findBackpackStack(Player player, InventoryOpenSource openSource, Optional<InteractionHand> hand) {
        if(openSource == InventoryOpenSource.BACKPACK_OPEN_HAND) {
            ItemStack stack = player.getItemInHand(hand.orElse(InteractionHand.MAIN_HAND));
            if(stack.getItem() instanceof BackpackItem) {
                return stack;
            } else {
                return ItemStack.EMPTY;
            }
        } else {
            ItemStack stack = CompactStoragePlatform.getAdditionalSlotBackpack(player).orElse(ItemStack.EMPTY);
            return stack;
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
        BackpackInventory backpackInventory = getBackpackInventory(player, openSource, hand);
        return new CompactChestScreenHandler(syncId, inv, writeToByteBuf(new FriendlyByteBuf(Unpooled.buffer())));
    }

    public FriendlyByteBuf writeToByteBuf(FriendlyByteBuf buf) {
        buf.writeInt(openSource.ordinal());
        buf.writeInt(hand.orElse(InteractionHand.MAIN_HAND).ordinal());
        return buf;
    }
}
