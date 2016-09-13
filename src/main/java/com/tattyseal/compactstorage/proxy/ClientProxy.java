package com.tattyseal.compactstorage.proxy;

import com.tattyseal.compactstorage.CompactStorage;
import com.tattyseal.compactstorage.client.render.TileEntityChestRenderer;
import com.tattyseal.compactstorage.tileentity.TileEntityChest;
import com.tattyseal.compactstorage.util.ModelUtil;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * Created by Toby on 06/11/2014.
 */
public class ClientProxy implements IProxy
{
    public void registerRenderers()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChest.class, new TileEntityChestRenderer());
        ModelUtil.registerChest();
        ModelUtil.registerBlock(CompactStorage.chestBuilder, 0, "compactstorage:chestBuilder");
        ModelUtil.registerItem(CompactStorage.backpack, 0, "compactstorage:backpack");
        ModelUtil.registerItem(CompactStorage.storage, 0, "compactstorage:storage");
    }
}
