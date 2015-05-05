package com.workshop.compactchests.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.workshop.compactchests.itementity.ItemEntityChest;

/**
 * Created by Toby on 19/08/2014.
 */
public abstract class ContainerChest extends Container
{
    public EntityPlayer player;
    public World world;

    public int x;
    public int y;
    public int z;

    public IInventory chest;
    public boolean item;

    public int lastID = 0;

    public int xSize;
    public int zSize;

    public ContainerChest(EntityPlayer player, World world, int x, int y, int z, boolean item, int xSize, int zSize, int xStart, int zStart, int xInvStart, int zInvStart, int xHotStart, int zHotStart)
    {
        this.player = player;
        this.world = world;

        this.x = x;
        this.y = y;
        this.z = z;

        this.zSize = zSize;
        this.xSize = xSize;
        
        this.item = item;

        if(!item)
        {
            ((IInventory) world.getTileEntity(x, y, z)).openInventory();

            chest = (IInventory) world.getTileEntity(x, y, z);

            initializeContainer(player, (IInventory) world.getTileEntity(x, y, z), xSize, zSize, xStart, zStart, xInvStart, zInvStart, xHotStart, zHotStart);
        }
        else
        {
            ItemStack stack = player.getHeldItem();
            
            chest = (IInventory) new ItemEntityChest(8, stack);
            chest.openInventory();

            initializeContainer(player, chest, xSize, zSize, xStart, zStart, xInvStart, zInvStart, xHotStart, zHotStart);
        }
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
    {
    	try
    	{
    		Slot slot = (Slot) inventorySlots.get(slotIndex);
    		
    		if (slot != null && slot.getHasStack())
    		{
    		    ItemStack itemStack1 = slot.getStack();
    			ItemStack itemStack = itemStack1.copy();
    			
    			if (slotIndex < 36)
    			{
    				if (!this.mergeItemStack(itemStack1, 36, xSize * zSize + 36, false))
    				{
    					return null;
    				}
    			}
    			else if (!this.mergeItemStack(itemStack1, 0, 36, false))
    			{
    				return null;
    			}
    			
    			if (itemStack1.stackSize == 0)
    			{
    				slot.putStack(null);
    			}
    			else
    			{
    				slot.onSlotChanged();
    			}
    			return itemStack;
    		}
    		
    		return null;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		return null;
    	}
    }


    @Override
    public void onContainerClosed(EntityPlayer player)
    {
        super.onContainerClosed(player);
        chest.closeInventory();
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }

    public void initializeContainer(EntityPlayer player, IInventory inventory, int xSize, int zSize, int xStart, int zStart, int xInvStart, int zInvStart, int xHotStart, int zHotStart)
    {
        for(int slot = 0; slot < 9; slot++)
        {
            Slot s = new Slot(player.inventory, slot, xHotStart + (slot * 18), zHotStart);

            addSlotToContainer(s);
        }

        for(int x = 0; x < 9; x++)
        {
            for(int y = 0; y < 3; y++)
            {
                Slot s = new Slot(player.inventory, x + y * 9 + 9, xInvStart + (x * 18), zInvStart + (y * 18));
                addSlotToContainer(s);
            }
        }

        int id = 0;

        for(int z = 0; z < zSize; z++)
        {
            for(int x = 0; x < xSize; x++)
            {
                Slot slot = null;

                boolean problem = false;

                if(x >= 7 && xSize == 15)
                {
                    slot = new Slot(chest, id, xStart + (x * 18) - 1, zStart + (z * 18));
                    problem = true;
                }
                else if(x >= 4 && xSize == 12)
                {
                    slot = new Slot(chest, id, xStart + (x * 18) - 1, zStart + (z * 18));
                    problem = true;
                }
                else if(x >= 10 && xSize == 18)
                {
                    slot = new Slot(chest, id, xStart + (x * 18) - 1, zStart + (z * 18));
                    problem = true;
                }

                if(!problem) slot = new Slot(chest, id, xStart + (x * 18), zStart + (z * 18));

                if(slot != null) addSlotToContainer(slot);

                id++;
            }
        }

        this.lastID = id;
    }
    
    @Override
    public ItemStack slotClick(int slot, int button, int flag, EntityPlayer player) 
    {
	    if(item)
	    {
	    	if (slot >= 0 && getSlot(slot) != null && getSlot(slot).getStack() == player.getHeldItem()) 
		    {
		    	return null;
		    }
	    }
	    
	    return super.slotClick(slot, button, flag, player);
    }
}
