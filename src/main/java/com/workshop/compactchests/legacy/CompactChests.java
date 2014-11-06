package com.workshop.compactchests.legacy;

import com.workshop.compactchests.essential.CompactStorage;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import com.workshop.compactchests.legacy.client.GuiHandler;
import com.workshop.compactchests.legacy.configuration.ConfigurationHandler;
import com.workshop.compactchests.legacy.creativetabs.CreativeTabChest;
import com.workshop.compactchests.legacy.event.CCHandler;
import com.workshop.compactchests.legacy.init.ChestBlocks;
import com.workshop.compactchests.legacy.init.ChestItems;
import com.workshop.compactchests.legacy.init.ChestReferences;
import com.workshop.compactchests.legacy.network.ChestHandler;
import com.workshop.compactchests.legacy.network.ChestPacket;
import com.workshop.compactchests.legacy.proxy.IProxy;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
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
