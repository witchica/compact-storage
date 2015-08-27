package com.tattyseal.compactstorage.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class PacketApplyChestUpdate implements IMessage
{
	public int x;
	public int y;
	public int z;
	
	public int invX;
	public int invY;
	
	public PacketApplyChestUpdate(int x, int y, int z, int invX, int invY)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.invX = invX;
		this.invY = invY;
	}
	
	public PacketApplyChestUpdate()
	{
		this(0, 0, 0, 0, 0);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		invX = buf.readInt();
		invY = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(invX);
		buf.writeInt(invY);
	}
}
