package com.workshop.compactchests;

import com.workshop.compactstorage.essential.CompactStorage;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import com.workshop.compactchests.client.GuiHandler;
import com.workshop.compactchests.configuration.ConfigurationHandler;
import com.workshop.compactchests.event.CCHandler;
import com.workshop.compactchests.proxy.IProxy;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Toby on 19/08/2014.
 */
public class CompactChests
{
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInitialization(FMLPreInitializationEvent fmlPreInitializationEvent)
    {
        FMLCommonHandler.instance().bus().register(new ConfigurationHandler());
        ConfigurationHandler.init(fmlPreInitializationEvent.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void initialization(FMLInitializationEvent fmlInitializationEvent)
    {

    }

    @Mod.EventHandler
    public void postInitialization(FMLPostInitializationEvent fmlPostInitializationEvent)
    {
        proxy.registerRenderers();

        MinecraftForge.EVENT_BUS.register(new CCHandler());

        NetworkRegistry.INSTANCE.registerGuiHandler(CompactStorage.instance, new GuiHandler());
    }
}
