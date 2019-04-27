package com.tattyseal.compactstorage.tileentity;

import java.awt.Color;

import javax.annotation.Nonnull;

import com.tattyseal.compactstorage.api.IChest;
import com.tattyseal.compactstorage.block.BlockChest;
import com.tattyseal.compactstorage.inventory.InfoItemHandler;
import com.tattyseal.compactstorage.util.StorageInfo;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Created by Toby on 06/11/2014.
 */
public class TileEntityChest extends TileEntity implements IChest, ITickable {

	private EnumFacing direction;
	private Color color;
	protected StorageInfo info;
	protected boolean retaining;
	private float lidAngle;
	private float prevLidAngle;
	protected int numPlayersUsing;
	protected int ticksSinceSync;
	private InfoItemHandler items;

	public TileEntityChest() {
		this.setDirection(EnumFacing.NORTH);
		this.info = new StorageInfo(9, 3, 180, StorageInfo.Type.CHEST);
		this.items = new InfoItemHandler(info);
	}

	public TileEntityChest(StorageInfo info) {
		this.setDirection(EnumFacing.NORTH);
		this.info = info;
		this.items = new InfoItemHandler(info);
	}

	@Override
	public boolean receiveClientEvent(int id, int type) {
		if (id == 1) {
			this.numPlayersUsing = type;
			return true;
		} else {
			return super.receiveClientEvent(id, type);
		}
	}

	@Override
	public void onOpened(EntityPlayer player) {
		if (!player.isSpectator()) {
			if (this.numPlayersUsing < 0) {
				this.numPlayersUsing = 0;
			}

			++this.numPlayersUsing;
			this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
			this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
		}
	}

	@Override
	public void onClosed(EntityPlayer player) {
		if (!player.isSpectator() && this.getBlockType() instanceof BlockChest) {
			--this.numPlayersUsing;
			this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
			this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
		}
	}

	@Override
	@Nonnull
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("facing", getDirection().ordinal());
		tag.setTag("info", info.serialize());
		tag.setBoolean("retaining", retaining);
		tag.setTag("items", getItems().serializeNBT());
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		this.setDirection(EnumFacing.byIndex(tag.getInteger("facing")));
		this.retaining = tag.getBoolean("retaining");
		this.info.deserialize(tag.getCompoundTag("info"));
		this.getItems().deserializeNBT(tag.getCompoundTag("items"));
		this.setColor(getHue() == -1 ? Color.white : Color.getHSBColor(info.getHue() / 360f, 0.5f, 0.5f));
	}

	@Override
	@Nonnull
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = super.getUpdateTag();
		tag.setInteger("facing", getDirection().ordinal());
		tag.setTag("info", info.serialize());
		tag.setBoolean("retaining", retaining);
		return tag;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		this.setDirection(EnumFacing.byIndex(tag.getInteger("facing")));
		this.retaining = tag.getBoolean("retaining");
		this.info.deserialize(tag.getCompoundTag("info"));
		this.getItems().setSize(info.getSizeX() * info.getSizeY());
		this.setColor(getHue() == -1 ? Color.white : Color.getHSBColor(info.getHue() / 360f, 0.5f, 0.5f));
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		handleUpdateTag(pkt.getNbtCompound());
	}

	public void updateBlock() {
		markDirty();
	}

	@Override
	public void update() {
		int i = this.pos.getX();
		int j = this.pos.getY();
		int k = this.pos.getZ();
		++this.ticksSinceSync;

		if (!this.world.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + i + j + k) % 200 == 0) {
			this.numPlayersUsing = 0;

			for (EntityPlayer entityplayer : this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(i - 5.0F, j - 5.0F, k - 5.0F, i + 1 + 5.0F, j + 1 + 5.0F, k + 1 + 5.0F))) {
				if (entityplayer.openContainer instanceof ContainerChest) {
					IInventory iinventory = ((ContainerChest) entityplayer.openContainer).getLowerChestInventory();
					if (iinventory == this) ++this.numPlayersUsing;
				}
			}
		}

		this.setPrevLidAngle(this.getLidAngle());

		if (this.numPlayersUsing > 0 && this.getLidAngle() == 0.0F) {
			double d1 = i + 0.5D;
			double d2 = k + 0.5D;

			this.world.playSound(null, d1, j + 0.5D, d2, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
		}

		if (this.numPlayersUsing == 0 && this.getLidAngle() > 0.0F || this.numPlayersUsing > 0 && this.getLidAngle() < 1.0F) {
			float f2 = this.getLidAngle();

			if (this.numPlayersUsing > 0) {
				this.setLidAngle(this.getLidAngle() + 0.1F);
			} else {
				this.setLidAngle(this.getLidAngle() - 0.1F);
			}

			if (this.getLidAngle() > 1.0F) {
				this.setLidAngle(1.0F);
			}

			if (this.getLidAngle() < 0.5F && f2 >= 0.5F) {
				double d3 = i + 0.5D;
				double d0 = k + 0.5D;

				this.world.playSound(null, d3, j + 0.5D, d0, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
			}

			if (this.getLidAngle() < 0.0F) {
				this.setLidAngle(0.0F);
			}
		}
	}

	@Override
	public int getInvX() {
		return this.info.getSizeX();
	}

	@Override
	public int getInvY() {
		return this.info.getSizeY();
	}

	@Override
	public StorageInfo getInfo() {
		return info;
	}

	@Override
	public int getHue() {
		return info.getHue();
	}

	@Override
	public void setHue(int hue) {
		info.setHue(hue);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(items);
		return super.getCapability(capability, facing);
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public ItemStackHandler getItems() {
		return items;
	}

	public boolean isRetaining() {
		return this.retaining;
	}

	public void setRetaining(boolean retain) {
		this.retaining = retain;
	}

	public EnumFacing getDirection() {
		return direction;
	}

	public void setDirection(EnumFacing direction) {
		this.direction = direction;
	}

	public float getLidAngle() {
		return lidAngle;
	}

	public void setLidAngle(float lidAngle) {
		this.lidAngle = lidAngle;
	}

	public float getPrevLidAngle() {
		return prevLidAngle;
	}

	public void setPrevLidAngle(float prevLidAngle) {
		this.prevLidAngle = prevLidAngle;
	}
}
