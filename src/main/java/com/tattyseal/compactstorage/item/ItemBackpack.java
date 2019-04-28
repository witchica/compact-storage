package com.tattyseal.compactstorage.item;

import java.util.List;

import javax.annotation.Nonnull;

import com.tattyseal.compactstorage.CompactStorage;
import com.tattyseal.compactstorage.tileentity.TileEntityChest;
import com.tattyseal.compactstorage.util.StorageInfo;
import com.tattyseal.compactstorage.util.StorageInfo.Type;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

/**
 * Created by Toby on 11/02/2015.
 */
public class ItemBackpack extends Item {

	public ItemBackpack() {
		setTranslationKey(CompactStorage.MODID + ".backpack");
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
	public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			ItemStack stack = new ItemStack(this);
			new TileEntityChest().writeToNBT(stack.getOrCreateSubCompound("BlockEntityTag"));
			items.add(stack);
		}
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (stack.hasTagCompound()) {
			StorageInfo info = new StorageInfo(0, 0, 0, Type.CHEST);
			info.deserialize(stack.getOrCreateSubCompound("BlockEntityTag").getCompoundTag("info"));
			tooltip.add(TextFormatting.GREEN + "Slots: " + info.getSizeX() * info.getSizeY());
			int hue = info.getHue();

			if (hue != -1) {
				tooltip.add(TextFormatting.AQUA + "Hue: " + hue);
			} else {
				tooltip.add(TextFormatting.AQUA + "White");
			}
		}
	}

	@Override
	public NBTTagCompound getNBTShareTag(ItemStack stack) {
		NBTTagCompound tag = super.getNBTShareTag(stack).copy();
		tag.getCompoundTag("BlockEntityTag").removeTag("items");
		return tag;
	}
}
