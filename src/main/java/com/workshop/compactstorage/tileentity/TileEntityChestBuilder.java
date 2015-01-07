package com.workshop.compactstorage.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import cofh.api.tileentity.ISecurable.AccessMode;
import cofh.core.util.SocialRegistry;

import com.workshop.compactstorage.util.StorageInfo;

import cpw.mods.fml.common.Optional;

@Optional.Interface(iface = "cofh.api.tileentity.ISecurable", modid = "CoFHCore")
public class TileEntityChestBuilder extends TileEntity implements cofh.api.tileentity.ISecurable
{
	public StorageInfo info;
	public int dimension;
	
	public TileEntityChestBuilder(int dimension)
	{
		this.dimension = dimension;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) 
	{
		super.writeToNBT(tag);
		
		tag.setTag("info", StorageInfo.writeToNBT(info));
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) 
	{
		super.readFromNBT(tag);
		
		this.info = StorageInfo.readFromNBT(tag.getCompoundTag("info"));
	}
	

    /* COFH CORE START */

    @Optional.Method(modid = "CoFHCore")
	@Override
	public boolean canPlayerAccess(String name) 
	{
    	return true;
    	
		/* switch(mode)
		{
			case PUBLIC: return true;
			case PRIVATE: return name.equals(player);
			case RESTRICTED: return SocialRegistry.playerHasAccess(name, player);
			default: return false;
		} */
	}

    @Optional.Method(modid = "CoFHCore")
    @Override
	public AccessMode getAccess() 
	{
		return AccessMode.PUBLIC;
	}

    @Optional.Method(modid = "CoFHCore")
    @Override
	public String getOwnerName() 
	{
		return /* player */ "tattyseal";
	}

    @Optional.Method(modid = "CoFHCore")
    @Override
	public boolean setAccess(AccessMode mode) 
	{
    	//this.mode = mode;
		return true;
	}

    @Optional.Method(modid = "CoFHCore")
    @Override
	public boolean setOwnerName(String name) 
	{
    	//this.player = name;
		return true;
	}
}
