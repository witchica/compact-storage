package me.modforgery.cc.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import me.modforgery.cc.client.render.ChestItemRenderer;
import me.modforgery.cc.client.render.RenderChest;
import me.modforgery.cc.init.ChestBlocks;
import me.modforgery.cc.tileentity.*;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.MinecraftForgeClient;

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
