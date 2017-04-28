package com.tattyseal.compactstorage.util;

import com.tattyseal.compactstorage.CompactStorage;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

/***
 * Created by tattyseal for 3.0, moved to 2.1-1.8.9 on the 22/03/201
 */
public class ModelUtil
{
    public static void registerItem(Item item, int metadata, String itemName)
    {
        ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
        mesher.register(item, metadata, new ModelResourceLocation(itemName, "inventory"));
    }

    public static void registerChest()
    {
        ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
        Item chestItem = Item.getItemFromBlock(CompactStorage.chest);
        mesher.register(chestItem, 0, new ModelResourceLocation("compactstorage:compactchest", "inventory"));
    }

    public static void registerBlock(Block block, int metadata, String blockName)
    {
        registerItem(Item.getItemFromBlock(block), metadata, blockName);
    }

    public static void registerBlock(Block block, String blockName)
    {
        registerBlock(block, 0, blockName);
    }

    public static void registerItem(Item item, String itemName)
    {
        registerItem(item, 0, itemName);
    }
}
