package com.tattyseal.compactstorage.tileentity;

import java.util.List;

import javax.annotation.Nonnull;

import com.tattyseal.compactstorage.util.StorageInfo;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.util.Constants;

public class TileEntityChestBuilder extends TileEntity implements IInventory, ITickable
{
	public StorageInfo info;
	public int dimension;

	public boolean init;

	public ItemStack[] items;

	private String customName;

	public int mode;
	public String player;

	public TileEntityChestBuilder()
	{
		init = false;
		info = new StorageInfo(9, 3, 180, StorageInfo.Type.CHEST);
		items = new ItemStack[getSizeInventory()];

		for(int i = 0; i < items.length; i++)
		{
			items[i] = ItemStack.EMPTY;
		}
	}

	@Override
	@Nonnull
	public NBTTagCompound getTileData()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag = writeToNBT(tag);

		return tag;
	}

	@Override
	@Nonnull
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag = writeToNBT(tag);

		return tag;
	}

	@Override
	@Nonnull
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag = writeToNBT(tag);

		return tag;
	}

	@Override
	@Nonnull
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);

		tag.setInteger("infoX", info.getSizeX());
		tag.setInteger("infoY", info.getSizeY());
		tag.setInteger("infoHue", info.getHue());
		tag.setInteger("type", info.getType().ordinal());

		NBTTagList nbtTagList = new NBTTagList();
        for(int slot = 0; slot < getSizeInventory(); slot++)
        {
            if(items != null && items[slot] != null && items[slot] != ItemStack.EMPTY)
            {
                NBTTagCompound item = new NBTTagCompound();
                item.setInteger("Slot", slot);
                items[slot].writeToNBT(item);
                nbtTagList.appendTag(item);
            }
        }

        tag.setTag("Items", nbtTagList);

		if (this.hasCustomName()) {
			tag.setString("Name", this.customName);
		}

		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);

		this.info = new StorageInfo(tag.getInteger("infoX"), tag.getInteger("infoY"), tag.getInteger("hue"), StorageInfo.Type.values()[tag.getInteger("type")]);

		this.mode = tag.getInteger("mode");
		this.player = tag.getString("player");

		NBTTagList nbtTagList = tag.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        items = new ItemStack[getSizeInventory()];

        for(int slot = 0; slot < getSizeInventory(); slot++)
        {
            NBTTagCompound item = nbtTagList.getCompoundTagAt(slot);
            items[slot] = new ItemStack(item);
        }

		if (tag.hasKey("Name", 8)) {
			this.customName = tag.getString("Name");
		}

        markDirty();
	}

	@Override
	public void update()
	{
		if(!init)
		{
			dimension = getWorld().provider.getDimension();

			init = true;
		}
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound tag = getUpdateTag();

		return new SPacketUpdateTileEntity(pos, 1, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		super.onDataPacket(net, pkt);
		readFromNBT(pkt.getNbtCompound());
	}

    /* INVENTORY START */

	@Override
	public int getSizeInventory()
	{
		return 5;
	}

	@Override
	@Nonnull
	public ItemStack getStackInSlot(int slot)
	{
		return items[slot];
	}

	@Override
	@Nonnull
    public ItemStack decrStackSize(int slot, int amount)
    {
    	ItemStack stack = getStackInSlot(slot);

        if(stack != ItemStack.EMPTY)
        {
            if(stack.getCount() <= amount)
            {
                setInventorySlotContents(slot, ItemStack.EMPTY);
                markDirty();
            }
            else
            {
                ItemStack stack2 = stack.splitStack(amount);
                markDirty();

                return stack2;
            }
        }

        return stack;
    }

	@Override
	@Nonnull
	public ItemStack removeStackFromSlot(int index) {
		items[index] = ItemStack.EMPTY;
		return ItemStack.EMPTY;
	}

	@Override
	public void setInventorySlotContents(int slot, @Nonnull ItemStack stack)
	{
		items[slot] = stack;
	}

	@Override
	@Nonnull
	public String getName()
	{
		return this.hasCustomName() ? this.customName : "chestbuilder.json";
	}

	@Override
	public boolean hasCustomName()
	{
		return this.customName != null && !this.customName.isEmpty();
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	@Override
	public boolean isEmpty() { return true; }

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(@Nonnull EntityPlayer player) {
		return this.world.getTileEntity(this.pos) == this
				&& player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(@Nonnull EntityPlayer player) {}

	@Override
	public void closeInventory(@Nonnull EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int slot, @Nonnull ItemStack stack)
	{
		if(info != null)
		{
			List<ItemStack> cost = info.getMaterialCost();
			return cost.size() > slot && stack.getItem() == cost.get(slot).getItem();
		}

		return false;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {

	}
}
