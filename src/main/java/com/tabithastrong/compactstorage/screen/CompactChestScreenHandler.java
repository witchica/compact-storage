package com.tabithastrong.compactstorage.screen;

import com.tabithastrong.compactstorage.CompactStorage;
import com.tabithastrong.compactstorage.block.entity.CompactChestBlockEntity;
import com.tabithastrong.compactstorage.inventory.BackpackInventory;

import com.tabithastrong.compactstorage.inventory.BackpackInventoryHandlerFactory;
import com.tabithastrong.compactstorage.util.CompactStorageInventoryImpl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CompactChestScreenHandler extends ScreenHandler {
    private Inventory inventory;
    private PlayerInventory playerInventory;

    public CompactStorageInventoryImpl blockEntity;

    public int inventoryWidth;
    public int inventoryHeight;

    private ItemStack backpack;
    private boolean isBackpackInOffhand;

    public CompactChestScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        super(CompactStorage.COMPACT_CHEST_SCREEN_HANDLER, syncId);
        int inventoryType = buf.readInt();

        this.playerInventory = playerInventory;

        if(inventoryType == 0) {
            BlockPos pos = buf.readBlockPos();
            CompactStorageInventoryImpl inv = (CompactStorageInventoryImpl) playerInventory.player.getWorld().getBlockEntity(pos);
            this.inventory = (Inventory) inv;
            this.inventoryWidth = inv.getInventoryWidth();
            this.inventoryHeight = inv.getInventoryHeight();
            this.blockEntity = inv;
            this.backpack = null;
        } else {
            Hand hand = buf.readInt() == 0 ? Hand.MAIN_HAND : Hand.OFF_HAND;
            BackpackInventory backpackInventory = BackpackInventoryHandlerFactory.getBackpackInventory(playerInventory.player, hand);
            this.inventory = (Inventory) backpackInventory;
            this.inventoryWidth = backpackInventory.inventoryWidth;
            this.inventoryHeight = backpackInventory.inventoryHeight;
            this.backpack = playerInventory.player.getStackInHand(hand);
            this.isBackpackInOffhand = hand == Hand.OFF_HAND;
            this.blockEntity = null;
        }

        checkSize(inventory, inventoryWidth * inventoryHeight);
        inventory.onOpen(playerInventory.player);

        setupSlots(true);
    }

    protected CompactChestScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @Override
    public void onClosed(final PlayerEntity player) {
        super.onClosed(player);
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
            if(this.blockEntity == null && j==playerInventory.selectedSlot && !isBackpackInOffhand) {
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
    public ItemStack quickMove(final PlayerEntity player, final int invSlot) {
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