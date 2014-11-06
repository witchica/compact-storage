package com.workshop.compactchests.legacy;

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
//@Mod(modid = ChestReferences.ID, name = ChestReferences.NAME, version = ChestReferences.VERSION, guiFactory = ChestReferences.FACTORY)
public class CompactChests
{
    @SidedProxy(serverSide = "com.workshop.compactchests.legacy.proxy.Server", clientSide = "com.workshop.compactchests.legacy.proxy.Client", modId = ChestReferences.ID)
    public static IProxy proxy;

    @Mod.Instance(ChestReferences.ID)
    private static CompactChests instance;

    public static SimpleNetworkWrapper networkWrapper;

    public static int difficulty;

    @Mod.EventHandler
    public void preInitialization(FMLPreInitializationEvent fmlPreInitializationEvent)
    {
        FMLCommonHandler.instance().bus().register(new ConfigurationHandler());
        ConfigurationHandler.init(fmlPreInitializationEvent.getSuggestedConfigurationFile());

        System.out.println(difficulty);
    }

    @Mod.EventHandler
    public void initialization(FMLInitializationEvent fmlInitializationEvent)
    {
        CreativeTabChest.tab = new CreativeTabChest();
        ChestBlocks.init();
        ChestItems.init();
    }

    @Mod.EventHandler
    public void postInitialization(FMLPostInitializationEvent fmlPostInitializationEvent)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(ChestReferences.ID);

        networkWrapper.registerMessage(ChestHandler.class, ChestPacket.class, 0, Side.CLIENT);

        proxy.registerRenderers();

        MinecraftForge.EVENT_BUS.register(new CCHandler());

        switch(difficulty)
        {
            case 0:
            {
                GameRegistry.addRecipe(new ItemStack(ChestBlocks.doubleChest, 1), "WWW", "WCW", "WWW", 'W', new ItemStack(Blocks.planks, 1), 'C', new ItemStack(Blocks.chest, 1));
                GameRegistry.addRecipe(new ItemStack(ChestBlocks.tripleChest, 1), "WWW", "WCW", "WWW", 'W', new ItemStack(Blocks.planks, 1), 'C', new ItemStack(ChestBlocks.doubleChest, 1));
                GameRegistry.addRecipe(new ItemStack(ChestBlocks.quadrupleChest, 1), "WWW", "WCW", "WWW", 'W', new ItemStack(Blocks.planks, 1), 'C', new ItemStack(ChestBlocks.tripleChest, 1));
                GameRegistry.addRecipe(new ItemStack(ChestBlocks.quintupleChest, 1), "WWW", "WCW", "WWW", 'W', new ItemStack(Blocks.planks, 1), 'C', new ItemStack(ChestBlocks.quadrupleChest, 1));
                break;
            }
            case 1:
            {
                GameRegistry.addRecipe(new ItemStack(ChestBlocks.doubleChest, 1), "WWW", "WCW", "WWW", 'W', new ItemStack(Items.iron_ingot, 1), 'C', new ItemStack(Blocks.chest, 1));
                GameRegistry.addRecipe(new ItemStack(ChestBlocks.tripleChest, 1), "WWW", "WCW", "WWW", 'W', new ItemStack(Items.iron_ingot, 1), 'C', new ItemStack(ChestBlocks.doubleChest, 1));
                GameRegistry.addRecipe(new ItemStack(ChestBlocks.quadrupleChest, 1), "WWW", "WCW", "WWW", 'W', new ItemStack(Items.iron_ingot, 1), 'C', new ItemStack(ChestBlocks.tripleChest, 1));
                GameRegistry.addRecipe(new ItemStack(ChestBlocks.quintupleChest, 1), "WWW", "WCW", "WWW", 'W', new ItemStack(Items.iron_ingot, 1), 'C', new ItemStack(ChestBlocks.quadrupleChest, 1));
                break;
            }
            case 2:
            {
                GameRegistry.addRecipe(new ItemStack(ChestBlocks.doubleChest, 1), "WWW", "WCW", "WWW", 'W', new ItemStack(Items.gold_ingot, 1), 'C', new ItemStack(Blocks.chest, 1));
                GameRegistry.addRecipe(new ItemStack(ChestBlocks.tripleChest, 1), "WWW", "WCW", "WWW", 'W', new ItemStack(Items.gold_ingot, 1), 'C', new ItemStack(ChestBlocks.doubleChest, 1));
                GameRegistry.addRecipe(new ItemStack(ChestBlocks.quadrupleChest, 1), "WWW", "WCW", "WWW", 'W', new ItemStack(Items.gold_ingot, 1), 'C', new ItemStack(ChestBlocks.tripleChest, 1));
                GameRegistry.addRecipe(new ItemStack(ChestBlocks.quintupleChest, 1), "WWW", "WCW", "WWW", 'W', new ItemStack(Items.gold_ingot, 1), 'C', new ItemStack(ChestBlocks.quadrupleChest, 1));
                break;
            }
            case 3:
            {
                GameRegistry.addRecipe(new ItemStack(ChestBlocks.doubleChest, 1), "WWW", "WCW", "WWW", 'W', new ItemStack(Items.diamond, 1), 'C', new ItemStack(Blocks.chest, 1));
                GameRegistry.addRecipe(new ItemStack(ChestBlocks.tripleChest, 1), "WWW", "WCW", "WWW", 'W', new ItemStack(Items.diamond, 1), 'C', new ItemStack(ChestBlocks.doubleChest, 1));
                GameRegistry.addRecipe(new ItemStack(ChestBlocks.quadrupleChest, 1), "WWW", "WCW", "WWW", 'W', new ItemStack(Items.diamond, 1), 'C', new ItemStack(ChestBlocks.tripleChest, 1));
                GameRegistry.addRecipe(new ItemStack(ChestBlocks.quintupleChest, 1), "WWW", "WCW", "WWW", 'W', new ItemStack(Items.diamond, 1), 'C', new ItemStack(ChestBlocks.quadrupleChest, 1));
                break;
            }
            case 4:
            {
                GameRegistry.addRecipe(new ItemStack(ChestBlocks.doubleChest, 1), "WWW", "WCW", "WWW", 'W', new ItemStack(Blocks.obsidian, 1), 'C', new ItemStack(Blocks.chest, 1));
                GameRegistry.addRecipe(new ItemStack(ChestBlocks.tripleChest, 1), "WWW", "WCW", "WWW", 'W', new ItemStack(Blocks.obsidian, 1), 'C', new ItemStack(ChestBlocks.doubleChest, 1));
                GameRegistry.addRecipe(new ItemStack(ChestBlocks.quadrupleChest, 1), "WWW", "WCW", "WWW", 'W', new ItemStack(Blocks.obsidian, 1), 'C', new ItemStack(ChestBlocks.tripleChest, 1));
                GameRegistry.addRecipe(new ItemStack(ChestBlocks.quintupleChest, 1), "WWW", "WCW", "WWW", 'W', new ItemStack(Blocks.obsidian, 1), 'C', new ItemStack(ChestBlocks.quadrupleChest, 1));
                break;
            }
            case 5:
            {
                GameRegistry.addRecipe(new ItemStack(ChestBlocks.doubleChest, 1), "WWW", "WCW", "WWW", 'W', new ItemStack(Items.emerald, 1), 'C', new ItemStack(Blocks.chest, 1));
                GameRegistry.addRecipe(new ItemStack(ChestBlocks.tripleChest, 1), "WWW", "WCW", "WWW", 'W', new ItemStack(Items.emerald, 1), 'C', new ItemStack(ChestBlocks.doubleChest, 1));
                GameRegistry.addRecipe(new ItemStack(ChestBlocks.quadrupleChest, 1), "WWW", "WCW", "WWW", 'W', new ItemStack(Items.emerald, 1), 'C', new ItemStack(ChestBlocks.tripleChest, 1));
                GameRegistry.addRecipe(new ItemStack(ChestBlocks.quintupleChest, 1), "WWW", "WCW", "WWW", 'W', new ItemStack(Items.emerald, 1), 'C', new ItemStack(ChestBlocks.quadrupleChest, 1));
                break;
            }
        }

