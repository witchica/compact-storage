package com.workshop.compactchests.legacy.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Toby on 22/08/2014.
 */
public class ChestHandler implements IMessageHandler<ChestPacket, IMessage>
{

    @Override
    public IMessage onMessage(ChestPacket message, MessageContext ctx)
    {
        //((TileEntityChest) Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z)).shouldOpen = message.open;

        return null;
    }
}
