package com.tattyseal.compactstorage.tileentity;

import com.tattyseal.compactstorage.ConfigurationHandler;
import com.tattyseal.compactstorage.api.IChest;
import com.tattyseal.compactstorage.util.StorageInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;

/**
 * Created by Toby on 06/11/2014.
 */
public class TileEntityChest extends TileEntity implements IInventory, IChest
{
    public EnumFacing direction;

    public int color;
    public int invX;
    public int invY;

    public boolean init;
    
    public ItemStack[] items;

    public TileEntityChest()
    {
        super();

        this.direction = EnumFacing.NORTH;
        this.items = new ItemStack[getSizeInventory()];

        for(int i = 0; i < items.length; i++)
        {
            items[i] = ItemStack.EMPTY;
        }
    }

    /* INVENTORY START */
    @Override
    public int getSizeInventory() 
    {
        return invX * invY;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getStackInSlot(int slot) 
    {
    	if(slot < items.length && items[slot] != null && !items[slot].isEmpty())
        {
        	return items[slot];
        }
        
        return ItemStack.EMPTY;
    }

    @Override
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
    public ItemStack removeStackFromSlot(int index)
    {
        items[index] = ItemStack.EMPTY;
        return ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) 
    {
    	if(items != null && slot < items.length)
        {
            items[slot] = stack;
            markDirty();
        }
    }

    @Override
    public String getName()
    {
        return "compactChest.inv";
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
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

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

    @Override
    public void markDirty()
    {
        super.markDirty();
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return false;
    }

    /* CUSTOM START */
    
    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        if(tag.hasKey("facing")) this.direction = EnumFacing.getFront(tag.getInteger("facing"));

        this.color = tag.getInteger("color");
        this.invX = tag.getInteger("invX");
        this.invY = tag.getInteger("invY");
        
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
    }

    @Override
    public NBTTagCompound getTileData()
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag = writeToNBT(tag);

        return tag;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag = writeToNBT(tag);

        return tag;
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag = writeToNBT(tag);

        return tag;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        if(direction != null) tag.setInteger("facing", direction.ordinal());
        tag.setInteger("color", color);
        tag.setInteger("invX", invX);
        tag.setInteger("invY", invY);
        
        NBTTagList nbtTagList = new NBTTagList();
        for(int slot = 0; slot < getSizeInventory(); slot++)
        {
            if(slot < items.length && items[slot] != null && !items[slot].isEmpty())
            {
                NBTTagCompound item = new NBTTagCompound();
                item.setInteger("Slot", slot);
                items[slot].writeToNBT(item);
                nbtTagList.appendTag(item);
            }
        }

        tag.setTag("Items", nbtTagList);

        return tag;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);

        return new SPacketUpdateTileEntity(pos, getBlockMetadata(), tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
    }

    public void setDirection(EnumFacing direction)
    {
        this.direction = direction;
        updateBlock();
    }

    public void setDirection(int direction)
    {
        this.direction = EnumFacing.getFront(direction);
        updateBlock();
    }

    public void updateBlock()
    {

    }

    @Override
    public int getInvX()
    {
        return invX;
    }

    @Override
    public int getInvY()
    {
        return invY;
    }

    @Override
    public StorageInfo getInfo()
    {
        return new StorageInfo(invX, invY);
    }

    @Override
    public int getColor()
    {
        return color;
    }

    @Override
    public boolean shouldConnectToNetwork()
    {
        return ConfigurationHandler.shouldConnect;
    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }
}
