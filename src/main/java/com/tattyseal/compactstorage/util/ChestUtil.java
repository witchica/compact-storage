package com.tattyseal.compactstorage.util;

import net.minecraft.item.ItemStack;

public class ChestUtil
{
	public static enum Type 
	{
		CHEST,
		BACKPACK;
	}
	
	public static int clamp(int val, int min, int max) 
	{
	    return Math.max(min, Math.min(max, val));
	}
}
