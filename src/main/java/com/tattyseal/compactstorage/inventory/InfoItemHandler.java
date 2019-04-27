package com.tattyseal.compactstorage.inventory;

import com.tattyseal.compactstorage.util.StorageInfo;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;

public class InfoItemHandler extends ItemStackHandler {

	protected StorageInfo info;

	public InfoItemHandler(StorageInfo info) {
		super(info.getSizeX() * info.getSizeY());
		this.info = info;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = super.serializeNBT();
		tag.removeTag("Size");
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		setSize(info.getSizeX() * info.getSizeY());
		super.deserializeNBT(nbt);
	}

}
