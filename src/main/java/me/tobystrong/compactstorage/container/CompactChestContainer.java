package me.tobystrong.compactstorage.container;

import net.minecraft.container.Container;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class CompactChestContainer extends Container {
    private final Inventory inventory;

    public final int inventoryWidth;
    public final int inventoryHeight;

    public CompactChestContainer(int syncId, PlayerInventory playerInventory, Inventory inventory, int inventoryWidth, int inventoryHeight) {
        super(null, syncId);
        this.inventory = inventory;
        this.inventoryWidth = inventoryWidth;
        this.inventoryHeight = inventoryHeight;

        checkContainerSize(inventory, inventoryWidth * inventoryHeight);
        inventory.onInvOpen(playerInventory.player);

        // Creating Slots for GUI. A Slot is essentially a correspoding from inventory itemstacks to the GUI position.
        int i;
        int j;

        // Chest Inventory
        for (i = 0; i < inventoryHeight; i++) {
            for (j = 0; j < inventoryWidth; j++) {
                this.addSlot(new Slot(inventory, i * inventoryWidth + j, 8 + j * 18, 18 + i * 18));
            }
        }

        int chestInvHeight = inventoryHeight * 18;

        // Player Inventory (27 storage + 9 hotbar)
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, i * 9 + j + 9, 8 + ((inventoryWidth * 18) / 2) - (9 * 9) + j * 18, 18 + i * 18 + chestInvHeight + 18));
            }
        }


        for (j = 0; j < 9; j++) {
            this.addSlot(new Slot(playerInventory, j, 8 + ((inventoryWidth * 18) / 2) - (9 * 9) + j * 18, 18 + chestInvHeight + 60 + 18));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUseInv(player);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.getInvSize()) {
                if (!this.insertItem(originalStack, this.inventory.getInvSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.getInvSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }
}
