package com.tattyseal.compactstorage.item;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.tattyseal.compactstorage.CompactStorage;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

/**
 * Created by Toby on 11/02/2015.
 */
public class ItemBackpack extends Item {
	public ItemBackpack() {
		super();
		setTranslationKey("backpack");
		setCreativeTab(CompactStorage.TAB);
		setMaxStackSize(1);

	}

	@Override
	@Nonnull
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		if (!world.isRemote && hand == EnumHand.MAIN_HAND) {
			player.openGui(CompactStorage.instance, 0, world, (int) player.posX, (int) player.posY, (int) player.posZ);
			world.playSound(null, player.posX, player.posY + 1, player.posZ, SoundEvents.BLOCK_CLOTH_FALL, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
		}

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 1;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);

		if (stack.hasTagCompound()) {
			NBTTagCompound tag = stack.getTagCompound();

			if (tag.hasKey("size")) {
				int[] size = tag.getIntArray("size");
				tooltip.add(TextFormatting.DARK_GREEN + "Slots: " + (size[0] * size[1]));
			}

			if (tag.hasKey("hue")) {
				int hue = stack.getTagCompound().getInteger("hue");

				if (hue != -1) {
					tooltip.add(TextFormatting.AQUA + "Hue: " + hue);
				} else {
					tooltip.add(TextFormatting.AQUA + "White");
				}
			}
		}
	}
}
