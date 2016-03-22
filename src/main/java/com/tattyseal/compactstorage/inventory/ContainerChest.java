package com.tattyseal.compactstorage.inventory;

import com.tattyseal.compactstorage.api.IChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Toby on 11/11/2014.
 */
public class ContainerChest extends Container
{
    public World world;
    public EntityPlayer player;
    public BlockPos pos;

    public IChest chest;
    
    public int invX;
    public int invY;
    
    public int lastId;
    public int backpackSlot;
    
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
        
        backpackSlot = -1;
        if(chest instanceof InventoryBackpack)
	    {
        	backpackSlot = player.inventory.currentItem;
	    }
        
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
        	boolean immovable = false;
                	
        	if(backpackSlot != -1 && backpackSlot == x)
        	{
        		immovable = true;
        	}
        	
        	SlotImmovable slot = new SlotImmovable(player.inventory, x, slotX + (x * 18), slotY, immovable);
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
    		    ItemStack itemStack1 = slot.getStack();
    			ItemStack itemStack = itemStack1.copy();
    			
    			if (slotIndex < lastId)
    			{
    				if (!this.mergeItemStack(itemStack1, lastId, lastId + 36, false))
    				{
    					return null;
    				}
    			}
    			else if (!this.mergeItemStack(itemStack1, 0, lastId, false))
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
        chest.closeInventory(player);

        if(!world.isRemote)
        {
            world.playSound((EntityPlayer)null, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, SoundEvents.block_chest_close, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        }

        super.onContainerClosed(player);
    }
    @Override
    public ItemStack func_184996_a(int slot, int button, ClickType flag, EntityPlayer player)
    {
        if(chest instanceof InventoryBackpack)
        {
            if (slot >= 0 && getSlot(slot) != null && getSlot(slot).getStack() == player.getHeldItem(EnumHand.MAIN_HAND))
            {
                return null;
            }
        }

        return super.func_184996_a(slot, button, flag, player);
    }


}
