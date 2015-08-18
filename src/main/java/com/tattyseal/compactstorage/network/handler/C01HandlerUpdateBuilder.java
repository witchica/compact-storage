package com.tattyseal.compactstorage.network.handler;

import com.tattyseal.compactstorage.network.packet.C01PacketUpdateBuilder;
import com.tattyseal.compactstorage.tileentity.TileEntityChestBuilder;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.server.MinecraftServer;

public class C01HandlerUpdateBuilder implements IMessageHandler<C01PacketUpdateBuilder, IMessage>
{
	@Override
	public IMessage onMessage(C01PacketUpdateBuilder message, MessageContext ctx) 
	{
		TileEntityChestBuilder builder = (TileEntityChestBuilder) MinecraftServer.getServer().worldServerForDimension(message.dimension).getTileEntity(message.x, message.y, message.z);
		if(builder != null) builder.info = message.info;
		if(builder != null) builder.type = message.type;
		
		return null;
	}
}
