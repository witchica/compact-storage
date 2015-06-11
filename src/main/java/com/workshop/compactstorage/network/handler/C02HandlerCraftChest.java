package com.workshop.compactstorage.network.handler;

import com.workshop.compactstorage.essential.init.StorageBlocks;
import com.workshop.compactstorage.essential.init.StorageItems;
import com.workshop.compactstorage.network.packet.C02PacketCraftChest;
import com.workshop.compactstorage.tileentity.TileEntityChestBuilder;
import com.workshop.compactstorage.util.StorageInfo;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Arrays;
import java.util.List;

public class C02HandlerCraftChest implements IMessageHandler<C02PacketCraftChest, IMessage>
{
	@Override
	public IMessage onMessage(C02PacketCraftChest message, MessageContext ctx) 
	{
		TileEntityChestBuilder builder = (TileEntityChestBuilder) MinecraftServer.getServer().worldServerForDimension(message.dimension).getTileEntity(message.x, message.y, message.z);
		
		List<ItemStack> items = Arrays.asList(builder.items);
		List<ItemStack> requiredItems = builder.info.getMaterialCost(message.type);
		
		boolean hasRequiredMaterials = true;
		
		for(int slot = 0; slot < items.size(); slot++)
		{
			ItemStack stack = items.get(slot);

			if(stack != null && slot < requiredItems.size() && requiredItems.get(slot) != null)
			{
				if(OreDictionary.itemMatches(requiredItems.get(slot), stack, false) && stack.stackSize >=  requiredItems.get(slot).stackSize)
				{
					hasRequiredMaterials = true;
				}
				else
				{
					if(requiredItems.get(slot) != null && requiredItems.get(slot).stackSize == 0)
					{
						hasRequiredMaterials = true;
					}
					else
					{
						hasRequiredMaterials = false;
					}
					break;
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
			ItemStack stack = new ItemStack((Item) (message.type.equals(StorageInfo.Type.BACKPACK) ? StorageItems.backpack : ItemBlock.getItemFromBlock(StorageBlocks.chest)), 1);
			
			NBTTagCompound tag = new NBTTagCompound();
			tag.setIntArray("size", new int[] {message.info.getSizeX(), message.info.getSizeY()});
			tag.setString("color", message.color);
			stack.setTagCompound(tag);
			
			EntityItem item = new EntityItem(MinecraftServer.getServer().worldServerForDimension(message.dimension), message.x, message.y + 1, message.z, stack);
			item.worldObj.spawnEntityInWorld(item);
			
			for(int x = 0; x < requiredItems.size(); x++)
			{
				builder.decrStackSize(x, requiredItems.get(x).stackSize);
			}
		}
		
		return null;
	}
}
