package com.workshop.compactstorage.creativetabs;

import com.workshop.compactstorage.essential.init.StorageBlocks;
import net.minecraft.creativetab.CreativeTabs;
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
        return new ItemStack(StorageBlocks.chest, 1, 0).getItem();
    }
}
