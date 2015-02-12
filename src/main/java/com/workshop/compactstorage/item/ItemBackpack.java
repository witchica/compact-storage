package com.workshop.compactstorage.item;

import com.workshop.compactstorage.essential.CompactStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;

/**
 * Created by Toby on 11/02/2015.
 */
public class ItemBackpack extends Item
{
    public ItemBackpack()
    {
        super();
        setTextureName("compactstorage:backpack");
        setUnlocalizedName("backpack");
        setCreativeTab(CompactStorage.tabCS);
    }

    @Override
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
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int i, float xx, float yy, float zz)
    {
        player.openGui(CompactStorage.instance, 0, world, x, y, z);
        return true;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 1;
    }
}
