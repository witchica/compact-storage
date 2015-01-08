package com.workshop.compactstorage.util;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public class StorageInfo
{
	private int sizeX;
	private int sizeY;
	
	public StorageInfo(int sizeX, int sizeY)
	{
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}

	public int getSizeX() 
	{
		return sizeX;
	}

	public void setSizeX(int sizeX) 
	{
		this.sizeX = sizeX;
	}

	public int getSizeY() 
	{
		return sizeY;
	}

	public void setSizeY(int sizeY) 
	{
		this.sizeY = sizeY;
	}
	
	public ArrayList<ItemStack> getMaterialCost()
	{
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		
		/* Max 12 */
		
		Item[] primary = new Item[] 
		{
				Items.iron_ingot, 
				Items.gold_ingot,
				Items.diamond, 
				Items.emerald
		};
		
		Item[] secondary = new Item[] 
		{
			ItemBlock.getItemFromBlock(Blocks.log), 
			ItemBlock.getItemFromBlock(Blocks.coal_block), 
			ItemBlock.getItemFromBlock(Blocks.iron_bars), 
			ItemBlock.getItemFromBlock(Blocks.quartz_block)
		};
		
		int primaryIndex = (sizeX / 3 * sizeY / 3) / 4;
		int secondaryIndex = ((sizeX / 3 * sizeY / 3) / 4);
		
		int chestAmount = (sizeX * sizeY) / 27 == 0 ? 1 : (sizeX * sizeY) / 27;
		int clayAmount = (sizeX * sizeY) / 3 == 0 ? 1 : (sizeX * sizeY) / 3;
		
		list.add(new ItemStack(Blocks.chest, chestAmount));
		list.add(new ItemStack(primary[primaryIndex > 3 ? 3 : primaryIndex], primaryIndex + 1 * 2, OreDictionary.WILDCARD_VALUE));
		list.add(new ItemStack(secondary[secondaryIndex > 3 ? 3 : primaryIndex], secondaryIndex + 1 * 2, (Block.getBlockFromItem(secondary[secondaryIndex > 3 ? 3 : primaryIndex]).equals(Blocks.quartz_block) ? 2 : 0)));
		list.add(new ItemStack(Items.clay_ball, clayAmount));
		
		return list;
	}
	
	public static NBTTagCompound writeToNBT(StorageInfo info)
	{
		if(info != null)
		{
			NBTTagCompound tag = new NBTTagCompound();
			
			tag.setInteger("sizeX", info.getSizeX());
			tag.setInteger("sizeY", info.getSizeY());
		
			return tag;
		}
		
		return null;
	}
	
	public static StorageInfo readFromNBT(NBTTagCompound tag)
	{
		if(tag == null)
		{
			return new StorageInfo(9, 3);
		}
		
		StorageInfo info = new StorageInfo(tag.getInteger("sizeX"), tag.getInteger("sizeY"));
		return info;
	}
}
