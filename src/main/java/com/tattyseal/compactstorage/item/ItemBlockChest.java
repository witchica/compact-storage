package com.tattyseal.compactstorage.item;

import java.util.List;

import javax.annotation.Nonnull;

import com.tattyseal.compactstorage.tileentity.TileEntityChest;
import com.tattyseal.compactstorage.util.StorageInfo;
import com.tattyseal.compactstorage.util.StorageInfo.Type;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

/**
 * Created by Toby on 06/11/2014.
 */
public class ItemBlockChest extends ItemBlock {

	public ItemBlockChest(Block block) {
		super(block);
		this.setMaxStackSize(64);
	}

	@Override
	public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			ItemStack stack = new ItemStack(this);
			new TileEntityChest().writeToNBT(stack.getOrCreateSubCompound("BlockEntityTag"));
			items.add(stack);
		}
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> list, ITooltipFlag flagIn) {
		if (stack.hasTagCompound()) {
			StorageInfo info = new StorageInfo(0, 0, 0, Type.CHEST);
			info.deserialize(stack.getOrCreateSubCompound("BlockEntityTag").getCompoundTag("info"));
			list.add(TextFormatting.GREEN + "Slots: " + info.getSizeX() * info.getSizeY());
			int hue = info.getHue();

			if (hue != -1) {
				list.add(TextFormatting.AQUA + "Hue: " + hue);
			} else {
				list.add(TextFormatting.AQUA + "White");
			}

			if (stack.getOrCreateSubCompound("BlockEntityTag").getBoolean("retaining")) {
				list.add(TextFormatting.AQUA + "" + TextFormatting.ITALIC + "Retaining");
			} else {
				list.add(TextFormatting.RED + "" + TextFormatting.ITALIC + "Non-Retaining");
			}
		}
	}
}
