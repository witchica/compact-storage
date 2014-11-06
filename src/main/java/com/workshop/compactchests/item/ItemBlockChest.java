package com.workshop.compactchests.item;

import com.workshop.compactchests.block.ChestType;
import com.workshop.compactchests.essential.CompactStorage;
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
