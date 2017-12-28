package com.tattyseal.compactstorage.inventory;

import com.tattyseal.compactstorage.api.IChest;
import com.tattyseal.compactstorage.util.StorageInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Color;

/**
 * Created by Toby on 11/02/2015.
 */
public class InventoryBackpack implements IChest
{
    public ItemStack stack;
    public int[] size;

    public ItemStack[] items;

    public StorageInfo info;

    public InventoryBackpack(ItemStack stack)
    {
        this.stack = stack;

        if(stack.hasTagCompound() && stack.getTagCompound().hasKey("size"))
        {
            this.size = stack.getTagCompound().getIntArray("size");
            this.info = new StorageInfo(size[0], size[1], stack.getTagCompound().getInteger("hue"), StorageInfo.Type.BACKPACK);
        }
        else
        {
            this.size = new int[] {9, 3};
            this.info = new StorageInfo(size[0], size[1], 180, StorageInfo.Type.BACKPACK);
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setIntArray("size", new int[] {9, 3});
        }

        items = new ItemStack[getSizeInventory()];

        for(int i = 0; i < items.length; i++)
        {
            items[i] = ItemStack.EMPTY;
        }

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
        return info;
    }

    @Override
    public int getColor()
    {
        return 0xffffff;//stack.getItem().getColorFromItemStack(stack, 0);
    }

    /* INVENTORY START */
    @Override
    public int getSizeInventory()
    {
        return getInvX() * getInvY() + 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot)
    {
        if(slot < items.length && items[slot] != null && items[slot] != ItemStack.EMPTY)
        {
            return items[slot];
        }

        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public ItemStack decrStackSize(int slot, int amount)
    {
        ItemStack stack = getStackInSlot(slot);

        if(stack != ItemStack.EMPTY)
        {
            if(stack.getCount() <= amount)
            {
                setInventorySlotContents(slot, ItemStack.EMPTY);
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
    @Nonnull
    public ItemStack removeStackFromSlot(int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int slot, @Nonnull ItemStack stack)
    {
        items[slot] = stack;
        markDirty();
    }

    @Override
    @Nonnull
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
    @Nullable
    public ITextComponent getDisplayName() {
        return null;
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
    public boolean isUsableByPlayer(@Nonnull EntityPlayer player) {
        return false;
    }

    @Override
    public void openInventory(@Nonnull EntityPlayer player) {

    }

    @Override
    public void closeInventory(@Nonnull EntityPlayer player) {
        writeToNBT(stack.getTagCompound());
    }

    @Override
    public boolean isItemValidForSlot(int slot, @Nonnull ItemStack stack)
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

    private void readFromNBT(NBTTagCompound tag)
    {
        NBTTagList nbtTagList = tag.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        items = new ItemStack[getSizeInventory()];

        for(int slot = 0; slot < nbtTagList.tagCount(); slot++)
        {
            NBTTagCompound item = nbtTagList.getCompoundTagAt(slot);

            int i = item.getInteger("Slot");

            if(i >= 0 && i < getSizeInventory())
            {
                items[i] = new ItemStack(item);
            }
        }



        if(stack.hasTagCompound() && !stack.getTagCompound().hasKey("hue") && stack.getTagCompound().hasKey("color"))
        {
            String color;

            if(tag.getTag("color") instanceof NBTTagInt)
            {
                color = String.format("#%06X", (0xFFFFFF & tag.getInteger("color")));
            }
            else
            {
                color = tag.getString("color");

                if(color.startsWith("0x"))
                {
                    color = "#" + color.substring(2);
                }
            }

            //System.out.println("color: " + color);

            if(!color.isEmpty())
            {
                Color c = Color.decode(color);
                float[] hsbVals = new float[3];

                hsbVals = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsbVals);
                tag.setInteger("hue", (int) (hsbVals[0] * 360));
                tag.removeTag("color");
            }
        }
    }

    private void writeToNBT(NBTTagCompound tag)
    {
        NBTTagList nbtTagList = new NBTTagList();
        for(int slot = 0; slot < getSizeInventory(); slot++)
        {
            if(slot < items.length && items[slot] != null && items[slot] != ItemStack.EMPTY)
            {
                NBTTagCompound item = new NBTTagCompound();
                item.setInteger("Slot", slot);
                items[slot].writeToNBT(item);
                nbtTagList.appendTag(item);
            }
        }

        tag.setTag("Items", nbtTagList);
        tag.setInteger("hue", getHue());
    }

    @Override
    public boolean shouldConnectToNetwork()
    {
        return false;
    }

    @Override
    public boolean getRetaining()
    {
        return true;
    }

    @Override
    public void setRetaining(boolean retaining) {
    }

    @Override
    public int getHue() {
        return info.getHue();
    }

    @Override
    public void setHue(int hue)
    {
        info.setHue(hue);
    }
}
