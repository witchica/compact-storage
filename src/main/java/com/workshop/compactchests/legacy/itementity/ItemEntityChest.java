package com.workshop.compactchests.legacy.itementity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

/**
 * Created by Toby on 19/08/2014.
 */
public class ItemEntityChest implements IInventory
{
    public ItemStack[] items;

    public int size;

    public float lidAngle;
    public float prevLidAngle;
    public int numPlayersUsing;

    public ItemStack stack;

    public ItemEntityChest(int size, ItemStack stack)
    {
        super();

        this.size = size;

        items = new ItemStack[getSizeInventory()];

        if(stack.stackTagCompound == null) stack.stackTagCompound = new NBTTagCompound();

        readFromNBT(stack.getTagCompound());

        this.stack = stack;
    }

    public ItemEntityChest()
    {
        super();
        items = new ItemStack[getSizeInventory()];
    }

    @Override
    public int getSizeInventory()
    {
        return 27 * size;
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
            }
            else
            {
                ItemStack stack2 = stack.splitStack(amount);
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
        items[slot] = stack;
    }

    @Override
    public String getInventoryName()
    {
        return "gui.chest" + size;
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
    public void markDirty()
    {

    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return true;
    }

    @Override
    public void openInventory()
    {

    }

    @Override
    public void closeInventory()
    {
        writeToNBT(stack.stackTagCompound);
    }

    public void writeToNBT(NBTTagCompound tag)
    {
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

    public void readFromNBT(NBTTagCompound tag)
    {
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
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        return true;
    }
}
