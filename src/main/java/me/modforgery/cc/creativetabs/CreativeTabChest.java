package me.modforgery.cc.creativetabs;

import me.modforgery.cc.init.ChestBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Created by Toby on 24/08/2014.
 */
public class CreativeTabChest extends CreativeTabs
{
    public static CreativeTabs tab;

    public CreativeTabChest()
    {
        super(CreativeTabs.getNextID(), "chest");
    }

    @Override
    public Item getTabIconItem()
    {
        return Item.getItemFromBlock(ChestBlocks.quintupleChest);
    }
}
