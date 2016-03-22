package com.tattyseal.compactstorage.network.handler;

import com.tattyseal.compactstorage.network.packet.C01PacketUpdateBuilder;
import com.tattyseal.compactstorage.tileentity.TileEntityChestBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class C01HandlerUpdateBuilder implements IMessageHandler<C01PacketUpdateBuilder, IMessage>
{
	@Override
	public IMessage onMessage(C01PacketUpdateBuilder message, MessageContext ctx)
	{
		TileEntityChestBuilder builder = (TileEntityChestBuilder) MinecraftServer.getServer().worldServerForDimension(message.dimension).getTileEntity(new BlockPos(message.x, message.y, message.z));
		if(builder != null) builder.info = message.info;
		if(builder != null) builder.type = message.type;
		
		return null;
	}
}
