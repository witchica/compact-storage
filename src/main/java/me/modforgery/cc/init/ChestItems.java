package me.modforgery.cc.init;

import cpw.mods.fml.common.registry.GameRegistry;
import me.modforgery.cc.item.ItemBackpack;
import net.minecraft.item.Item;

/**
 * Created by Toby on 27/08/2014.
 */
public class ChestItems
{
    public static Item backpack;

    public static void init()
    {
        backpack = new ItemBackpack();
        GameRegistry.registerItem(backpack, "backpack");
    }
}
