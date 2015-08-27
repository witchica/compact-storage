package com.tattyseal.compactstorage;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.tattyseal.compactstorage.block.BlockChest;
import com.tattyseal.compactstorage.command.CommandCompactStorage;
import com.tattyseal.compactstorage.compat.ICompat;
import com.tattyseal.compactstorage.creativetabs.CreativeTabCompactStorage;
import com.tattyseal.compactstorage.item.ItemBackpack;
import com.tattyseal.compactstorage.item.ItemBlockChest;
import com.tattyseal.compactstorage.network.HandlerApplyChestUpdate;
import com.tattyseal.compactstorage.network.PacketApplyChestUpdate;
import com.tattyseal.compactstorage.proxy.IProxy;
import com.tattyseal.compactstorage.tileentity.TileEntityChest;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by Toby on 06/11/2014.
 */
@Mod(modid = CompactStorage.ID, name = CompactStorage.NAME, version = CompactStorage.VERSION)
public class CompactStorage
{
    @Instance(CompactStorage.ID)
    public static CompactStorage instance;
    
    public static List<ICompat> compat;

    @SidedProxy(clientSide = CompactStorage.CLIENT_PROXY, serverSide = CompactStorage.SERVER_PROXY, modId = CompactStorage.ID)
    public static IProxy proxy;

    public static CreativeTabs tabCS;

    public static final Logger logger = LogManager.getLogger("CompactStorage");
    public static boolean deobf;

    public SimpleNetworkWrapper wrapper;
    
    public static final String ID = "compactstorage";
    public static final String NAME = "CompactStorage";
    public static final String VERSION = "v2.0.0a";

    public static final String CLIENT_PROXY = "com.tattyseal.compactstorage.proxy.ClientProxy";
    public static final String SERVER_PROXY = "com.tattyseal.compactstorage.proxy.ServerProxy";
    
    public static Block chest;
    
    public static Item backpack;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	compat = Lists.newArrayList();
    	
        OreDictionary.registerOre("barsIron", Blocks.iron_bars);
        OreDictionary.registerOre("blockChest", Blocks.chest);
        OreDictionary.registerOre("itemClay", Items.clay_ball);

        OreDictionary.registerOre("string", Items.string);
        OreDictionary.registerOre("wool", Blocks.wool);
    	
        tabCS = new CreativeTabCompactStorage();
        
        wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(CompactStorage.ID);
        wrapper.registerMessage(HandlerApplyChestUpdate.class, PacketApplyChestUpdate.class, 0, Side.SERVER);
        
        chest = new BlockChest();
        GameRegistry.registerBlock(chest, ItemBlockChest.class, "compactChest");
        GameRegistry.registerTileEntity(TileEntityChest.class, "tileChest");

        backpack = new ItemBackpack();
        GameRegistry.registerItem(backpack, "backpack");
        
        ConfigurationHandler.configFile = event.getSuggestedConfigurationFile();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	for(ICompat icompat : compat)
    	{
    		String modid = icompat.modid();
    		
    		logger.info("Found compatibility for " + modid + " attempting load!");
    		
    		if(Loader.isModLoaded(modid))
    		{
    			try
    			{
    				icompat.registerCompat();
    			}
    			catch(Exception e)
    			{
    				logger.error("Exception " + e.getClass().getName() + " while loading compatibility for " + modid + ".");
    				continue;
    			}
    			
        		logger.info("Loaded compatability for " + modid + ".");
    		}
    		else
    		{
    			logger.warn("Compatability for " + modid + " cannot be loaded as it depends on the mod being installed.");
    		}
    	}
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

        proxy.registerRenderers();
        ConfigurationHandler.init();
    }

    @EventHandler
    public void missingMapping(FMLMissingMappingsEvent event)
    {
    	for(int map = 0; map < event.getAll().size(); map++)
    	{
    		if(event.getAll().get(map).name.startsWith("compactstorage"))
    		{
    			System.out.println("Ignoring missing block/item " + event.getAll().get(map).name);
    			event.getAll().get(map).ignore();
    		}
    	}
    }
    
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandCompactStorage());
    }
}
