package com.workshop.compactstorage.compat;

import com.workshop.compactstorage.block.BlockChest;
import com.workshop.compactstorage.tileentity.TileEntityChest;
import cpw.mods.fml.common.event.FMLInterModComms;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

public class WailaCompat implements ICompat
{
	@Override
	public String modid()
	{
		return "Waila";
	}

	@Override
	public void registerCompat() throws Exception 
	{
		FMLInterModComms.sendMessage("Waila", "register", CompactStorageWailaHandler.class.getName() + ".callback");
	}
	
	public static class CompactStorageWailaHandler implements mcp.mobius.waila.api.IWailaDataProvider
	{

		@Override
		public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound nbt, World world, int x, int y, int z)
		{
			return null;
		}

		@Override
		public List<String> getWailaHead(ItemStack stack, List<String> toolTip, IWailaDataAccessor accessor, IWailaConfigHandler config) 
		{
			return toolTip;
		}

		@Override
		public List<String> getWailaBody(ItemStack stack, List<String> toolTip, IWailaDataAccessor accessor, IWailaConfigHandler config) 
		{
			if(accessor.getTileEntity() instanceof TileEntityChest)
			{
				TileEntityChest chest = (TileEntityChest) accessor.getTileEntity();
				
				toolTip.add("Chest Size: " + (chest.invX * chest.invY) * 64 + " (" + chest.invX * chest.invY + " slots)");
				
				int freeSlots = chest.invX * chest.invY;
				int freeItems = ((chest.invX * chest.invY) * 64);
				
				for(ItemStack item : chest.items)
				{
					if(item != null)
					{
						freeSlots = freeSlots - 1;
						freeItems = freeItems - item.stackSize;
					}
				}
				
				toolTip.add("Free Items: " + freeItems + " (" + freeSlots + " slots)");
				
				if(chest.items[0] != null) toolTip.add(EnumChatFormatting.BOLD + I18n.format(chest.items[0].getUnlocalizedName() + ".name", new Object[0]));
			}
			
			return toolTip;
		}

		@Override
		public List<String> getWailaTail(ItemStack stack, List<String> toolTip, IWailaDataAccessor accessor, IWailaConfigHandler config) 
		{
			return toolTip;
		}

		@Override
		public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) 
		{
			return null;
		}
		
		public static void callback(IWailaRegistrar registrar)
		{
			CompactStorageWailaHandler handler = new CompactStorageWailaHandler();
			registrar.registerBodyProvider(handler, BlockChest.class);
		}
	}
}
