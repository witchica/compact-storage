package com.tattyseal.compactstorage.creativetabs;

import javax.annotation.Nonnull;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

/**
 * Created by Toby on 07/11/2014.
 */
public class CreativeTabCompactStorage extends CreativeTabs {
	public CreativeTabCompactStorage() {
		super("compactStorage");
	}

	@Override
	@Nonnull
	public ItemStack createIcon() {
		return new ItemStack(Blocks.CHEST, 1, 0);
	}
}
