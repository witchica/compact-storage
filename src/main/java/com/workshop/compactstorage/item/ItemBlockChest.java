package com.workshop.compactstorage.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

/**
 * Created by Toby on 06/11/2014.
 */
public class ItemBlockChest extends ItemBlock
{
    public ItemBlockChest(Block block)
    {
        super(block);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) 
    {
    	ItemStack stack = new ItemStack(item, 1);
    	
    	NBTTagCompound tag = new NBTTagCompound();
    	tag.setIntArray("size", new int[] {9, 3});
    	
    	stack.setTagCompound(tag);
    	list.add(stack);
    }
    
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) 
    {
    	if(stack.hasTagCompound())
    	{
    		int size = (int) (stack.getTagCompound().getIntArray("size")[0] * stack.getTagCompound().getIntArray("size")[1]);
    		list.add(EnumChatFormatting.GREEN + "Slots: " + size);
    	}
    	else
    	{
    		list.add(EnumChatFormatting.RED + "Slots: none");
    	}
    	
    	super.addInformation(stack, player, list, b);
    }
}
