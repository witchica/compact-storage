package com.workshop.compactstorage.inventory;

import com.workshop.compactchests.itementity.ItemEntityChest;
import com.workshop.compactstorage.api.IChest;
import com.workshop.compactstorage.util.BlockPos;
import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by Toby on 11/11/2014.
 */
@ChestContainer(showButtons = true, isLargeChest = false)
public class ContainerChest extends Container
{
    public World world;
    public EntityPlayer player;
    public BlockPos pos;

    public IChest chest;
    
    public int invX;
    public int invY;
    
    public int lastId;
    
    /***
     * This is carried over from the GUI for slot placement issues
     */
    public int xSize;
    public int ySize;
    
    public ContainerChest(World world, IChest chest, EntityPlayer player, BlockPos pos)
    {
        super();

        this.world = world;
        this.player = player;
        this.pos = pos;
        this.chest = chest;
        
        this.invX = chest.getInvX();
        this.invY = chest.getInvY();
        this.xSize = 7 + (invX < 9 ? (9 * 18) : (invX * 18)) + 7;
        this.ySize = 15 + (invY * 18) + 13 + 54 + 4 + 18 + 7;
        
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
        int slotY = 18; //(ySize / 2) - ((invY * 18) / 2);

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
    	try
    	{
    		Slot slot = (Slot) inventorySlots.get(slotIndex);
    		
    		if (slot != null && slot.getHasStack())
    		{
    			ItemStack itemStack = slot.getStack().copy();
    			
    			if (slotIndex < lastId)
    			{
    				if (!this.mergeItemStack(itemStack, lastId, lastId + 36, false))
    				{
    					return null;
    				}
    			}
    			else if (!this.mergeItemStack(itemStack, 0, lastId, false))
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
    		
    		return null;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		return null;
    	}
    }
    
    @ChestContainer.RowSizeCallback
    public int getInvX()
    {
    	return invX;
    }
    
    public int getInvY()
    {
    	return invY;
    }

    @Override
    public void onContainerClosed(EntityPlayer player)
    {
        chest.closeInventory();

        if(!world.isRemote)
        {
            world.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), "random.chestclosed", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        }

        super.onContainerClosed(player);
    }

    @Override
    public ItemStack slotClick(int slot, int button, int flag, EntityPlayer player)
    {
        if(chest instanceof InventoryBackpack)
        {
            if (slot >= 0 && getSlot(slot) != null && getSlot(slot).getStack() == player.getHeldItem())
            {
                return null;
            }
        }

        return super.slotClick(slot, button, flag, player);
    }
}
