package com.workshop.compactstorage.tileentity;

import com.workshop.compactstorage.block.ChestType;
import com.workshop.compactstorage.essential.handler.ConfigurationHandler;
import com.workshop.compactstorage.util.ColorUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.awt.*;

/**
 * Created by Toby on 06/11/2014.
 */
public class TileEntityChest extends TileEntity
{
    public ForgeDirection direction;

    public boolean init;

    public TileEntityChest()
    {
        super();

        this.direction = ForgeDirection.NORTH;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        this.direction = ForgeDirection.getOrientation(tag.getInteger("facing"));
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        tag.setInteger("facing", direction.ordinal());
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, getBlockMetadata(), tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.func_148857_g());
    }

    public void setDirection(ForgeDirection direction)
    {
        this.direction = direction;
        updateBlock();
    }

    @Deprecated
    public void setDirection(int direction)
    {
        this.direction = ForgeDirection.getOrientation(direction);
        updateBlock();
    }

    public void updateBlock()
    {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
}
