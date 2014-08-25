package me.modforgery.cc.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import me.modforgery.cc.client.render.ChestItemRenderer;
import me.modforgery.cc.client.render.RenderChest;
import me.modforgery.cc.init.ChestBlocks;
import me.modforgery.cc.tileentity.TileEntityDoubleChest;
import me.modforgery.cc.tileentity.TileEntityQuadrupleChest;
import me.modforgery.cc.tileentity.TileEntityQuintupleChest;
import me.modforgery.cc.tileentity.TileEntityTripleChest;
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

        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(ChestBlocks.doubleChest), new ChestItemRenderer("double"));
        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(ChestBlocks.tripleChest), new ChestItemRenderer("triple"));
        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(ChestBlocks.quadrupleChest), new ChestItemRenderer("quadruple"));
        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(ChestBlocks.quintupleChest), new ChestItemRenderer("quintuple"));
    }

    @Override
    public String side()
    {
        return "CLIENT";
    }
}
