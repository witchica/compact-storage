package com.tattyseal.compactstorage.network.handler;

import com.tattyseal.compactstorage.CompactStorage;
import com.tattyseal.compactstorage.network.packet.C02PacketCraftChest;
import com.tattyseal.compactstorage.tileentity.TileEntityChestBuilder;
import com.tattyseal.compactstorage.util.LogHelper;
import com.tattyseal.compactstorage.util.StorageInfo;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Arrays;
import java.util.List;

public class C02HandlerCraftChest implements IMessageHandler<C02PacketCraftChest, IMessage>
{
	@Override
	public IMessage onMessage(final C02PacketCraftChest message, MessageContext ctx)
	{
		if(!ctx.side.equals(Side.SERVER))
			return null;

		final WorldServer world = ctx.getServerHandler().player.getServerWorld();
		world.addScheduledTask(new Runnable() {
			@Override
			public void run() {
				TileEntityChestBuilder builder = (TileEntityChestBuilder) world.getTileEntity(new BlockPos(message.x, message.y, message.z));

				if (builder == null) {
					return;
				}

				LogHelper.dump(builder.info.getType().name);

				ItemStack[] itemsArray = new ItemStack[4];

				System.arraycopy(builder.items, 0, itemsArray, 0, 4);

				List<ItemStack> items = Arrays.asList(itemsArray);
				List<ItemStack> requiredItems = builder.info.getMaterialCost();

				boolean hasRequiredMaterials = true;

				for(int slot = 0; slot < items.size(); slot++)
				{
					ItemStack stack = items.get(slot);

					if(stack != null && slot < requiredItems.size() && requiredItems.get(slot) != null)
					{
						if(OreDictionary.itemMatches(requiredItems.get(slot), stack, false) && stack.getCount() >=  requiredItems.get(slot).getCount())
						{
							hasRequiredMaterials = true;
						}
						else
						{
							hasRequiredMaterials = requiredItems.get(slot) != null && requiredItems.get(slot).getCount() == 0;
							break;
						}
					}
					else
					{
						hasRequiredMaterials = false;
						break;
					}
				}

				LogHelper.dump("HAS REQ MATS: " + hasRequiredMaterials);

				if(hasRequiredMaterials && builder.getStackInSlot(4).isEmpty())
				{
					ItemStack stack = new ItemStack(message.info.getType().equals(StorageInfo.Type.BACKPACK) ? CompactStorage.backpack : ItemBlock.getItemFromBlock(CompactStorage.chest), 1);

					NBTTagCompound tag = new NBTTagCompound();
					tag.setIntArray("size", new int[]{message.info.getSizeX(), message.info.getSizeY()});
					tag.setInteger("hue", message.info.getHue());
					stack.setTagCompound(tag);

					builder.setInventorySlotContents(4, stack);

					LogHelper.dump("SPAWNED ITEM ENTITY");

					for(int x = 0; x < requiredItems.size(); x++)
					{
						builder.decrStackSize(x, requiredItems.get(x).getCount());

						LogHelper.dump("DECREASED ITEMS IN INVENTORY");
					}

					world.playSound(null, builder.getPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 1, 1);
				}
				else
				{
					world.playSound(null, builder.getPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 1, 0);
				}
			}
		});

		
		return null;
	}
}
