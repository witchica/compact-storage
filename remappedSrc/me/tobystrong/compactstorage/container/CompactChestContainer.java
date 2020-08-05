package me.tobystrong.compactstorage.container;

import me.tobystrong.compactstorage.block.entity.CompactChestBlockEntity;
import me.tobystrong.compactstorage.inventory.BackpackInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;

public class CompactChestContainer extends ScreenHandler {
    private final Inventory inventory;
    private final PlayerInventory playerInventory;

    public CompactChestBlockEntity blockEntity;

    public final int inventoryWidth;
    public final int inventoryHeight;

    private ItemStack backpack;

    public CompactChestContainer(final int syncId, final PlayerInventory playerInventory, final Inventory inventory, final int inventoryWidth, final int inventoryHeight, final Hand hand) {
        super(null, syncId);
        this.inventory = inventory;
        this.playerInventory = playerInventory;
        this.inventoryWidth = inventoryWidth;
        this.inventoryHeight = inventoryHeight;

        if(inventory instanceof CompactChestBlockEntity) {
            this.blockEntity = (CompactChestBlockEntity) inventory;
            this.backpack = null;
        } else if(inventory instanceof BackpackInventory) {
            this.backpack = playerInventory.player.getStackInHand(hand);
            this.blockEntity = null;
        }

        checkSize(inventory, inventoryWidth * inventoryHeight);
        inventory.onOpen(playerInventory.player);

        setupSlots(true);
    }

    @Override
    public void close(final PlayerEntity player) {
        super.close(player);
        inventory.onClose(player);
    }

    public void setupSlots(final boolean includeChestInventory) {
        // Creating Slots for GUI. A Slot is essentially a correspoding from inventory itemstacks to the GUI position.
        int i;
        int j;

        this.slots.clear();

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
            if(this.blockEntity == null && j==playerInventory.selectedSlot) {
                this.addSlot(new Slot(playerInventory, j, 8 + ((inventoryWidth * 18) / 2) - (9 * 9) + j * 18, 18 + chestInvHeight + 60 + 18) {
                    @Override
                    public boolean canTakeItems(PlayerEntity playerEntity) {
                        return false;
                    }

                });
            } else {
                this.addSlot(new Slot(playerInventory, j, 8 + ((inventoryWidth * 18) / 2) - (9 * 9) + j * 18, 18 + chestInvHeight + 60 + 18));
            }
        }
    }

    @Override
    public boolean canUse(final PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack transferSlot(final PlayerEntity player, final int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        final Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {

            final ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();

            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
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
