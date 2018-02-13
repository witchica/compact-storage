package com.tattyseal.compactstorage.tileentity;

import com.tattyseal.compactstorage.util.LogHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityBarrelFluid extends TileEntity implements IBarrel
{
    public FluidTank tank;

    public TileEntityBarrelFluid()
    {
        super();
        tank = new FluidTank(64000);
    }

    @Override
    public void dropItems(EntityPlayer player)
    {

    }

    @Override
    public boolean insertItems(@Nonnull ItemStack stack, EntityPlayer player)
    {
        FluidActionResult res = FluidUtil.tryEmptyContainerAndStow(stack, tank, null, tank.getCapacity(), player);

        if (res.isSuccess())
        {
            player.setHeldItem(EnumHand.MAIN_HAND, res.result);
        }
        else
        {
            res = FluidUtil.tryFillContainerAndStow(stack, tank, null, tank.getCapacity(), player);
            if(res.isSuccess())
            {
                player.setHeldItem(EnumHand.MAIN_HAND, res.result);
            }
        }

        markDirty();

        return false;
    }

    public String getText()
    {
        return (tank.getFluid() == null ? "Empty" : tank.getFluidAmount() + "mB");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setTag("fluid", tank.writeToNBT(new NBTTagCompound()));

        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        tank = new FluidTank(64000);
        tank.readFromNBT(compound.getCompoundTag("fluid"));

        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound tag =  super.getUpdateTag();
        writeToNBT(tag);

        return tag;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(pos, 0, writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        super.onDataPacket(net, pkt);

        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag)
    {
        super.handleUpdateTag(tag);
    }

    public IBlockState getState()
    {
        return world.getBlockState(pos);
    }

    @Override
    public void markDirty()
    {
        world.markBlockRangeForRenderUpdate(pos, pos);
        world.notifyBlockUpdate(pos, getState(), getState(), 3);
        world.scheduleBlockUpdate(pos,this.getBlockType(),0,0);
        super.markDirty();
    }
}
