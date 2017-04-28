package com.tattyseal.compactstorage.inventory;

import com.tattyseal.compactstorage.inventory.slot.SlotChestBuilder;
import com.tattyseal.compactstorage.tileentity.TileEntityChestBuilder;
import com.tattyseal.compactstorage.util.StorageInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
        this.chest = ((TileEntityChestBuilder) world.getTileEntity(pos));
        
        this.xSize = 7 + 162 + 7;
        this.ySize = 7 + 108 + 13 + 54 + 4 + 18 + 7;
        
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
        slotY = slotY + 108 + 13;

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
                ItemStack itemStack1 = slot.getStack();
                ItemStack itemStack = itemStack1.copy();

                if (slotIndex < 4)
                {
                    if (!this.mergeItemStack(itemStack1, 4, 4 + 36, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (!this.mergeItemStack(itemStack1, 0, 4, false))
                {
                    return ItemStack.EMPTY;
                }

                if (itemStack1.getCount() == 0)
                {
                    slot.putStack(ItemStack.EMPTY);
                }
                else
                {
                    slot.onSlotChanged();
                }
                return itemStack;
            }

            return ItemStack.EMPTY;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendProgressBarUpdate(this, 0, 9);
        listener.sendProgressBarUpdate(this, 1, 3);
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener crafter = (IContainerListener)this.listeners.get(i);
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

    /**
     * merges provided ItemStack with the first avaliable one in the container/player inventory
     */
    protected boolean mergeItemStack2(ItemStack p_75135_1_, int p_75135_2_, int p_75135_3_, boolean p_75135_4_)
    {
        boolean flag1 = false;
        int k = p_75135_2_;

        if (p_75135_4_)
        {
            k = p_75135_3_ - 1;
        }

        Slot slot;
        ItemStack itemstack1;

        if (p_75135_1_.isStackable())
        {
            while (p_75135_1_.getCount() > 0 && (!p_75135_4_ && k < p_75135_3_ || p_75135_4_ && k >= p_75135_2_))
            {
                slot = (Slot)this.inventorySlots.get(k);
                itemstack1 = slot.getStack();

                if (itemstack1 != ItemStack.EMPTY && itemstack1.getItem() == p_75135_1_.getItem() && (!p_75135_1_.getHasSubtypes() || p_75135_1_.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(p_75135_1_, itemstack1) && slot.isItemValid(p_75135_1_))
                {
                    int l = itemstack1.getCount() + p_75135_1_.getCount();

                    if (l <= p_75135_1_.getMaxStackSize())
                    {
                        p_75135_1_.setCount(0);
                        itemstack1.setCount(l);
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                    else if (itemstack1.getCount() < p_75135_1_.getMaxStackSize())
                    {
                        p_75135_1_.setCount(p_75135_1_.getCount() - p_75135_1_.getMaxStackSize() - itemstack1.getCount());
                        itemstack1.setCount(p_75135_1_.getMaxStackSize());
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                }

                if (p_75135_4_)
                {
                    --k;
                }
                else
                {
                    ++k;
                }
            }
        }

        if (p_75135_1_.getCount() > 0)
        {
            if (p_75135_4_)
            {
                k = p_75135_3_ - 1;
            }
            else
            {
                k = p_75135_2_;
            }

            while (!p_75135_4_ && k < p_75135_3_ || p_75135_4_ && k >= p_75135_2_)
            {
                slot = (Slot)this.inventorySlots.get(k);
                itemstack1 = slot.getStack();

                if (itemstack1.isEmpty() && slot.isItemValid(p_75135_1_))
                {
                    slot.putStack(p_75135_1_.copy());
                    slot.onSlotChanged();
                    p_75135_1_.setCount(0);
                    flag1 = true;
                    break;
                }

                if (p_75135_4_)
                {
                    --k;
                }
                else
                {
                    ++k;
                }
            }
        }

        return flag1;
    }
}
