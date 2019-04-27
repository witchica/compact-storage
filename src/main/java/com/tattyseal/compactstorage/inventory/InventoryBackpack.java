package com.tattyseal.compactstorage.inventory;

import com.tattyseal.compactstorage.api.IChest;
import com.tattyseal.compactstorage.util.StorageInfo;
import com.tattyseal.compactstorage.util.StorageInfo.Type;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Created by Toby on 11/02/2015.
 */
public class InventoryBackpack implements IChest {

	protected ItemStack backpack;
	protected StorageInfo info = new StorageInfo(0, 0, 0, Type.BACKPACK);
	protected InfoItemHandler items = new InfoItemHandler(info);

	public InventoryBackpack(ItemStack stack) {
		this.backpack = stack;
		readFromNBT(this.backpack.getOrCreateSubCompound("BlockEntityTag"));
	}

	@Override
	public int getInvX() {
		return info.getSizeX();
	}

	@Override
	public int getInvY() {
		return info.getSizeY();
	}

	@Override
	public StorageInfo getInfo() {
		return info;
	}

	@Override
	public void onOpened(EntityPlayer player) {
	}

	@Override
	public void onClosed(EntityPlayer player) {
		writeToNBT(backpack.getOrCreateSubCompound("BlockEntityTag"));
	}

	@Override
	public int getHue() {
		return info.getHue();
	}

	@Override
	public void setHue(int hue) {
		info.setHue(hue);
	}

	@Override
	public ItemStackHandler getItems() {
		return items;
	}

	private void writeToNBT(NBTTagCompound tag) {
		tag.setTag("info", info.serialize());
		tag.setTag("items", items.serializeNBT());
	}

	private void readFromNBT(NBTTagCompound tag) {
		this.info.deserialize(tag.getCompoundTag("info"));
		this.items.deserializeNBT(tag.getCompoundTag("items"));
	}
}
