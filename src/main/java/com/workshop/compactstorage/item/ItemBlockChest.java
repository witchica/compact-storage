package com.workshop.compactstorage.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

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
    public String getUnlocalizedName()
    {
        return "block.configChest.name";
    }
}
