package com.workshop.compactstorage.network.handler;

import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.oredict.OreDictionary;

import com.workshop.compactstorage.essential.init.StorageBlocks;
import com.workshop.compactstorage.network.packet.C02PacketCraftChest;
import com.workshop.compactstorage.tileentity.TileEntityChestBuilder;
import com.workshop.compactstorage.util.StorageInfo;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class C02HandlerCraftChest implements IMessageHandler<C02PacketCraftChest, IMessage>
{
	@Override
	public IMessage onMessage(C02PacketCraftChest message, MessageContext ctx) 
	{
		TileEntityChestBuilder builder = (TileEntityChestBuilder) MinecraftServer.getServer().worldServerForDimension(message.dimension).getTileEntity(message.x, message.y, message.z);
		
		List<ItemStack> items = Arrays.asList(builder.items);
		List<List<ItemStack>> requiredItems = builder.info.getMaterialCost();
		
		boolean hasRequiredMaterials = true;
		
		for(ItemStack stack : items)
		{
			if(stack != null)
			{
				for(List<ItemStack> lists : requiredItems)
				{
					for(ItemStack listStack : lists)
					{
						if(listStack.getItem().equals(stack.getItem())  && stack.stackSize >= listStack.stackSize)
						{
							hasRequiredMaterials = true;
							break;
						}
						else
						{
							hasRequiredMaterials = false;
							break;
						}
					}
				}
			}
			else
			{
				hasRequiredMaterials = false;
				break;
			}
		}
		
		if(hasRequiredMaterials)
		{
			ItemStack stack = new ItemStack(StorageBlocks.chest, 1);
			
			NBTTagCompound tag = new NBTTagCompound();
			tag.setIntArray("size", new int[] {message.info.getSizeX(), message.info.getSizeY()});
			stack.setTagCompound(tag);
			
			EntityItem item = new EntityItem(MinecraftServer.getServer().worldServerForDimension(message.dimension), message.x, message.y + 1, message.z, stack);
			item.worldObj.spawnEntityInWorld(item);
			
			for(int x = 0; x < 4; x++)
			{
				builder.decrStackSize(x, requiredItems.get(x).get(0).stackSize);
			}
		}
		
		return null;
	}
}
