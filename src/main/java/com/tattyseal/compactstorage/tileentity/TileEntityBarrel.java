package com.tattyseal.compactstorage.tileentity;

import javax.annotation.Nullable;

import com.tattyseal.compactstorage.inventory.BarrelItemHandler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
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
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityBarrel extends TileEntity implements IBarrel {

	protected ItemStack item = ItemStack.EMPTY;
	protected int count = 0;
	protected BarrelItemHandler handler = new BarrelItemHandler(this);

	public int hue = 0;

	public TileEntityBarrel() {
		super();
		hue = 128;
	}

	@Override
	public ItemStack giveItems(EntityPlayer player) {
		ItemStack stack = tryTakeStack(player, item.getMaxStackSize(), false);
		return stack;
	}

	@Override
	public ItemStack takeItems(ItemStack stack, EntityPlayer player) {
		return insertItems(stack, player, false);
	}

	public ItemStack tryTakeStack(EntityPlayer player, int amount, boolean simulate) {
		if (count > 0) {
			ItemStack stack = item.copy();
			stack.setCount(Math.min(count, Math.min(stack.getMaxStackSize(), amount)));
			if (!simulate) {
				count -= stack.getCount();
				if (count <= 0) {
					count = 0;
					item = ItemStack.EMPTY;
				}
			}
			markDirty();
			return stack;
		}
		return ItemStack.EMPTY;
	}

	public ItemStack insertItems(ItemStack stack, EntityPlayer player, boolean simulate) {
		ItemStack workingStack = stack.copy();
		if (workingStack.isEmpty()) return ItemStack.EMPTY;

		if (item.isEmpty()) {
			if (!simulate) {
				item = workingStack.copy();
				count = item.getCount();
				item.setCount(1);
				markDirty();
			}
			return ItemStack.EMPTY;
		} else {
			if (OreDictionary.itemMatches(item, workingStack, true) && count < getMaxStorage()) {
				int used = Math.min(workingStack.getCount(), getMaxStorage() - count);
				if (!simulate) {
					count += used;
					markDirty();
				}
				workingStack.shrink(used);
				return workingStack;
			}
		}
		return workingStack;
	}

	@Override
	public int color() {
		return hue;
	}

	public String getText() {
		if (item.isEmpty()) {
			return "Empty";
		} else if (count < item.getMaxStackSize()) {
			return count + "";
		} else {
			int numOfStacks = count / item.getMaxStackSize();

			return numOfStacks + "x" + item.getMaxStackSize();
		}
	}

	public int getMaxStorage() {
		return item.getMaxStackSize() * 64;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("item", item.writeToNBT(new NBTTagCompound()));
		compound.setInteger("count", count);
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		item = new ItemStack(compound.getCompoundTag("item"));
		count = compound.getInteger("count");
		super.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = this.writeToNBT(new NBTTagCompound());
		NBTTagCompound item = this.item.getItem().getNBTShareTag(this.item);
		if (this.item.hasTagCompound()) tag.getCompoundTag("item").setTag("tag", item);
		return tag;
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}

	public IBlockState getState() {
		return world.getBlockState(pos);
	}

	@Override
	public void markDirty() {
		if (world != null && pos != null) {
			world.markBlockRangeForRenderUpdate(pos, pos);
			world.notifyBlockUpdate(pos, getState(), getState(), 3);
			world.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
		}

		super.markDirty();
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(handler);
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return super.hasCapability(capability, facing) || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

	public ItemStack getBarrelStack() {
		return this.item;
	}

	public int getCount() {
		return count;
	}
}
