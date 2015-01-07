package com.workshop.compactstorage.essential.init;

import net.minecraft.block.Block;

import com.workshop.compactstorage.block.BlockChest;
import com.workshop.compactstorage.block.BlockChestBuilder;
import com.workshop.compactstorage.item.ItemBlockChest;
import com.workshop.compactstorage.tileentity.TileEntityChest;
import com.workshop.compactstorage.tileentity.TileEntityChestBuilder;

import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Created by Toby on 06/11/2014.
 */
public class StorageBlocks
{
    public static Block chest;
    public static Block chestBuilder;

    public static void init()
    {
        chest = new BlockChest();
        GameRegistry.registerBlock(chest, ItemBlockChest.class, "compactChest");
        GameRegistry.registerTileEntity(TileEntityChest.class, "tileChest");
        
        chestBuilder = new BlockChestBuilder();
        GameRegistry.registerBlock(chestBuilder, "chestBuilder");
        GameRegistry.registerTileEntity(TileEntityChestBuilder.class, "tileChestBuilder");
    }
}
