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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityBarrel extends TileEntity
{
    public ItemStack item = ItemStack.EMPTY;
    public int stackSize = 0;

    public void dropItems(EntityPlayer player)
    {
        LogHelper.dump("starting method to drop");

        if(stackSize > 0)
        {
            LogHelper.dump("stack size was > 0");

            ItemStack stack = item.copy();

            if(stackSize > stack.getMaxStackSize())
            {
                LogHelper.dump("stack size was > maxStackSize");

                stack.setCount(stack.getMaxStackSize());
                stackSize -= stack.getMaxStackSize();
            }
            else
            {
                LogHelper.dump("stack size was < maxStackSize, stacksize was " + stackSize);

                stack.setCount(stackSize);

                stackSize = 0;
                item = ItemStack.EMPTY;
            }

            EntityItem item = new EntityItem(world, player.posX, player.posY, player.posZ);
            item.setItem(stack);

            world.spawnEntity(item);
        }
        else
        {
            LogHelper.dump("No items inside!");
        }

        markDirty();
    }

    public boolean insertItems(@Nonnull ItemStack stack, EntityPlayer player)
    {
        LogHelper.dump("starting method");

        if(item.isEmpty() && !stack.isEmpty())
        {
            LogHelper.dump("item was empty, setting item to stack, stack size was " + item.getCount());

            item = stack.copy();
            stackSize = item.getCount();

            stack.setCount(0);
        }
        else
        {
            if(!stack.isEmpty() && ItemStack.areItemsEqual(stack, item) && stackSize < getMaxStorage())
            {
                LogHelper.dump("stack was not null, the items were equal and storage was less than max");

                stackSize += stack.getCount();
                stack.setCount(0);

                markDirty();

                return true;
            }
            else
            {
                LogHelper.dump(String.format("error inserting, stack was empty = %b, items are equal = %b, stackSize was less than %d = %b", stack.isEmpty(), ItemStack.areItemsEqual(stack, item), getMaxStorage(), stackSize < getMaxStorage()));
            }
        }

        markDirty();

        return false;
    }

    public String getText()
    {
        if(item.isEmpty())
        {
            return "Empty";
        }
        else if(stackSize < item.getMaxStackSize())
        {
            return stackSize + "";
        }
        else
        {
            int numOfStacks = stackSize / item.getMaxStackSize();

            return numOfStacks + "x" + item.getMaxStackSize();
        }
    }

    public int getMaxStorage()
    {
        if(item == ItemStack.EMPTY)
            return 64 * 64;

        return item.getMaxStackSize() * 64;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setTag("item", item.writeToNBT(new NBTTagCompound()));
        compound.setInteger("stackSize", stackSize);

        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        item = new ItemStack(compound.getCompoundTag("item"));
        stackSize = compound.getInteger("stackSize");

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
