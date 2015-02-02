package com.workshop.compactchests.network;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

/**
 * Created by Toby on 22/08/2014.
 */
public class ChestPacket implements IMessage
{
    public boolean open;

    public int x;
    public int y;
    public int z;

    public ChestPacket(boolean open, int x, int y, int z)
    {
        this.open = open;

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ChestPacket()
    {
        open = false;
        x = 0;
        y = 0;
        z = 0;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        open = buf.readBoolean();

        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(open);

        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }
}
