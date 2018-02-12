package com.tattyseal.compactstorage.network.packet;

import com.tattyseal.compactstorage.util.StorageInfo;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class C02PacketCraftChest implements IMessage
{
	public int x;
	public int y;
	public int z;
	
	private int dimension;
	
	public StorageInfo info;

	public C02PacketCraftChest()
	{
		this.x = 0;
		this.y = 0;
		this.z = 0;

		this.dimension = 0;
		this.info = new StorageInfo(0, 0, 180, StorageInfo.Type.CHEST);
	}
	
	public C02PacketCraftChest(BlockPos pos, int dim, StorageInfo info)
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

		info = new StorageInfo(buf.readInt(), buf.readInt(), buf.readInt(), StorageInfo.Type.values()[buf.readInt()]);
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
		buf.writeInt(info.getHue());
		buf.writeInt(info.getType().ordinal());
	}
}
