package com.tattyseal.compactstorage.tileentity;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingInventoryHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntitySortingChest extends TileEntityChest implements ISortingInventory, IFilterTileGUI
{
    public boolean isFirstRun = true;

    private IFilterGUI filter = APIUtils.createStandardFilter(this);

    private ISortingInventoryHandler sortingInventoryHandler = APIUtils.createSortingInventoryHandler(this);
    private ISortingInventory.Priority priority = ISortingInventory.Priority.NORMAL;

    @Override
    public void updateEntity()
    {
        if (isFirstRun)
        {
            sortingInventoryHandler.onTileAdded();
            isFirstRun = false;
        }
        super.updateEntity();
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        sortingInventoryHandler.setInventorySlotContents(i, itemstack);
    }

    @Override
    public boolean putStackInSlot(ItemStack itemStack, int slotIndex)
    {
        if (slotIndex >= 0 && slotIndex < items.length)
        {
            items[slotIndex] = itemStack;
            return true;
        }
        return false;
    }

    @Override
    public ItemStack putInInventory(ItemStack itemStack, boolean simulate)
    {
        int emptySlot = -1;
        for (int slot = 0; slot < getSizeInventory() && itemStack != null && itemStack.stackSize > 0; ++slot)
        {
            if (isItemValidForSlot(slot, itemStack))
            {
                ItemStack itemstack1 = getStackInSlot(slot);

                if (itemstack1 == null)
                {
                    if (simulate)
                        return null;
                    if (emptySlot == -1)
                        emptySlot = slot;
                }
                else if (areItemStacksEqual(itemstack1, itemStack))
                {
                    int max = Math.min(itemStack.getMaxStackSize(), getInventoryStackLimit());
                    if (max > itemstack1.stackSize)
                    {
                        int l = Math.min(itemStack.stackSize, max - itemstack1.stackSize);
                        itemStack.stackSize -= l;
                        if (!simulate)
                        {
                            itemstack1.stackSize += l;
                            markDirty();
                        }
                    }
                }
            }
        }

        if (itemStack != null && itemStack.stackSize != 0 && emptySlot != -1)
        {
            items[emptySlot] = itemStack;
            itemStack = null;
            markDirty();
        }

        return itemStack;
    }

    /**
     * compares ItemStack argument to the instance ItemStack; returns true if both ItemStacks are equal
     */
    public static boolean areItemStacksEqual(ItemStack itemStack1, ItemStack itemStack2)
    {
        return itemStack1 == null && itemStack2 == null || (!(itemStack1 == null || itemStack2 == null) && (itemStack1.getItem() == itemStack2.getItem() && (itemStack1.getItemDamage() == itemStack2.getItemDamage() && (!(itemStack1.stackTagCompound == null && itemStack2.stackTagCompound != null) && (itemStack1.stackTagCompound == null || itemStack1.stackTagCompound.equals(itemStack2.stackTagCompound))))));
    }

    @Override
    public IFilterGUI getFilter()
    {
        return filter;
    }

    @Override
    public TileEntity getTileEntity()
    {
        return this;
    }

    @Override
    public void onFilterChanged()
    {
        this.markDirty();
    }

    @Override
    public Priority getPriority()
    {
        return priority;
    }

    @Override
    public void setPriority(Priority priority)
    {
        this.priority = priority;
    }

    @Override
    public ISortingInventoryHandler getHandler()
    {
        return sortingInventoryHandler;
    }

    @Override
    public void markDirty()
    {
        super.markDirty();
        getHandler().onInventoryChange();
    }


    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        filter.readFromNBT(compound);
        setPriority(Priority.values()[compound.getByte("priority")]);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        filter.writeToNBT(compound);
        compound.setByte("priority", (byte) priority.ordinal());
    }

    @Override
    public void invalidate()
    {
        sortingInventoryHandler.onTileRemoved();
        super.invalidate();
    }

    @Override
    public void onChunkUnload()
    {
        sortingInventoryHandler.onTileRemoved();
        super.onChunkUnload();
    }
}
