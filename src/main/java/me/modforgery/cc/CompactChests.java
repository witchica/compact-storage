package me.modforgery.cc;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import me.modforgery.cc.client.GuiHandler;
import me.modforgery.cc.client.render.RenderChest;
import me.modforgery.cc.client.render.RenderItemChest;
import me.modforgery.cc.init.ChestBlocks;
import me.modforgery.cc.init.ChestReferences;
import me.modforgery.cc.network.ChestHandler;
import me.modforgery.cc.network.ChestPacket;
import me.modforgery.cc.tileentity.TileEntityDoubleChest;
import me.modforgery.cc.tileentity.TileEntityQuadrupleChest;
import me.modforgery.cc.tileentity.TileEntityQuintupleChest;
import me.modforgery.cc.tileentity.TileEntityTripleChest;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.MinecraftForgeClient;

/**
 * Created by Toby on 19/08/2014.
 */
@Mod(modid = ChestReferences.ID, name = ChestReferences.NAME, version = ChestReferences.VERSION)
public class CompactChests
{
    @Mod.Instance(ChestReferences.ID)
    private static CompactChests instance;

    public static SimpleNetworkWrapper networkWrapper;

    @Mod.EventHandler
    public void preInitialization(FMLPreInitializationEvent fmlPreInitializationEvent)
    {

    }

    @Mod.EventHandler
    public void initialization(FMLInitializationEvent fmlInitializationEvent)
    {
        ChestBlocks.init();
    }

    @Mod.EventHandler
    public void postInitialization(FMLPostInitializationEvent fmlPostInitializationEvent)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(ChestReferences.ID);

        networkWrapper.registerMessage(ChestHandler.class, ChestPacket.class, 0, Side.CLIENT);

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDoubleChest.class, new RenderChest("double"));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTripleChest.class, new RenderChest("triple"));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityQuadrupleChest.class, new RenderChest("quadruple"));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityQuintupleChest.class, new RenderChest("quintuple"));

        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(ChestBlocks.doubleChest), new RenderItemChest("double"));
    }

    /**
     * Getters and Setters
     */

    public static CompactChests instance()
    {
        return instance;
    }
}
