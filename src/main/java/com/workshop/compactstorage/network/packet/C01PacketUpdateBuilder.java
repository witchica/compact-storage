package com.workshop.compactstorage.network.packet;

import io.netty.buffer.ByteBuf;

import com.workshop.compactstorage.util.BlockPos;
import com.workshop.compactstorage.util.StorageInfo;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class C01PacketUpdateBuilder implements IMessage
{
	public int x;
	public int y;
	public int z;
	
	public int dimension;
	
	public StorageInfo info;
	
	public C01PacketUpdateBuilder() 
	{
		this.x = 0;
		this.y = 0;
		this.z = 0;
		
		this.dimension = 0;
		this.info = new StorageInfo(0, 0);
	}
	
	public C01PacketUpdateBuilder(int x, int y, int z, int dim, StorageInfo info)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.dimension = dim;
		this.info = info;
	}
	
	public C01PacketUpdateBuilder(BlockPos pos, int dim, StorageInfo info)
	{
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		
		this.dimension = dim;
		this.info = info;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		
		dimension = buf.readInt();
		
		info = new StorageInfo(buf.readInt(), buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		
		buf.writeInt(dimension);
		
		buf.writeInt(info.getSizeX());
		buf.writeInt(info.getSizeY());
	}
}
