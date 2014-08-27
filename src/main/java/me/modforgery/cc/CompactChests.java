package me.modforgery.cc;

import cpw.mods.fml.client.CustomModLoadingErrorDisplayException;
import cpw.mods.fml.client.GuiCustomModLoadingErrorScreen;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import me.modforgery.cc.client.GuiHandler;
import me.modforgery.cc.client.render.ChestItemRenderer;
import me.modforgery.cc.client.render.RenderChest;
import me.modforgery.cc.creativetabs.CreativeTabChest;
import me.modforgery.cc.init.ChestBlocks;
import me.modforgery.cc.init.ChestReferences;
import me.modforgery.cc.network.ChestHandler;
import me.modforgery.cc.network.ChestPacket;
import me.modforgery.cc.proxy.IProxy;
import me.modforgery.cc.tileentity.*;
import net.minecraft.client.renderer.tileentity.TileEntityRendererChestHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;

/**
 * Created by Toby on 19/08/2014.
 */
@Mod(modid = ChestReferences.ID, name = ChestReferences.NAME, version = ChestReferences.VERSION)
public class CompactChests
{
    @SidedProxy(serverSide = "me.modforgery.cc.proxy.Server", clientSide = "me.modforgery.cc.proxy.Client", modId = ChestReferences.ID)
    public static IProxy proxy;

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
        CreativeTabChest.tab = new CreativeTabChest();
        ChestBlocks.init();
    }

    @Mod.EventHandler
    public void postInitialization(FMLPostInitializationEvent fmlPostInitializationEvent)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(ChestReferences.ID);

        networkWrapper.registerMessage(ChestHandler.class, ChestPacket.class, 0, Side.CLIENT);

        proxy.registerRenderers();

        GameRegistry.addRecipe(new ItemStack(ChestBlocks.doubleChest, 1), "WWW", "WCW", "WWW", 'W', new ItemStack(Blocks.planks, 1), 'C', new ItemStack(Blocks.chest, 1));
        GameRegistry.addRecipe(new ItemStack(ChestBlocks.tripleChest, 1), "WWW", "WCW", "WWW", 'W', new ItemStack(Blocks.planks, 1), 'C', new ItemStack(ChestBlocks.doubleChest, 1));
        GameRegistry.addRecipe(new ItemStack(ChestBlocks.quadrupleChest, 1), "WWW", "WCW", "WWW", 'W', new ItemStack(Blocks.planks, 1), 'C', new ItemStack(ChestBlocks.tripleChest, 1));
        GameRegistry.addRecipe(new ItemStack(ChestBlocks.quintupleChest, 1), "WWW", "WCW", "WWW", 'W', new ItemStack(Blocks.planks, 1), 'C', new ItemStack(ChestBlocks.quadrupleChest, 1));
    }

    /**
     * Getters and Setters
     */

    public static CompactChests instance()
    {
        return instance;
    }
}
