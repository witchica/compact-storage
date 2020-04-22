package me.tobystrong.compactstorage.container;

import me.tobystrong.compactstorage.block.entity.CompactChestBlockEntity;
import me.tobystrong.compactstorage.inventory.BackpackInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class CompactChestContainer extends Container {
    private final IInventory inventory;
    private final PlayerInventory playerInventory;

    public CompactChestBlockEntity blockEntity;

    public final int inventoryWidth;
    public final int inventoryHeight;

    private ItemStack backpack;

    public CompactChestContainer(ContainerType<CompactChestContainer> type, final int syncId, final PlayerInventory playerInventory, final IInventory inventory, final int inventoryWidth, final int inventoryHeight, final Hand hand) {
        super(type, syncId);
        this.inventory = inventory;
        this.playerInventory = playerInventory;
        this.inventoryWidth = inventoryWidth;
        this.inventoryHeight = inventoryHeight;

        if(inventory instanceof CompactChestBlockEntity) {
            this.blockEntity = (CompactChestBlockEntity) inventory;
            this.backpack = null;
        } else if(inventory instanceof BackpackInventory) {
            this.backpack = playerInventory.player.getHeldItem(hand);
            this.blockEntity = null;
        }

        assertInventorySize(inventory, inventoryWidth * inventoryHeight);
        inventory.openInventory(playerInventory.player);

        setupSlots(true);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public void onContainerClosed(final PlayerEntity player) {
        super.onContainerClosed(player);
        inventory.closeInventory(player);
    }

    public void setupSlots(final boolean includeChestInventory) {
        // Creating Slots for GUI. A Slot is essentially a correspoding from inventory itemstacks to the GUI position.
        int i;
        int j;

        this.inventorySlots.clear();

        final int chestInvHeight = inventoryHeight * 18;

        // Chest Inventory
        for (i = 0; i < inventoryHeight; i++) {
            for (j = 0; j < inventoryWidth; j++) {
                final Slot slot = new Slot(inventory, i * inventoryWidth + j, 8 + j * 18, 18 + i * 18);
                this.addSlot(slot);
            }
        }

        // Player Inventory (27 storage + 9 hotbar)
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, i * 9 + j + 9, 8 + ((inventoryWidth * 18) / 2) - (9 * 9) + j * 18, 18 + i * 18 + chestInvHeight + 18));
            }
        }


        for (j = 0; j < 9; j++) {
            if(this.blockEntity == null && j==playerInventory.currentItem) {
                this.addSlot(new Slot(playerInventory, j, 8 + ((inventoryWidth * 18) / 2) - (9 * 9) + j * 18, 18 + chestInvHeight + 60 + 18) {
                    @Override
                    public boolean canTakeStack(PlayerEntity p_82869_1_) {
                        return false;
                    }
                });
            } else {
                this.addSlot(new Slot(playerInventory, j, 8 + ((inventoryWidth * 18) / 2) - (9 * 9) + j * 18, 18 + chestInvHeight + 60 + 18));
            }
        }
    }


    @Override
    public ItemStack transferStackInSlot(final PlayerEntity player, final int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(invSlot);
        if (slot != null && slot.getHasStack()) {

            final ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();

            if (invSlot < this.inventory.getSizeInventory()) {
                if (!this.mergeItemStack(originalStack, this.inventory.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(originalStack, 0, this.inventory.getSizeInventory(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return newStack;
    }
}
