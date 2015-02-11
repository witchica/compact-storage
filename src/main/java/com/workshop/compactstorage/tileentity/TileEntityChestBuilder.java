package com.workshop.compactstorage.tileentity;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

import com.workshop.compactstorage.util.StorageInfo;

import cpw.mods.fml.common.Loader;

public class TileEntityChestBuilder extends TileEntity implements IInventory
{
	public StorageInfo info;
	public int dimension;
	
	public boolean init;
	
	public ItemStack[] items;
	
	public int mode;
	public String player;

	public StorageInfo.Type type;

	public TileEntityChestBuilder()
	{
		init = false;
		info = new StorageInfo(9, 3);
		items = new ItemStack[getSizeInventory()];
		type = StorageInfo.Type.CHEST;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) 
	{
		super.writeToNBT(tag);
		
		tag.setInteger("infoX", info.getSizeX());
		tag.setInteger("infoY", info.getSizeY());
		
		tag.setInteger("mode", mode);
		tag.setString("player", player);
		
		NBTTagList nbtTagList = new NBTTagList();
        for(int slot = 0; slot < getSizeInventory(); slot++)
        {
            if(items[slot] != null)
            {
                NBTTagCompound item = new NBTTagCompound();
                item.setInteger("Slot", slot);
                items[slot].writeToNBT(item);
                nbtTagList.appendTag(item);
            }
        }

        tag.setTag("Items", nbtTagList);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) 
	{
		super.readFromNBT(tag);
		
		this.info = new StorageInfo(tag.getInteger("infoX"), tag.getInteger("infoY"));
		
		this.mode = tag.getInteger("mode");
		this.player = tag.getString("player");
		
		NBTTagList nbtTagList = tag.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        items = new ItemStack[getSizeInventory()];

        for(int slot = 0; slot < getSizeInventory(); slot++)
        {
            NBTTagCompound item = nbtTagList.getCompoundTagAt(slot);
            items[slot] = ItemStack.loadItemStackFromNBT(item);
        }
        
        markDirty();
	}
	
	@Override
	public void updateEntity() 
	{
		super.updateEntity();
		
		if(!init)
		{
			dimension = getWorldObj().provider.dimensionId;
			
			init = true;
		}
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) 
	{
		super.onDataPacket(net, pkt);
		readFromNBT(pkt.func_148857_g());
	}

    /* INVENTORY START */
    
	@Override
	public int getSizeInventory() 
	{
		return 4;
	}

	@Override
	public ItemStack getStackInSlot(int slot) 
	{
		return items[slot];
	}

	@Override
    public ItemStack decrStackSize(int slot, int amount)
    {
    	ItemStack stack = getStackInSlot(slot);

        if(stack != null)
        {
            if(stack.stackSize <= amount)
            {
                setInventorySlotContents(slot, null);
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
	public ItemStack getStackInSlotOnClosing(int slot) 
	{
		return items[slot];
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) 
	{
		items[slot] = stack;
	}

	@Override
	public String getInventoryName() 
	{
		return "chestBuilder";
	}

	@Override
	public boolean hasCustomInventoryName() 
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit() 
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) 
	{
		if(Loader.isModLoaded("CoFHCore"))
		{
			return canPlayerAccess(player.getCommandSenderName());
		}
		
		return true;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) 
	{
		if(info != null)
		{
			List<ItemStack> stack2 = info.getMaterialCost(type).get(slot);
			
			for(ItemStack stack3 : stack2)
			{
				if(stack3.getItem().equals(stack.getItem()))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
    /* COFH CORE START */

    public boolean canPlayerAccess(String name) 
	{    	
		switch(mode)
		{
			case 0: return true;
			case 1: return name.equals(player);
			case 2: return name.equals(player); //SocialRegistry.playerHasAccess(name, player);
			default: return false;
		}
	}

    public int getAccess() 
	{
		return mode;
	}

    public String getOwnerName() 
	{
		return player;
	}

    public boolean setAccess(int mode) 
	{
    	this.mode = mode;
		return true;
	}

    public boolean setOwnerName(String name) 
	{
    	this.player = name;
		return true;
	}
}
