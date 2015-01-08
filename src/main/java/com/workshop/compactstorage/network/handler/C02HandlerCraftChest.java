package com.workshop.compactstorage.network.handler;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

import com.workshop.compactstorage.essential.init.StorageBlocks;
import com.workshop.compactstorage.network.packet.C01PacketUpdateBuilder;
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
		if(builder != null) builder.info = new StorageInfo(9, 3);
		
		ItemStack stack = new ItemStack(StorageBlocks.chest, 1);
		
		NBTTagCompound tag = new NBTTagCompound();
		tag.setIntArray("size", new int[] {message.info.getSizeX(), message.info.getSizeY()});
		stack.setTagCompound(tag);
		
		EntityItem item = new EntityItem(MinecraftServer.getServer().worldServerForDimension(message.dimension), message.x, message.y + 1, message.z, stack);
		item.worldObj.spawnEntityInWorld(item);
		
		return null;
	}
}
