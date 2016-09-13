package com.tattyseal.compactstorage.item;

import com.tattyseal.compactstorage.CompactStorage;
import net.minecraft.item.Item;

/**
 * Created by tattyseal on 13/09/2016.
 */
public class ItemStorage extends Item
{
    public ItemStorage()
    {
        super();
        setUnlocalizedName("storage");
        setCreativeTab(CompactStorage.tabCS);
        setMaxStackSize(1);
        setRegistryName("storage");
    }
}
