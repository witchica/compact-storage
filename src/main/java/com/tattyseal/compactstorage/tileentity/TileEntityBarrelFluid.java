package com.tattyseal.compactstorage.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityBarrelFluid extends TileEntity implements IBarrel, ITickable {
	public static final int CAPACITY = 32000;

	public FluidTank tank;
	public int hue;

	public int lastAmount;

	public TileEntityBarrelFluid() {
		super();
		tank = new FluidTank(CAPACITY);
		hue = 128;
	}

	@Override
	public ItemStack giveItems(EntityPlayer player) {
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack takeItems(@Nonnull ItemStack stack, EntityPlayer player) {
		FluidActionResult res = FluidUtil.tryEmptyContainerAndStow(stack, tank, null, tank.getCapacity(), player, true);

		if (res.isSuccess()) {
			return res.result;
		} else {
			res = FluidUtil.tryFillContainerAndStow(stack, tank, null, tank.getCapacity(), player, true);
			if (res.isSuccess()) { return res.result; }
		}

		markDirty();

		return stack;
	}

	@Override
	public int color() {
		return hue;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("fluid", tank.writeToNBT(new NBTTagCompound()));
		compound.setInteger("hue", hue);

		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		tank = new FluidTank(CAPACITY);
		tank.readFromNBT(compound.getCompoundTag("fluid"));

		hue = compound.getInteger("hue");

		super.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = super.getUpdateTag();
		writeToNBT(tag);

		return tag;
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, 0, writeToNBT(new NBTTagCompound()));
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);

		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
	}

	public IBlockState getState() {
		return world.getBlockState(pos);
	}

	@Override
	public void markDirty() {
		world.markBlockRangeForRenderUpdate(pos, pos);
		world.notifyBlockUpdate(pos, getState(), getState(), 3);
		world.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
		super.markDirty();
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return super.hasCapability(capability, facing) || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) { return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank); }

		return super.getCapability(capability, facing);
	}

	@Override
	public void update() {
		if (tank != null) {
			if (tank.getFluid() != null && lastAmount != tank.getFluidAmount()) {
				markDirty();
			}

			lastAmount = tank.getFluid() == null ? 0 : tank.getFluidAmount();
		}
	}

	@SideOnly(Side.CLIENT)
	public String getText() {
		if (tank == null || tank.getFluid() == null || tank.getFluidAmount() == 0) {
			return "Empty";
		} else {
			return tank.getFluidAmount() + "/" + CAPACITY + "mB";
		}
	}
}
