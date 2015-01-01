package com.workshop.compactstorage.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.Optional;

/**
 * Created by Toby on 06/11/2014.
 */
@Optional.Interface(iface = "cofh.api.tileentity.ISecurable", modid = "CoFHCore")
public class TileEntityChest extends TileEntity implements IInventory, cofh.api.tileentity.ISecurable
{
    public ForgeDirection direction;

    public int color;
    public int invX;
    public int invY;

    public boolean init;

    public TileEntityChest()
    {
        super();

        this.direction = ForgeDirection.NORTH;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        this.direction = ForgeDirection.getOrientation(tag.getInteger("facing"));

        this.color = tag.getInteger("color");
        this.invX = tag.getInteger("invX");
        this.invY = tag.getInteger("invY");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        tag.setInteger("facing", direction.ordinal());
        tag.setInteger("color", color);
        tag.setInteger("invX", invX);
        tag.setInteger("invY", invY);
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, getBlockMetadata(), tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.func_148857_g());
    }

    public void setDirection(ForgeDirection direction)
    {
        this.direction = direction;
        updateBlock();
    }

    @Deprecated
    public void setDirection(int direction)
    {
        this.direction = ForgeDirection.getOrientation(direction);
        updateBlock();
    }

    public void updateBlock()
    {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public int getSizeInventory() {
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int p_70301_1_) {
        return null;
    }

    @Override
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {

    }

    @Override
    public String getInventoryName() {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 0;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return false;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return false;
    }

    @Optional.Method(modid = "CoFHCore")
	@Override
	public boolean canPlayerAccess(String string) 
	{
		return true;
	}

    @Optional.Method(modid = "CoFHCore")
    @Override
	public AccessMode getAccess() 
	{
		return AccessMode.RESTRICTED;
	}

    @Optional.Method(modid = "CoFHCore")
    @Override
	public String getOwnerName() 
	{
		return "tattyseal";
	}

    @Optional.Method(modid = "CoFHCore")
    @Override
	public boolean setAccess(AccessMode mode) 
	{
		return true;
	}

    @Optional.Method(modid = "CoFHCore")
    @Override
	public boolean setOwnerName(String name) 
	{
		return false;
	}
}
