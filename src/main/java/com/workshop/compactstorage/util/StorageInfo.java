package com.workshop.compactstorage.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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

	public static enum Type
	{
		CHEST,
		BACKPACK;
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
	
	public ArrayList<List<ItemStack>> getMaterialCost(Type type)
	{
		ArrayList<List<ItemStack>> list = new ArrayList<List<ItemStack>>();
		
		/* Max 12 */
		
		String[] primary = new String[] 
		{
				"ingotIron", 
				"ingotGold",
				"gemDiamond", 
				"gemEmerald"
		};
		
		String[] secondary = new String[] 
		{
			"logWood", 
			"blockCoal", 
			"barsIron", 
			"blockQuartz"
		};
		
		int primaryIndex = (sizeX / 3 * sizeY / 3) / 4;
		int secondaryIndex = ((sizeX / 3 * sizeY / 3) / 4);
		
		int storageAmount = (sizeX * sizeY) / 27 == 0 ? 1 : (sizeX * sizeY) / 27;
		int binderAmount = (int) ((int) (sizeX * sizeY) / 3 == 0 ? 1 : (sizeX * sizeY) / 4.5f);

		String storageName = "blockChest";
		String binderName = "itemClay";

		if(type.equals(Type.BACKPACK))
		{
			storageAmount = (int) (storageAmount * 1.5f);
			binderAmount = (int) (binderAmount / 1.5f);

			storageName = "wool";
			binderName=  "string";
		}
		
		list.add(changeAmounts(OreDictionary.getOres(storageName), storageAmount));
		list.add(changeAmounts(OreDictionary.getOres(primary[primaryIndex > 3 ? 3 : primaryIndex]), primaryIndex + 1 * 2));
		list.add(changeAmounts(OreDictionary.getOres(secondary[secondaryIndex > 3 ? 3 : primaryIndex]), secondaryIndex + 1 * 2));
		list.add(changeAmounts(OreDictionary.getOres(binderName), binderAmount));
		
		return list;
	}
	
	public List<ItemStack> changeAmounts(List<ItemStack> list, int amount)
	{
		List<ItemStack> newList = new ArrayList<ItemStack>();
		
		for(ItemStack stack : list)
		{
			stack.stackSize = amount;
			newList.add(stack);
		}
		
		return newList;
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
