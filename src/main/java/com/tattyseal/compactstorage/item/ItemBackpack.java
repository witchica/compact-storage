package com.tattyseal.compactstorage.item;

import com.tattyseal.compactstorage.CompactStorage;
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

    /*@Override
    public int getColorFromItemStack(ItemStack stack, int color)
    {
        if(stack.hasTagCompound() && stack.getTagCompound().hasKey("color"))
        {
            if(stack.getTagCompound().getTag("color") instanceof NBTTagString)
            {
                return Integer.decode(stack.getTagCompound().getString("color"));
            }
            else if(stack.getTagCompound().getTag("color") instanceof NBTTagInt)
            {
                return stack.getTagCompound().getInteger("color");
            }
        }

        return 0xFFFFFF;
    }*/



    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        if(!world.isRemote && hand == EnumHand.MAIN_HAND)
        {
            player.openGui(CompactStorage.instance, 0, world, (int) player.posX, (int) player.posY, (int) player.posZ);
            world.playSound((EntityPlayer)null, player.posX, player.posY + 1, player.posZ, SoundEvents.block_chest_open, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        }

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 1;
    }
}
