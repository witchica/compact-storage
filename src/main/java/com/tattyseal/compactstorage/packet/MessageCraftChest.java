package com.tattyseal.compactstorage.packet;

import java.util.ArrayList;
import java.util.List;

import com.tattyseal.compactstorage.tileentity.TileEntityChest;
import com.tattyseal.compactstorage.tileentity.TileEntityChestBuilder;
import com.tattyseal.compactstorage.util.StorageInfo;

import io.netty.buffer.ByteBuf;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.oredict.OreDictionary;

public class MessageCraftChest implements IMessage {
	protected int x;
	protected int y;
	protected int z;
	protected StorageInfo info;

	public MessageCraftChest() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.info = new StorageInfo(0, 0, 180, StorageInfo.Type.CHEST);
	}

	public MessageCraftChest(BlockPos pos, StorageInfo info) {
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.info = info;
	}

	public BlockPos getPos() {
		return new BlockPos(x, y, z);
	}

	public StorageInfo getInfo() {
		return info;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		info = new StorageInfo(buf.readInt(), buf.readInt(), buf.readInt(), StorageInfo.Type.values()[buf.readInt()]);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(info.getSizeX());
		buf.writeInt(info.getSizeY());
		buf.writeInt(info.getHue());
		buf.writeInt(info.getType().ordinal());
	}

	public static IMessage onMessage(MessageCraftChest message, MessageContext ctx) {
		WorldServer world = ctx.getServerHandler().player.getServerWorld();
		world.addScheduledTask(() -> {
			TileEntityChestBuilder builder = (TileEntityChestBuilder) world.getTileEntity(new BlockPos(message.x, message.y, message.z));
			if (builder == null) return;

			List<ItemStack> items = new ArrayList<>();
			for (int i = 0; i < 4; i++) {
				items.add(builder.getItems().getStackInSlot(i));
			}
			List<ItemStack> requiredItems = builder.getInfo().getMaterialCost();

			boolean hasRequiredMaterials = true;

			for (int slot = 0; slot < items.size(); slot++) {
				ItemStack stack = items.get(slot);

				if (stack != null && slot < requiredItems.size() && requiredItems.get(slot) != null) {
					if (OreDictionary.itemMatches(requiredItems.get(slot), stack, false) && stack.getCount() >= requiredItems.get(slot).getCount()) {
						hasRequiredMaterials = true;
					} else {
						hasRequiredMaterials = requiredItems.get(slot) != null && requiredItems.get(slot).getCount() == 0;
						break;
					}
				} else {
					hasRequiredMaterials = false;
					break;
				}
			}

			if (hasRequiredMaterials && builder.getItems().getStackInSlot(4).isEmpty()) {
				ItemStack stack = message.info.getType().display.copy();
				TileEntityChest chest = new TileEntityChest(message.info);
				chest.writeToNBT(stack.getOrCreateSubCompound("BlockEntityTag"));
				builder.getItems().setStackInSlot(4, stack);

				for (int x = 0; x < requiredItems.size(); x++) {
					builder.getItems().getStackInSlot(x).shrink(requiredItems.get(x).getCount());
				}

				world.playSound(null, builder.getPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 1, 1);
			} else {
				world.playSound(null, builder.getPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 1, 0);
			}
		});

		return null;
	}
}
