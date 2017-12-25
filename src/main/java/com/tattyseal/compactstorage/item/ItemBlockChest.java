package com.tattyseal.compactstorage.item;

import com.tattyseal.compactstorage.exception.InvalidSizeException;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
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
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
		ItemStack stack = new ItemStack(this, 1);

		NBTTagCompound tag = new NBTTagCompound();
		tag.setIntArray("size", new int[] {9, 3});
		tag.setInteger("hue", 180);

		stack.setTagCompound(tag);
		items.add(stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flagIn)
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


			if(stack.getTagCompound().hasKey("hue"))
			{
				int hue = stack.getTagCompound().getInteger("hue");

				if(hue != -1)
				{
					list.add(TextFormatting.AQUA + "Hue: " + hue);
				}
				else
				{
					list.add(TextFormatting.AQUA + "White");
				}
			}

			if(stack.getTagCompound().hasKey("chestData") && stack.getTagCompound().getCompoundTag("chestData").hasKey("retaining") && stack.getTagCompound().getCompoundTag("chestData").getBoolean("retaining"))
			{
				list.add(TextFormatting.AQUA + "" + TextFormatting.ITALIC + "Retaining");
			}
			else
			{
				list.add(TextFormatting.RED + "" + TextFormatting.ITALIC + "Non-Retaining");
			}
    	}
    	else
    	{
    		list.add(TextFormatting.RED + "Slots: none");
    	}
    	
    	super.addInformation(stack, worldIn, list, flagIn);
    }
}
