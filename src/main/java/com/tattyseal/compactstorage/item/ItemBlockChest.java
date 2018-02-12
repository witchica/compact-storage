package com.tattyseal.compactstorage.item;

import com.tattyseal.compactstorage.CompactStorage;
import com.tattyseal.compactstorage.exception.InvalidSizeException;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
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
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items)
    {
		if(tab == CompactStorage.tabCS)
		{
			ItemStack stack = new ItemStack(this, 1);

			NBTTagCompound tag = new NBTTagCompound();
			tag.setIntArray("size", new int[] {9, 3});
			tag.setInteger("hue", 180);

			stack.setTagCompound(tag);
			items.add(stack);
		}
    }

    @Override
    public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<String> list, @Nonnull ITooltipFlag flagIn)
    {
    	if(stack.hasTagCompound())
    	{
			if(stack.getTagCompound().getTag("size") instanceof NBTTagIntArray)
			{
				int size = stack.getTagCompound().getIntArray("size")[0] * stack.getTagCompound().getIntArray("size")[1];
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
