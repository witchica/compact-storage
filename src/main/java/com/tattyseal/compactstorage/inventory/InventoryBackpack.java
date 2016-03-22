package com.tattyseal.compactstorage.inventory;

import com.tattyseal.compactstorage.api.IChest;
import com.tattyseal.compactstorage.util.StorageInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.Constants;

/**
 * Created by Toby on 11/02/2015.
 */
public class InventoryBackpack implements IChest
{
    public ItemStack stack;
    public int[] size;

    public ItemStack[] items;

    public InventoryBackpack(ItemStack stack)
    {
        this.stack = stack;

        if(stack.hasTagCompound() && stack.getTagCompound().hasKey("size"))
        {
            this.size = stack.getTagCompound().getIntArray("size");
        }
        else
        {
            this.size = new int[] {9, 3};
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setIntArray("size", new int[] {9, 3});
        }

        items = new ItemStack[getSizeInventory()];
        readFromNBT(this.stack.getTagCompound());
    }

    @Override
    public int getInvX()
    {
        return size[0];
    }

    @Override
    public int getInvY()
    {
        return size[1];
    }

    @Override
    public StorageInfo getInfo()
    {
        return new StorageInfo(getInvX(), getInvY());
    }

    @Override
    public int getColor()
    {
        return stack.getItem().getColorFromItemStack(stack, 0);
    }

    /* INVENTORY START */
    @Override
    public int getSizeInventory()
    {
        return getInvX() * getInvY() + 1;
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
    public ItemStack removeStackFromSlot(int index) {
        return null;
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
    public String getName()
    {
        return "backpack.inv";
    }

    @Override
    public boolean hasCustomName()
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
        writeToNBT(stack.getTagCompound());
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {
        writeToNBT(stack.getTagCompound());
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    /** CUSTOM START **/

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

    public void writeToNBT(NBTTagCompound tag)
    {
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
    public boolean shouldConnectToNetwork()
    {
        return false;
    }

    @Override
    public IChatComponent getDisplayName() {
        return null;
    }
}
