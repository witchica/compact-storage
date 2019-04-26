package com.tattyseal.compactstorage.tileentity;

import com.tattyseal.compactstorage.util.LogHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityBarrel extends TileEntity implements IBarrel
{
    public ItemStack item = ItemStack.EMPTY;
    public int stackSize = 0;

    public int hue = 0;

    public TileEntityBarrel()
    {
        super();
        hue = 128;
    }

    @Override
    public ItemStack dropItems(EntityPlayer player)
    {
        ItemStack stack = dropItems(player, item.isEmpty() ? 64 : item.getMaxStackSize(), false);
        return stack;
    }

    public ItemStack dropItems(EntityPlayer player, int amount, boolean simulate)
    {
        if(stackSize > 0)
        {
            ItemStack stack = item.copy();

            if(stackSize > amount)
            {
                stack.setCount(amount);

                if(!simulate)
                    stackSize -= amount;
            }
            else
            {
                stack.setCount(stackSize);

                if(!simulate)
                {
                    stackSize = 0;
                    item = ItemStack.EMPTY;
                }
            }

            markDirty();
            return stack;
        }

        markDirty();

        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack insertItems(@Nonnull ItemStack stack, EntityPlayer player)
    {
        return insertItems(stack, player, false);
    }

    public ItemStack insertItems(@Nonnull ItemStack stack, EntityPlayer player, boolean simulate)
    {
        ItemStack workingStack = stack.copy();

        if(item.isEmpty() && !workingStack.isEmpty())
        {
            if(!simulate)
            {
                item = workingStack.copy();
                stackSize = item.getCount();
            }

            workingStack.setCount(0);

            markDirty();

            return workingStack;
        }
        else
        {
            if(!workingStack.isEmpty() && ItemStack.areItemsEqual(workingStack, item) && stackSize < getMaxStorage())
            {
                if(!simulate)
                    stackSize += workingStack.getCount();

                workingStack.setCount(0);

                markDirty();

                return workingStack;
            }
        }

        markDirty();

        return workingStack;
    }

    @Override
    public int color()
    {
        return hue;
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
        compound.setInteger("hue", hue);

        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        item = new ItemStack(compound.getCompoundTag("item"));
        stackSize = compound.getInteger("stackSize");
        hue = compound.getInteger("hue");

        super.readFromNBT(compound);

        markDirty();
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
        if(world != null && pos != null)
        {
            world.markBlockRangeForRenderUpdate(pos, pos);
            world.notifyBlockUpdate(pos, getState(), getState(), 3);
            world.scheduleBlockUpdate(pos,this.getBlockType(),0,0);
        }

        super.markDirty();
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return oldState.getBlock() != newState.getBlock();
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new IItemHandler()
            {
                @Override
                public int getSlots()
                {
                    return 1;
                }

                @Nonnull
                @Override
                public ItemStack getStackInSlot(int slot)
                {
                    ItemStack s = item.copy();
                    s.setCount(stackSize);
                    return s;
                }

                @Nonnull
                @Override
                public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
                {
                    return insertItems(stack, null, simulate);
                }

                @Nonnull
                @Override
                public ItemStack extractItem(int slot, int amount, boolean simulate)
                {
                    return dropItems(null, amount, simulate);
                }

                @Override
                public int getSlotLimit(int slot)
                {
                    return getMaxStorage();
                }
            });
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return super.hasCapability(capability, facing) || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }
}
