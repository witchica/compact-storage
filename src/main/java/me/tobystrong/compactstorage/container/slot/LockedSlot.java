package me.tobystrong.compactstorage.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;

public class LockedSlot extends Slot {
    public LockedSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }


}
