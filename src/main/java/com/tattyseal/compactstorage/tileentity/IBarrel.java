package com.tattyseal.compactstorage.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IBarrel {
	public ItemStack dropItems(EntityPlayer player);

	public ItemStack insertItems(ItemStack stack, EntityPlayer player);

	public int color();
}