        GameRegistry.addRecipe(new ItemStack(ChestItems.single_backpack, 1), "WSW", "SCS", "WSW", 'C', new ItemStack(Blocks.chest, 1), 'W', new ItemStack(Blocks.wool, 1), 'S', new ItemStack(Items.string, 1));
        GameRegistry.addRecipe(new ItemStack(ChestItems.double_backpack, 1), "WSW", "SCS", "WSW", 'C', new ItemStack(ChestBlocks.doubleChest, 1), 'W', new ItemStack(Blocks.wool, 1), 'S', new ItemStack(Items.string, 1));
        GameRegistry.addRecipe(new ItemStack(ChestItems.triple_backpack, 1), "WSW", "SCS", "WSW", 'C', new ItemStack(ChestBlocks.tripleChest, 1), 'W', new ItemStack(Blocks.wool, 1), 'S', new ItemStack(Items.string, 1));
        GameRegistry.addRecipe(new ItemStack(ChestItems.quadruple_backpack, 1), "WSW", "SCS", "WSW", 'C', new ItemStack(ChestBlocks.quadrupleChest, 1), 'W', new ItemStack(Blocks.wool, 1), 'S', new ItemStack(Items.string, 1));
        GameRegistry.addRecipe(new ItemStack(ChestItems.quintuple_backpack, 1), "WSW", "SCS", "WSW", 'C', new ItemStack(ChestBlocks.quintupleChest, 1), 'W', new ItemStack(Blocks.wool, 1), 'S', new ItemStack(Items.string, 1));
    }

    /**
     * Getters and Setters
     */

    public static CompactChests instance()
    {
        return instance;
    }
}
