package com.tattyseal.compactstorage.item;

import com.tattyseal.compactstorage.exception.InvalidSizeException;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

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
		tag.setString("color", "0xFFFFFF");
    	
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
        		list.add(TextFormatting.GREEN + "Slots: " + size);
    		}
    		else
    		{
    			int size = 27;
    			list.add(TextFormatting.GREEN + "Slots: " + size);
    			list.add(TextFormatting.RED + "Yep. You broke it.");

                stack.getTagCompound().setIntArray("size", new int[] {9, 3});
    			
    			InvalidSizeException exception = new InvalidSizeException("You tried to pass off a " + stack.getTagCompound().getTag("size").getClass().getName() + " as a Integer Array. Do not report this or you will be ignored. This is a user based error.");
    			exception.printStackTrace();
    		}
    	}
    	else
    	{
    		list.add(TextFormatting.RED + "Slots: none");
    	}
    	
    	super.addInformation(stack, player, list, b);
    }

	/*@Override
	public int getColorFromItemStack(ItemStack stack, int renderPass)
	{
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("color"))
		{
			if(stack.getTagCompound().hasKey("color"))
			{
				String color = stack.getTagCompound().getString("color");
				return color == "" ? 0xffffff : Integer.decode(color);
			}
			else
			{
				return 0xffffff;
			}
		}

		return 0xFFFFFF;
	}*/
}
