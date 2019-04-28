package com.tattyseal.compactstorage.inventory;

import javax.annotation.Nonnull;

import com.tattyseal.compactstorage.tileentity.TileEntityBarrel;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class BarrelItemHandler implements IItemHandler {

	TileEntityBarrel barrel;

	public BarrelItemHandler(TileEntityBarrel barrel) {
		this.barrel = barrel;
	}

	@Override
	public int getSlots() {
		return 1;
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int slot) {
		return barrel.getBarrelStack();
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		return barrel.insertItems(stack, null, simulate);
	}

	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		return barrel.tryTakeStack(null, amount, simulate);
	}

	@Override
	public int getSlotLimit(int slot) {
		return barrel.getMaxStorage();
	}
}
