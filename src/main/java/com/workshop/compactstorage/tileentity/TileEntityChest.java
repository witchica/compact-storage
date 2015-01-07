package com.workshop.compactstorage.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.core.util.SocialRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;

/**
 * Created by Toby on 06/11/2014.
 */
@Optional.Interface(iface = "cofh.api.tileentity.ISecurable", modid = "CoFHCore")
public class TileEntityChest extends TileEntity implements IInventory, cofh.api.tileentity.ISecurable
{
    public ForgeDirection direction;
    public AccessMode mode;
    public String player;

    public int color;
    public int invX;
    public int invY;

    public boolean init;
    
    public ItemStack[] items;

    public TileEntityChest()
    {
        super();

        this.direction = ForgeDirection.NORTH;
    }

    /* INVENTORY START */
    @Override
    public int getSizeInventory() 
    {
        return invX * invY + 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) 
    {
        return items == null ? null : items[slot];
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
        return getStackInSlot(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) 
    {
    	if(items != null && stack != null)
    	{
    		items[slot] = stack;
    	}
    }

    @Override
    public String getInventoryName()
    {
        return "compactChest.inv";
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
        return true;
    }
    
    /* CUSTOM START */
    
    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        this.direction = ForgeDirection.getOrientation(tag.getInteger("facing"));

        this.color = tag.getInteger("color");
        this.invX = tag.getInteger("invX");
        this.invY = tag.getInteger("invY");
        
        this.mode = AccessMode.values()[tag.getInteger("mode")];
        this.player = tag.getString("player");
        
        this.items = new ItemStack[getSizeInventory()];
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        tag.setInteger("facing", direction.ordinal());
        tag.setInteger("color", color);
        tag.setInteger("invX", invX);
        tag.setInteger("invY", invY);
        
        tag.setInteger("mode", mode.ordinal());
        tag.setString("player", player);
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, getBlockMetadata(), tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.func_148857_g());
    }

    public void setDirection(ForgeDirection direction)
    {
        this.direction = direction;
        updateBlock();
    }

    public void setDirection(int direction)
    {
        this.direction = ForgeDirection.getOrientation(direction);
        updateBlock();
    }

    public void updateBlock()
    {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    /* COFH CORE START */

    @Optional.Method(modid = "CoFHCore")
	@Override
	public boolean canPlayerAccess(String name) 
	{
		switch(mode)
		{
			case PUBLIC: return true;
			case PRIVATE: return name.equals(player);
			case RESTRICTED: return SocialRegistry.playerHasAccess(name, player);
			default: return false;
		}
	}

    @Optional.Method(modid = "CoFHCore")
    @Override
	public AccessMode getAccess() 
	{
		return mode;
	}

    @Optional.Method(modid = "CoFHCore")
    @Override
	public String getOwnerName() 
	{
		return player;
	}

    @Optional.Method(modid = "CoFHCore")
    @Override
	public boolean setAccess(AccessMode mode) 
	{
    	this.mode = mode;
		return true;
	}

    @Optional.Method(modid = "CoFHCore")
    @Override
	public boolean setOwnerName(String name) 
	{
    	this.player = name;
		return true;
	}
}
