package com.workshop.compactstorage.essential.proxy;

import com.workshop.compactstorage.client.render.ChestItemRenderer;
import com.workshop.compactstorage.client.render.TileEntityChestRenderer;
import com.workshop.compactstorage.compat.RefinedRelocationCompat;
import com.workshop.compactstorage.essential.handler.FirstTimeRunHandler;
import com.workshop.compactstorage.essential.init.StorageBlocks;
import com.workshop.compactstorage.tileentity.TileEntityChest;
import com.workshop.compactstorage.tileentity.TileEntitySortingChest;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Toby on 06/11/2014.
 */
public class ClientProxy implements IProxy
{
    public void registerRenderers()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChest.class, new TileEntityChestRenderer());
        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(StorageBlocks.chest), new ChestItemRenderer());

        if (Loader.isModLoaded("RefinedRelocation"))
        {
            ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySortingChest.class, new TileEntityChestRenderer());
            MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(RefinedRelocationCompat.sortingChest), new ChestItemRenderer());
        }

        MinecraftForge.EVENT_BUS.register(new FirstTimeRunHandler());
        FMLCommonHandler.instance().bus().register(new FirstTimeRunHandler());
    }
}
