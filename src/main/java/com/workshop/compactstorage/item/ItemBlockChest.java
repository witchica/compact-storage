package com.workshop.compactstorage.item;

import java.util.List;

import com.workshop.compactstorage.exception.InvalidSizeException;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
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
    		if(stack.getTagCompound().getTag("size") instanceof NBTTagIntArray)
    		{
        		int size = (int) (stack.getTagCompound().getIntArray("size")[0] * stack.getTagCompound().getIntArray("size")[1]);
        		list.add(EnumChatFormatting.GREEN + "Slots: " + size);
    		}
    		else
    		{
    			int size = 27;
    			list.add(EnumChatFormatting.GREEN + "Slots: " + size);
    			list.add(EnumChatFormatting.RED + "Yep. You broke it.");

                stack.getTagCompound().setIntArray("size", new int[] {9, 3});
    			
    			InvalidSizeException exception = new InvalidSizeException("You tried to pass off a " + stack.getTagCompound().getTag("size").getClass().getName() + " as a Integer Array. Do not report this or you will be ignored. This is a user based error.");
    			exception.printStackTrace();
    		}
    	}
    	else
    	{
    		list.add(EnumChatFormatting.RED + "Slots: none");
    	}
    	
    	super.addInformation(stack, player, list, b);
    }
}
