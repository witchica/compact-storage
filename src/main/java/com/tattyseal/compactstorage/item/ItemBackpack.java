package com.tattyseal.compactstorage.item;

import com.tattyseal.compactstorage.CompactStorage;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemColored;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

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


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        if(stack.hasTagCompound())
        {
            NBTTagCompound tag = stack.getTagCompound();

            if(tag.hasKey("size"))
            {
                int[] size = tag.getIntArray("size");
                tooltip.add(TextFormatting.RED + "Slots: " + (size[0] * size[1]));
            }

            if(tag.hasKey("hue"))
            {
                int hue = stack.getTagCompound().getInteger("hue");

                if(hue != -1)
                {
                    tooltip.add(TextFormatting.AQUA + "Hue: " + hue);
                }
                else
                {
                    tooltip.add(TextFormatting.AQUA + "White");
                }
            }
        }
    }
}
