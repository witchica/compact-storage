package me.tobystrong.compactstorage.container;

import me.tobystrong.compactstorage.CompactStorage;
import me.tobystrong.compactstorage.block.tile.CompactChestTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.List;

public class CompactStorageBaseContainer extends Container {
    private PlayerInventory playerInventory;
    public IItemHandler chestInventory;
    public int chestWidth;
    public int chestHeight;

    public CompactStorageBaseContainer(ContainerType<?> type, int windowID, PlayerInventory playerInventory, int chestWidth, int chestHeight, IItemHandler inventory) {
        super(type, windowID);
        this.playerInventory = playerInventory;
        this.chestWidth = chestWidth;
        this.chestHeight = chestHeight;
        this.chestInventory = inventory;

        //add chest slots
        for(int y = 0; y < chestHeight; y++) {
            for(int x = 0; x < chestWidth; x++) {
                int index = x + y * chestWidth;
                addSlot(new SlotItemHandler(inventory, index, 8 + (x * 18), 17 + (y * 18)));
            }
        }

        //get the centered position of the player inventory
        int playerInvX = (int)(8 + ((chestWidth > 9 ? ((chestWidth - 9) * 18) / 2f : 0)));

        //add main inv slots
        for(int y = 0; y < 3; y++) {
            for(int x = 0; x < 9; x++) {
                int index = x + y * 9;
                addSlot(new Slot(playerInventory, 9 + index, playerInvX + (x * 18), 17 + (y * 18) + (chestHeight * 18) + 13));
            }
        }

        //add hotbar
        for(int x = 0; x < 9; x++) {
            //if the inventory is a backpack and the current slot contains said backpack - make it unusable to prevent backpacks going missing
            if(type == CompactStorage.BACKPACK_CONTAINER_TYPE && x == playerInventory.currentItem) {
                addSlot(new Slot(playerInventory, x, playerInvX + (x * 18), 17 + (3 * 18) + (chestHeight * 18) + 17) {
                    @Override
                    public boolean canTakeStack(PlayerEntity playerIn) {
                        return false;
                    }
                });
            } else {
                //otherwise just proceed as usual
                addSlot(new Slot(playerInventory, x, playerInvX + (x * 18), 17 + (3 * 18) + (chestHeight * 18) + 17));
            }
        }
    }

    /*
        Copied from ChestBlock
     */
    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;

        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < this.chestWidth * chestHeight) {
                if (!this.mergeItemStack(itemstack1, this.chestHeight * this.chestWidth, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, this.chestWidth * chestHeight, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}