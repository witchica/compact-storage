package com.tattyseal.compactstorage.tileentity;

import java.util.List;

import com.tattyseal.compactstorage.util.StorageInfo;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityChestBuilder extends TileEntity {

	protected StorageInfo info = new StorageInfo(9, 3, 180, StorageInfo.Type.CHEST);
	protected ItemHandler items = new ItemHandler(5);

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = new NBTTagCompound();
		tag = writeToNBT(tag);

		return tag;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setTag("info", info.serialize());
		tag.setTag("items", items.serializeNBT());
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		info.deserialize(tag.getCompoundTag("info"));
		items.deserializeNBT(tag.getCompoundTag("items"));
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound tag = getUpdateTag();

		return new SPacketUpdateTileEntity(pos, 1, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		readFromNBT(pkt.getNbtCompound());
	}

	public StorageInfo getInfo() {
		return info;
	}

	public ItemHandler getItems() {
		return items;
	}

	public class ItemHandler extends ItemStackHandler {

		public ItemHandler(int size) {
			super(size);
		}

		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			if (info == null) return false;
			List<ItemStack> cost = info.getMaterialCost();
			return cost.size() > slot && stack.getItem() == cost.get(slot).getItem();
		}
	}
}
