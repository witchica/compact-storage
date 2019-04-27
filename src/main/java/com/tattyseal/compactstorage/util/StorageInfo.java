package com.tattyseal.compactstorage.util;

import static com.tattyseal.compactstorage.ConfigurationHandler.binder;
import static com.tattyseal.compactstorage.ConfigurationHandler.binderBackpack;
import static com.tattyseal.compactstorage.ConfigurationHandler.binderModifier;
import static com.tattyseal.compactstorage.ConfigurationHandler.primary;
import static com.tattyseal.compactstorage.ConfigurationHandler.primaryModifier;
import static com.tattyseal.compactstorage.ConfigurationHandler.secondary;
import static com.tattyseal.compactstorage.ConfigurationHandler.secondaryModifier;
import static com.tattyseal.compactstorage.ConfigurationHandler.storage;
import static com.tattyseal.compactstorage.ConfigurationHandler.storageBackpack;
import static com.tattyseal.compactstorage.ConfigurationHandler.storageModifier;

import java.util.ArrayList;
import java.util.List;

import com.tattyseal.compactstorage.CompactStorage;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class StorageInfo {
	private int sizeX;
	private int sizeY;
	private int hue;
	private Type type;

	public StorageInfo(int sizeX, int sizeY, int hue, Type type) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.hue = hue;
		this.type = type;
	}

	public int getSizeX() {
		return sizeX;
	}

	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}

	public int getHue() {
		return hue;
	}

	public void setHue(int hue) {
		this.hue = hue;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public NBTTagCompound serialize() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setIntArray("data", new int[] { sizeX, sizeY, hue, type.ordinal() });
		return tag;
	}

	public void deserialize(NBTTagCompound tag) {
		int[] data = tag.getIntArray("data");
		if (data.length == 4) {
			sizeX = data[0];
			sizeY = data[1];
			hue = data[2];
			type = Type.values()[data[3]];
		}
	}

	public List<ItemStack> getMaterialCost() {
		Type type = getType();
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();

		/* STORAGE */

		int storageAmount = (int) (((sizeX * sizeY) / 8f) * storageModifier);
		list.add(changeStackSize(type.equals(Type.BACKPACK) ? storageBackpack : storage, type.equals(Type.BACKPACK) ? storageAmount : (storageAmount / 2)));

		/* PRIMARY */

		int amount = primary.length;
		int maxChest = 24 * 12;
		int thisChest = sizeX * sizeY;
		int divider = maxChest / amount;

		int primaryTier = 0;
		ItemStack primaryStack;

		for (int i = 0; i < amount; i++) {
			if (thisChest <= divider * (i + 1)) {
				primaryTier = i;
				break;
			}
		}

		primaryStack = primary[primaryTier];

		list.add(changeStackSize(primaryStack, (int) (((sizeX * sizeY) / 4.5f) / (primaryTier + 1) * primaryModifier)));

		/* SECONDARY */

		amount = secondary.length;
		divider = maxChest / amount;

		int secondaryTier = 0;
		ItemStack secondaryStack;

		for (int i = 0; i < amount; i++) {
			if (thisChest <= divider * (i + 1)) {
				secondaryTier = i;
				break;
			}
		}

		secondaryStack = secondary[secondaryTier];

		list.add(changeStackSize(secondaryStack, (int) (((sizeX * sizeY) / 4.5f) / (secondaryTier + 1) * secondaryModifier)));

		/* BINDER */

		int binderAmount = (int) (((sizeX * sizeY) / 8F) * binderModifier);
		list.add(changeStackSize(type.equals(Type.BACKPACK) ? binderBackpack : binder, binderAmount / 2));

		return list;
	}

	public ItemStack changeStackSize(ItemStack stack, int amount) {
		if (amount <= 0) amount = 1;

		stack.setCount(amount);

		return stack;
	}

	public static enum Type {
		CHEST("Chest", new ItemStack(CompactStorage.ModBlocks.chest, 1)),
		BACKPACK("Backpack", new ItemStack(CompactStorage.ModItems.backpack, 1));

		public String name;
		public ItemStack display;

		Type(String name, ItemStack display) {
			this.name = name;
			this.display = display;
		}
	}
}
