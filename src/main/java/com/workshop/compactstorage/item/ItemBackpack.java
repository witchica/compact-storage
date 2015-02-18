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
        setMaxStackSize(1);
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
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if(!world.isRemote)
        {
            player.openGui(CompactStorage.instance, 0, world, (int) player.posX, (int) player.posY, (int) player.posZ);
            world.playSoundEffect(player.posX, player.posY, player.posZ, "random.chestopen", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        }

        return stack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 1;
    }
}
