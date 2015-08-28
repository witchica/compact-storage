package com.tattyseal.compactstorage.network;

import com.tattyseal.compactstorage.util.ChestUtil;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class PacketApplyChestUpdate implements IMessage
{
	public int x;
	public int y;
	public int z;
	
	public int invX;
	public int invY;
	
	public ChestUtil.Type type;
	
	public PacketApplyChestUpdate(int x, int y, int z, int invX, int invY, ChestUtil.Type type)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.invX = invX;
		this.invY = invY;
		this.type = type;
	}
	
	public PacketApplyChestUpdate()
	{
		this(0, 0, 0, 0, 0, ChestUtil.Type.CHEST);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		invX = buf.readInt();
		invY = buf.readInt();
		type = ChestUtil.Type.values()[buf.readInt()];
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(invX);
		buf.writeInt(invY);
		buf.writeInt(type.ordinal());
	}
}
