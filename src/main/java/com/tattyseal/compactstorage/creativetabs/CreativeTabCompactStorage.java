package com.tattyseal.compactstorage.creativetabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by Toby on 07/11/2014.
 */
public class CreativeTabCompactStorage extends CreativeTabs
{
    public CreativeTabCompactStorage()
    {
        super(getNextID(), "compactStorage");
    }

    @Override
    public Item getTabIconItem()
    {
        return new ItemStack(Blocks.CHEST, 1, 0).getItem();
    }
}
