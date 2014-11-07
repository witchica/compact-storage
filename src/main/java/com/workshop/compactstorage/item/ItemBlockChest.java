package com.workshop.compactstorage.item;

import com.workshop.compactstorage.block.ChestType;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * Created by Toby on 06/11/2014.
 */
public class ItemBlockChest extends ItemBlock
{
    public ItemBlockChest(Block block)
    {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return "block." + ChestType.values()[stack.getItemDamage()].name + "Chest";
    }
}
