package com.tattyseal.compactstorage.network.packet;

import com.tattyseal.compactstorage.util.StorageInfo;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class C01PacketUpdateBuilder implements IMessage
{
	public int x;
	public int y;
	public int z;
	
	public int dimension;
	
	public StorageInfo info;
	public StorageInfo.Type type;
	
	public C01PacketUpdateBuilder() 
	{
		this.x = 0;
		this.y = 0;
		this.z = 0;
		
		this.dimension = 0;
		this.info = new StorageInfo(0, 0);
		this.type = StorageInfo.Type.CHEST;
	}
	
	public C01PacketUpdateBuilder(int x, int y, int z, int dim, StorageInfo info, StorageInfo.Type type)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.dimension = dim;
		this.info = info;
		this.type = type;
	}
	
	public C01PacketUpdateBuilder(BlockPos pos, int dim, StorageInfo info, StorageInfo.Type type)
	{
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		
		this.dimension = dim;
		this.info = info;
		this.type = type;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		
		dimension = buf.readInt();
		
		info = new StorageInfo(buf.readInt(), buf.readInt());
		type = StorageInfo.Type.values()[buf.readInt()];
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
		buf.writeInt(type.ordinal());
	}
}
