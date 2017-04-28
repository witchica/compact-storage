package com.tattyseal.compactstorage.item;

import com.tattyseal.compactstorage.CompactStorage;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemColored;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Toby on 11/02/2015.
 */
public class ItemBackpack extends Item
{
    public ItemBackpack()
    {
        super();
        setUnlocalizedName("backpack");
        setCreativeTab(CompactStorage.tabCS);
        setMaxStackSize(1);

    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        if(!world.isRemote && hand == EnumHand.MAIN_HAND)
        {
            player.openGui(CompactStorage.instance, 0, world, (int) player.posX, (int) player.posY, (int) player.posZ);
            world.playSound((EntityPlayer)null, player.posX, player.posY + 1, player.posZ, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        }

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 1;
    }
}
