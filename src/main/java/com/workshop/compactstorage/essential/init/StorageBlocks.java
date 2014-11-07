package com.workshop.compactstorage.essential.init;

import com.workshop.compactstorage.block.BlockChest;
import com.workshop.compactstorage.item.ItemBlockChest;
import com.workshop.compactstorage.tileentity.TileEntityChest;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

/**
 * Created by Toby on 06/11/2014.
 */
public class StorageBlocks
{
    public static Block chest;

    public static void init()
    {
        chest = new BlockChest();
        GameRegistry.registerBlock(chest, ItemBlockChest.class, "compactChest");
        GameRegistry.registerTileEntity(TileEntityChest.class, "tileChest");
    }
}
