package com.tattyseal.compactstorage.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IBarrel {
	
	public ItemStack giveItems(EntityPlayer player);

	public ItemStack takeItems(ItemStack stack, EntityPlayer player);

	public int color();
}
