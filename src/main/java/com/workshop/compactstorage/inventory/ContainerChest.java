package com.workshop.compactstorage.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.workshop.compactstorage.tileentity.TileEntityChest;
import com.workshop.compactstorage.util.BlockPos;

/**
 * Created by Toby on 11/11/2014.
 */
public class ContainerChest extends Container
{
    public World world;
    public EntityPlayer player;
    public BlockPos pos;

    public TileEntityChest chest;
    
    public int invX;
    public int invY;
    
    public int lastId;
    
    /***
     * This is carried over from the GUI for slot placement issues
     */
    public int xSize;
    public int ySize;
    
    public ContainerChest(World world, EntityPlayer player, BlockPos pos)
    {
        super();

        this.world = world;
        this.player = player;
        this.pos = pos;
        this.chest = ((TileEntityChest) world.getTileEntity(pos.getX(), pos.getY(), pos.getZ()));
        
        this.invX = chest.invX;
        this.invY = chest.invY;
        this.xSize = 7 + (invX < 9 ? (9 * 18) : (invX * 18)) + 7;
        this.ySize = 7 + (invY * 18) + 13 + 54 + 4 + 18 + 7;
        
        setupSlots();
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }
    
    public void setupSlots()
    {
    	int slotX = (xSize / 2) - (invX * 18 / 2) + 1; 
        int slotY = 8; //(ySize / 2) - ((invY * 18) / 2);

        int lastId = 0;
        
        for(int y = 0; y < invY; y++)
        {
        	for(int x = 0; x < invX; x++)
            {
                Slot slot = new Slot(chest, lastId, slotX + (x * 18), slotY + (y * 18));
                addSlotToContainer(slot);
                lastId++;
            }
        }
        
        this.lastId = lastId;

        slotX = (xSize / 2) - ((9 * 18) / 2) + 1;
        slotY = slotY + (invY * 18) + 13;

        for(int x = 0; x < 9; x++)
        {
            for(int y = 0; y < 3; y++)
            {
            	Slot slot = new Slot(player.inventory, x + y * 9 + 9, slotX + (x * 18), slotY + (y * 18));
                addSlotToContainer(slot);
            }
        }

        slotY = slotY + (3 * 18) + 4;

        for(int x = 0; x < 9; x++)
        {
        	Slot slot = new Slot(player.inventory, x, slotX + (x * 18), slotY);
            addSlotToContainer(slot);
        }
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
    {
    	Slot slot = (Slot) inventorySlots.get(slotIndex);
    	
    	try
    	{
    		if (slot != null && slot.getHasStack())
    		{
    			ItemStack itemStack = slot.getStack().copy();
    			
    			if (slotIndex < 36)
    			{
    				if (!this.mergeItemStack(itemStack, 36, xSize * ySize + 36, false))
    				{
    					return null;
    				}
    			}
    			else if (!this.mergeItemStack(itemStack, 0, 36, false))
    			{
    				return null;
    			}
    			
    			if (itemStack.stackSize == 0)
    			{
    				slot.putStack(null);
    			}
    			else
    			{
    				slot.onSlotChanged();
    			}
    		}
    		
    		slot.onSlotChanged();
    		
    		return null;
    	}
    	catch(Exception e)
    	{
    		slot.onSlotChanged();
    	}
    	
    	chest.markDirty();
    	player.inventory.inventoryChanged = true;
    	
    	return null;
    }
}
