package com.workshop.compactstorage.essential;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.workshop.compactchests.CompactChests;
import com.workshop.compactchests.init.ChestBlocks;
import com.workshop.compactchests.init.ChestItems;
import com.workshop.compactstorage.compat.ICompat;
import com.workshop.compactstorage.compat.JabbaCompat;
import com.workshop.compactstorage.compat.WailaCompat;
import com.workshop.compactstorage.creativetabs.CreativeTabCompactStorage;
import com.workshop.compactstorage.essential.handler.ConfigurationHandler;
import com.workshop.compactstorage.essential.handler.FirstTimeRunHandler;
import com.workshop.compactstorage.essential.handler.GuiHandler;
import com.workshop.compactstorage.essential.init.StorageBlocks;
import com.workshop.compactstorage.essential.init.StorageInfo;
import com.workshop.compactstorage.essential.init.StorageItems;
import com.workshop.compactstorage.essential.proxy.IProxy;
import com.workshop.compactstorage.network.handler.C01HandlerUpdateBuilder;
import com.workshop.compactstorage.network.handler.C02HandlerCraftChest;
import com.workshop.compactstorage.network.packet.C01PacketUpdateBuilder;
import com.workshop.compactstorage.network.packet.C02PacketCraftChest;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * Created by Toby on 06/11/2014.
 */
@Mod(modid = StorageInfo.ID, name = StorageInfo.NAME, version = StorageInfo.VERSION)
public class CompactStorage
{
    @Instance(StorageInfo.ID)
    public static CompactStorage instance;
    
    public static List<ICompat> compat;

    public static CompactChests legacy_instance;

    @SidedProxy(clientSide = StorageInfo.CLIENT_PROXY, serverSide = StorageInfo.SERVER_PROXY, modId = StorageInfo.ID)
    public static IProxy proxy;

    public static CreativeTabs tabCS;

    public static final Logger logger = LogManager.getLogger("CompactStorage");
    public static boolean deobf;

    public SimpleNetworkWrapper wrapper;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	compat = Lists.newArrayList();
    	
    	if(Loader.isModLoaded("JABBA")) compat.add(new JabbaCompat());
    	if(Loader.isModLoaded("Waila")) compat.add(new WailaCompat());
    	
        try
        {
            deobf = true; //Class.forName("net.minecraft.world.World") == null ? false : true;
        }
        catch(Exception ex)
        {
            logger.warn("Could not set deobf variable. Assuming normal game.");
        }
        
        OreDictionary.registerOre("barsIron", Blocks.iron_bars);
        OreDictionary.registerOre("blockChest", Blocks.chest);
        OreDictionary.registerOre("itemClay", Items.clay_ball);

        OreDictionary.registerOre("string", Items.string);
        OreDictionary.registerOre("wool", Blocks.wool);

        logger.info("Are we in deofb? " + (deobf ? "Yep!" : "Nope, going retro!"));

        legacy_instance = new CompactChests();
        
        wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(StorageInfo.ID);
        wrapper.registerMessage(C01HandlerUpdateBuilder.class, C01PacketUpdateBuilder.class, 0, Side.SERVER);
        wrapper.registerMessage(C02HandlerCraftChest.class, C02PacketCraftChest.class, 1, Side.SERVER);
        
        switch(FMLCommonHandler.instance().getEffectiveSide())
        {
            case CLIENT: legacy_instance.proxy = new com.workshop.compactchests.proxy.Client(); break;
            case SERVER: legacy_instance.proxy = new com.workshop.compactchests.proxy.Server(); break;
        }


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
    	
        tabCS = new CreativeTabCompactStorage();

        ChestBlocks.init();
        StorageBlocks.init();

        ChestItems.init();
        StorageItems.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

        proxy.registerRenderers();
        legacy_instance.postInitialization(event);
        
        MinecraftForge.EVENT_BUS.register(new FirstTimeRunHandler());
        FMLCommonHandler.instance().bus().register(new FirstTimeRunHandler());

        GameRegistry.addRecipe(new ItemStack(StorageBlocks.chestBuilder, 1), "ILI", "ICI", "ILI", 'I', new ItemStack(Items.iron_ingot, 1), 'C', new ItemStack(Blocks.chest, 1), 'L', new ItemStack(Blocks.lever, 1));

        ConfigurationHandler.init();
    }
}
