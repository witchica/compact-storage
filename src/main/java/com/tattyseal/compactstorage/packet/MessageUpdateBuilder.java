package com.tattyseal.compactstorage.packet;

import com.tattyseal.compactstorage.tileentity.TileEntityChestBuilder;
import com.tattyseal.compactstorage.util.StorageInfo;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageUpdateBuilder implements IMessage {
	protected int x;
	protected int y;
	protected int z;
	protected StorageInfo info;

	public MessageUpdateBuilder() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.info = new StorageInfo(0, 0, 180, StorageInfo.Type.CHEST);
	}

	public MessageUpdateBuilder(BlockPos pos, StorageInfo info) {
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.info = info;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		info = new StorageInfo(buf.readInt(), buf.readInt(), buf.readInt(), StorageInfo.Type.values()[buf.readInt()]);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(info.getSizeX());
		buf.writeInt(info.getSizeY());
		buf.writeInt(info.getHue());
		buf.writeInt(info.getType().ordinal());
	}

	public static IMessage onMessage(final MessageUpdateBuilder message, MessageContext ctx) {
		if (ctx.side.equals(Side.SERVER)) {
			final WorldServer world = ctx.getServerHandler().player.getServerWorld();

			world.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					TileEntityChestBuilder builder = (TileEntityChestBuilder) world.getTileEntity(new BlockPos(message.x, message.y, message.z));
					if (builder != null) builder.getInfo().deserialize(message.info.serialize());
				}
			});
		}
		return null;
	}
}
