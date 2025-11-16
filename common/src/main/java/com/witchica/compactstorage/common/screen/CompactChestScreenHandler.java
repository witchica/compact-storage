package com.witchica.compactstorage.common.screen;

import com.witchica.compactstorage.common.CompactStorage;
import com.witchica.compactstorage.common.inventory.BackpackInventory;

import com.witchica.compactstorage.common.inventory.BackpackInventoryHandlerFactory;
import com.witchica.compactstorage.common.inventory.slot.BackpackHoldSlot;
import com.witchica.compactstorage.common.item.BackpackItem;
import com.witchica.compactstorage.common.util.CompactStorageInventoryImpl;
import com.witchica.compactstorage.common.util.CompactStorageUtil;
import com.witchica.compactstorage.common.util.InventoryOpenSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CompactChestScreenHandler extends AbstractContainerMenu {
    private Container inventory;
    private Inventory playerInventory;

    public CompactStorageInventoryImpl blockEntity;

    public int inventoryWidth;
    public int inventoryHeight;
    public InventoryOpenSource openSource;
    private int backpackSlot = -1;

    public CompactStorageUtil.StorageVisualTypes visualType;

    public CompactChestScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf buf) {
        super(CompactStorage.COMPACT_CHEST_SCREEN_HANDLER.get(), syncId);
        openSource = InventoryOpenSource.values()[buf.readInt()];

        this.playerInventory = playerInventory;

        switch(openSource) {
            case CHEST_BARREL: {
                BlockPos pos = buf.readBlockPos();
                CompactStorageInventoryImpl inv = (CompactStorageInventoryImpl) playerInventory.player.level().getBlockEntity(pos);
                this.inventory = (Container) inv;
                this.inventoryWidth = inv.getInventoryWidth();
                this.inventoryHeight = inv.getInventoryHeight();
                visualType = inv.getVisualType();
                this.blockEntity = inv;
                break;
            } default: {
                Optional<InteractionHand> hand = Optional.of(InteractionHand.values()[buf.readInt()]);
                BackpackInventory backpackInventory = BackpackInventoryHandlerFactory.getBackpackInventory(playerInventory.player, openSource, hand);
                visualType = backpackInventory.getVisualType();
                this.inventory = (Container) backpackInventory;
                this.inventoryWidth = backpackInventory.inventoryWidth;
                this.inventoryHeight = backpackInventory.inventoryHeight;
                this.blockEntity = null;
                if(openSource == InventoryOpenSource.BACKPACK_OPEN_HAND) {
                    if(hand.get() ==  InteractionHand.MAIN_HAND) {
                        this.backpackSlot = playerInventory.selected;
                    }
                }
                break;
            }
        }

        checkContainerSize(inventory, inventoryWidth * inventoryHeight);
        inventory.startOpen(playerInventory.player);

        setupSlots(true);
    }

    protected CompactChestScreenHandler(@Nullable MenuType<?> type, int syncId) {
        super(type, syncId);
    }

    @Override
    public void removed(final Player player) {
        super.removed(player);
        inventory.stopOpen(player);
    }

    public void setupSlots(final boolean includeChestInventory) {
        // Creating Slots for GUI. A Slot is essentially a correspoding from inventory itemstacks to the GUI position.
        int i;
        int j;

        boolean checkBackpack = openSource != InventoryOpenSource.CHEST_BARREL;

        this.slots.clear();

        final int chestInvHeight = inventoryHeight * 18;

        // Chest Inventory
        for (i = 0; i < inventoryHeight; i++) {
            for (j = 0; j < inventoryWidth; j++) {
                final Slot slot = new BackpackHoldSlot(inventory, i * inventoryWidth + j, 8 + j * 18, 1 + 18 + i * 18, checkBackpack);
                this.addSlot(slot);
            }
        }

        // Player Inventory (27 storage + 9 hotbar)
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 9; j++) {
                this.addSlot(new BackpackHoldSlot(playerInventory, i * 9 + j + 9, 8 + ((inventoryWidth * 18) / 2) - (9 * 9) + j * 18, 7 + 18 + i * 18 + chestInvHeight + 18, checkBackpack));
            }
        }


        for (j = 0; j < 9; j++) {
            this.addSlot(new BackpackHoldSlot(playerInventory, j, 8 + ((inventoryWidth * 18) / 2) - (9 * 9) + j * 18, 7+18 + chestInvHeight + 60 + 18, checkBackpack));
        }
    }

    @Override
    public boolean stillValid(final Player player) {
        return this.inventory.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(final Player player, final int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        final Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasItem()) {

            final ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();

            if (invSlot < this.inventory.getContainerSize()) {
                if (!this.moveItemStackTo(originalStack, this.inventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 0, this.inventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return newStack;
    }
}