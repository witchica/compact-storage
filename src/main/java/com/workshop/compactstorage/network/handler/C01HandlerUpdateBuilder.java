package com.workshop.compactstorage.network.handler;

import net.minecraft.server.MinecraftServer;

import com.workshop.compactstorage.network.packet.C01PacketUpdateBuilder;
import com.workshop.compactstorage.tileentity.TileEntityChestBuilder;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class C01HandlerUpdateBuilder implements IMessageHandler<C01PacketUpdateBuilder, IMessage>
{
	@Override
	public IMessage onMessage(C01PacketUpdateBuilder message, MessageContext ctx) 
	{
		TileEntityChestBuilder builder = (TileEntityChestBuilder) MinecraftServer.getServer().worldServerForDimension(message.dimension).getTileEntity(message.x, message.y, message.z);
		if(builder != null) builder.info = message.info;
		
		return null;
	}
}
