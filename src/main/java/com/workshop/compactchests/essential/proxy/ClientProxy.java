package com.workshop.compactchests.essential.proxy;

import com.workshop.compactchests.client.render.TileEntityChestRenderer;
import com.workshop.compactchests.tileentity.TileEntityChest;
import cpw.mods.fml.client.registry.ClientRegistry;

/**
 * Created by Toby on 06/11/2014.
 */
public class ClientProxy implements IProxy
{
    public void registerRenderers()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChest.class, new TileEntityChestRenderer());
    }
}
