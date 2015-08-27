package com.tattyseal.compactstorage.network;

import com.tattyseal.compactstorage.tileentity.TileEntityChest;
import com.tattyseal.compactstorage.util.ChestUtil;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class HandlerApplyChestUpdate implements IMessageHandler<PacketApplyChestUpdate, IMessage>
{
	@Override
	public IMessage onMessage(PacketApplyChestUpdate message, MessageContext ctx) {
		if(message.x == 0 && message.y == 0 && message.z == 0 && message.invX == 0 && message.invY == 0) return null;
		
		TileEntityChest chest = (TileEntityChest) ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
		
		chest.invX = ChestUtil.clamp(message.invX, 1, 24);
		chest.invY = ChestUtil.clamp(message.invY, 1, 12);
		
		chest.markDirty();
		chest.updateBlock();
		
		return null;
	}
}