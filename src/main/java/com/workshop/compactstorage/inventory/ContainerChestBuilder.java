package com.workshop.compactstorage.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.workshop.compactstorage.inventory.slot.SlotChestBuilder;
import com.workshop.compactstorage.tileentity.TileEntityChestBuilder;
import com.workshop.compactstorage.util.BlockPos;
import com.workshop.compactstorage.util.StorageInfo;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Created by Toby on 11/11/2014.
 */
public class ContainerChestBuilder extends Container
{
    public World world;
    public EntityPlayer player;
    public BlockPos pos;

    public TileEntityChestBuilder chest;
    
    /***
     * This is carried over from the GUI for slot placement issues
     */
    public int xSize;
    public int ySize;
    
    public ContainerChestBuilder(World world, EntityPlayer player, BlockPos pos)
    {
        super();

        this.world = world;
        this.player = player;
        this.pos = pos;
        this.chest = ((TileEntityChestBuilder) world.getTileEntity(pos.getX(), pos.getY(), pos.getZ()));
        
        this.xSize = 7 + 162 + 7;
        this.ySize = 7 + 54 + 13 + 54 + 4 + 18 + 7;
        
        setupSlots();
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }
    
    public void setupSlots()
    {
    	int slotX = (xSize / 2) - (162 / 2) + 1;
        int slotY = 8; //(ySize / 2) - ((invY * 18) / 2);

        for(int x = 0; x < 4; x++)
        {
        	SlotChestBuilder slot = new SlotChestBuilder(chest, x, 7 + (x * 18) + 1, 7 + (18 * 2) + 1);
            addSlotToContainer(slot);
        }
        
        slotX = (xSize / 2) - ((9 * 18) / 2) + 1;
        slotY = slotY + 54 + 13;

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
    	
    	//chest.markDirty();
    	player.inventory.inventoryChanged = true;
    	
    	return null;
    }
    
    @Override
    public void addCraftingToCrafters(ICrafting crafter) 
    {
    	super.addCraftingToCrafters(crafter);
    	crafter.sendProgressBarUpdate(this, 0, 9);
    	crafter.sendProgressBarUpdate(this, 1, 3);
    }
    
    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i)
        {
            ICrafting crafter = (ICrafting)this.crafters.get(i);
            if(chest != null && chest.info != null) crafter.sendProgressBarUpdate(this, 0, chest.info.getSizeX());
            if(chest != null && chest.info != null) crafter.sendProgressBarUpdate(this, 1, chest.info.getSizeY());
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int id, int value)
    {
    	if(chest.info == null) chest.info = new StorageInfo(9, 3);
    	
        switch(id)
        {
        	case 0: chest.info.setSizeX(value); break;
        	case 1: chest.info.setSizeY(value); break;
        }
    }
}
