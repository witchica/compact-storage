package com.workshop.compactstorage.tileentity;

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
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.Loader;

/**
 * Created by Toby on 06/11/2014.
 */
public class TileEntityChest extends TileEntity implements IInventory
{
    public ForgeDirection direction;

    public int color;
    public int invX;
    public int invY;

    public boolean init;
    
    public ItemStack[] items;

    public TileEntityChest()
    {
        super();

        this.direction = ForgeDirection.NORTH;
        this.items = new ItemStack[getSizeInventory()];
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
    	if(slot < items.length && items[slot] != null)
        {
        	return items[slot];
        }
        
        return null;
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
                
                return stack.copy();
            }
            else
            {
                ItemStack stack2 = stack.splitStack(amount);
                markDirty();
                
                return stack2.copy();
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
    	if(items != null)
    	{
    		items[slot] = stack;
    		markDirty();
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

        if(tag.hasKey("facing")) this.direction = ForgeDirection.getOrientation(tag.getInteger("facing"));

        this.color = tag.getInteger("color");
        this.invX = tag.getInteger("invX");
        this.invY = tag.getInteger("invY");
        
        NBTTagList nbtTagList = tag.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        items = new ItemStack[getSizeInventory()];

        for(int slot = 0; slot < nbtTagList.tagCount(); slot++)
        {
            NBTTagCompound item = nbtTagList.getCompoundTagAt(slot);

            int i = item.getInteger("Slot");

            if(i >= 0 && i < getSizeInventory())
            {
                items[i] = ItemStack.loadItemStackFromNBT(item);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        if(direction != null) tag.setInteger("facing", direction.ordinal());
        tag.setInteger("color", color);
        tag.setInteger("invX", invX);
        tag.setInteger("invY", invY);
        
        NBTTagList nbtTagList = new NBTTagList();
        for(int slot = 0; slot < getSizeInventory(); slot++)
        {
            if(slot < items.length && items[slot] != null)
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
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, getBlockMetadata(), tag);
    }
    
    @Override
    public void updateEntity()
    {
    	super.updateEntity();
    	
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
        //worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
}
