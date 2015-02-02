package com.workshop.compactchests.proxy;

import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.MinecraftForgeClient;

import com.workshop.compactchests.client.render.ChestItemRenderer;
import com.workshop.compactchests.client.render.RenderChest;
import com.workshop.compactchests.init.ChestBlocks;
import com.workshop.compactchests.tileentity.TileEntityDoubleChest;
import com.workshop.compactchests.tileentity.TileEntityQuadrupleChest;
import com.workshop.compactchests.tileentity.TileEntityQuintupleChest;
import com.workshop.compactchests.tileentity.TileEntitySextupleChest;
import com.workshop.compactchests.tileentity.TileEntityTripleChest;

import cpw.mods.fml.client.registry.ClientRegistry;

/**
 * Created by Toby on 25/08/2014.
 */
public class Client implements IProxy
{
    @Override
    public void registerRenderers()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDoubleChest.class, new RenderChest("double"));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTripleChest.class, new RenderChest("triple"));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityQuadrupleChest.class, new RenderChest("quadruple"));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityQuintupleChest.class, new RenderChest("quintuple"));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySextupleChest.class, new RenderChest("sextuple"));

        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(ChestBlocks.doubleChest), new ChestItemRenderer("double"));
        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(ChestBlocks.tripleChest), new ChestItemRenderer("triple"));
        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(ChestBlocks.quadrupleChest), new ChestItemRenderer("quadruple"));
        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(ChestBlocks.quintupleChest), new ChestItemRenderer("quintuple"));
        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(ChestBlocks.sextupleChest), new ChestItemRenderer("sextuple"));

    }

    @Override
    public String side()
    {
        return "CLIENT";
    }
}
