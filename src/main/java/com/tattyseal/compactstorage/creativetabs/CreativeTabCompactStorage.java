package com.tattyseal.compactstorage.creativetabs;

import javax.annotation.Nonnull;

import com.tattyseal.compactstorage.CompactRegistry;
import com.tattyseal.compactstorage.tileentity.TileEntityChest;

import net.minecraft.creativetab.CreativeTabs;
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
		ItemStack stack = new ItemStack(CompactRegistry.CHEST);
		new TileEntityChest().writeToNBT(stack.getOrCreateSubCompound("BlockEntityTag"));
		return stack;
	}
}
