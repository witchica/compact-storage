package com.tattyseal.compactstorage.util;

import com.tattyseal.compactstorage.CompactStorage;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

/***
 * Created by tattyseal for 3.0, moved to 2.1-1.8.9 on the 22/03/201
 */
public class ModelUtil
{
    public static void registerItem(Item item, int metadata, String itemName)
    {
        ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(itemName, "inventory"));
    }

    public static void registerChest()
    {
        Item chestItem = Item.getItemFromBlock(CompactStorage.ModBlocks.chest);
        ModelLoader.setCustomModelResourceLocation(chestItem, 0, new ModelResourceLocation("compactstorage:compactchest", "inventory"));
    }

    public static void registerBlock(Block block, int metadata, String blockName)
    {
        registerItem(Item.getItemFromBlock(block), metadata, blockName);
    }
}
