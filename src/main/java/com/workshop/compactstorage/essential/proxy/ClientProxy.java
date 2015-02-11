package com.workshop.compactstorage.essential.proxy;

import com.workshop.compactstorage.client.render.ChestItemRenderer;
import com.workshop.compactstorage.client.render.TileEntityChestRenderer;
import com.workshop.compactstorage.essential.init.StorageBlocks;
import com.workshop.compactstorage.tileentity.TileEntityChest;

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
        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(StorageBlocks.chest), new ChestItemRenderer());
    }
}
