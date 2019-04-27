package com.tattyseal.compactstorage.inventory.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * Created by tobystrong on 02/05/2017.
 */
public class SlotUnplaceable extends SlotItemHandler {

	public SlotUnplaceable(ItemStackHandler inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}
}
