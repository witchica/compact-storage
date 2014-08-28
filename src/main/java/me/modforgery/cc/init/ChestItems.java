package me.modforgery.cc.init;

import cpw.mods.fml.common.registry.GameRegistry;
import me.modforgery.cc.item.*;
import net.minecraft.item.Item;

/**
 * Created by Toby on 27/08/2014.
 */
public class ChestItems
{
    public static Item single_backpack;
    public static Item double_backpack;
    public static Item triple_backpack;
    public static Item quadruple_backpack;
    public static Item quintuple_backpack;

    public static void init()
    {
        single_backpack = new ItemSingleBackpack();
        GameRegistry.registerItem(single_backpack, "single_backpack");

        double_backpack = new ItemDoubleBackpack();
        GameRegistry.registerItem(double_backpack, "double_backpack");

        triple_backpack = new ItemTripleBackpack();
        GameRegistry.registerItem(triple_backpack, "triple_backpack");

        quadruple_backpack = new ItemQuadrupleBackpack();
        GameRegistry.registerItem(quadruple_backpack, "quadruple_backpack");

        quintuple_backpack = new ItemQuintupleBackpack();
        GameRegistry.registerItem(quintuple_backpack, "quintuple_backpack");
    }
}
