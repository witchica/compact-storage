package com.tattyseal.compactstorage.proxy;

import com.tattyseal.compactstorage.CompactStorage;
import com.tattyseal.compactstorage.client.render.ChestItemRenderer;
import com.tattyseal.compactstorage.client.render.TileEntityChestRenderer;
import com.tattyseal.compactstorage.tileentity.TileEntityChest;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.MinecraftForgeClient;

/**
 * Created by Toby on 06/11/2014.
 */
public class ClientProxy implements IProxy
{
    public void registerRenderers()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChest.class, new TileEntityChestRenderer());
        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(CompactStorage.chest), new ChestItemRenderer());
    }
}
