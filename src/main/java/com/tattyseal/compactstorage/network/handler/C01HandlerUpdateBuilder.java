package com.tattyseal.compactstorage.network.handler;

import com.tattyseal.compactstorage.network.packet.C01PacketUpdateBuilder;
import com.tattyseal.compactstorage.tileentity.TileEntityChestBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class C01HandlerUpdateBuilder implements IMessageHandler<C01PacketUpdateBuilder, IMessage>
{

	@Override
	public IMessage onMessage(final C01PacketUpdateBuilder message, MessageContext ctx)
	{
		if(ctx.side.equals(Side.SERVER))
		{
			final WorldServer world = ctx.getServerHandler().player.getServerWorld();

			world.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					TileEntityChestBuilder builder = (TileEntityChestBuilder) world.getTileEntity(new BlockPos(message.x, message.y, message.z));
					if(builder != null) builder.info = message.info;
				}
			});
		}
		
		return null;
	}
}
