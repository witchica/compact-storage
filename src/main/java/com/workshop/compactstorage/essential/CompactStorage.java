package com.workshop.compactstorage.essential;

import cofh.CoFHCore;
import cofh.lib.CoFHLibProps;

import com.workshop.compactchests.CompactChests;
import com.workshop.compactchests.init.ChestBlocks;
import com.workshop.compactchests.init.ChestItems;
import com.workshop.compactstorage.creativetabs.CreativeTabCompactStorage;
import com.workshop.compactstorage.essential.handler.ConfigurationHandler;
import com.workshop.compactstorage.essential.handler.GuiHandler;
import com.workshop.compactstorage.essential.init.StorageBlocks;
import com.workshop.compactstorage.essential.init.StorageInfo;
import com.workshop.compactstorage.essential.init.StorageItems;
import com.workshop.compactstorage.essential.proxy.IProxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Toby on 06/11/2014.
 */
@Mod(modid = StorageInfo.ID, name = StorageInfo.NAME, version = StorageInfo.VERSION)
public class CompactStorage
{
    @Instance(StorageInfo.ID)
    public static CompactStorage instance;

    public static CompactChests legacy_instance;

    @SidedProxy(clientSide = StorageInfo.CLIENT_PROXY, serverSide = StorageInfo.SERVER_PROXY, modId = StorageInfo.ID)
    public static IProxy proxy;

    public static CreativeTabs tabCS;

    public static final Logger logger = LogManager.getLogger("CompactStorage");
    public static boolean deobf;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        try
        {
            deobf = false; //FORCE THIS ON BUILD! SORRY!!! Class.forName("net.minecraft.world.World") == null ? false : true;
        }
        catch(Exception ex)
        {
            logger.warn("Could not set deobf variable. Assuming normal game.");
        }

        logger.info("Are we in deofb? " + (deobf ? "Yep!" : "Nope, going retro!"));

        legacy_instance = new CompactChests();

        switch(FMLCommonHandler.instance().getEffectiveSide())
        {
            case CLIENT: legacy_instance.proxy = new com.workshop.compactchests.proxy.Client(); break;
            case SERVER: legacy_instance.proxy = new com.workshop.compactchests.proxy.Server(); break;
        }

        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        tabCS = new CreativeTabCompactStorage();

        ChestBlocks.init();
        StorageBlocks.init();

        ChestItems.init();
        StorageItems.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        if(deobf)
        {
            NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
        }
        else
        {
            NetworkRegistry.INSTANCE.registerGuiHandler(instance, new com.workshop.compactchests.client.GuiHandler());


            GameRegistry.addShapedRecipe(new ItemStack(ChestBlocks.doubleChest), "SIS", "ICI", "SIS", 'S', new ItemStack(Items.stick, 1), 'I', new ItemStack(Items.iron_ingot, 1), 'C', new ItemStack(Blocks.chest, 1));
            GameRegistry.addShapedRecipe(new ItemStack(ChestBlocks.tripleChest), "SIS", "ICI", "SIS", 'S', new ItemStack(Blocks.cobblestone_wall, 1), 'I', new ItemStack(Items.iron_ingot, 1), 'C', new ItemStack(ChestBlocks.doubleChest, 1));
            GameRegistry.addShapedRecipe(new ItemStack(ChestBlocks.quadrupleChest), "SIS", "ICI", "SIS", 'S', new ItemStack(Blocks.cobblestone_wall, 1), 'I', new ItemStack(Items.gold_ingot, 1), 'C', new ItemStack(ChestBlocks.tripleChest, 1));
            GameRegistry.addShapedRecipe(new ItemStack(ChestBlocks.quintupleChest), "SIS", "ICI", "SIS", 'S', new ItemStack(Blocks.glass_pane, 1), 'I', new ItemStack(Items.gold_ingot, 1), 'C', new ItemStack(ChestBlocks.quadrupleChest, 1));
            GameRegistry.addShapedRecipe(new ItemStack(ChestBlocks.sextupleChest), "SIS", "ICI", "SIS", 'S', new ItemStack(Blocks.glass_pane, 1), 'I', new ItemStack(Items.diamond, 1), 'C', new ItemStack(ChestBlocks.quintupleChest, 1));
        }

        Side side = FMLCommonHandler.instance().getEffectiveSide();

        proxy.registerRenderers();

        if(side.equals(Side.CLIENT))
        {
            if(!ConfigurationHandler.changeNBTForWorldsClient)
            {
                logger.info("Changing NBT is disabled. Will not check. Things may be lost...");
                return;
            }

            for (File file : new File("./saves/").listFiles())
            {
                if (file.isDirectory())
                {
                    changeModidForLevelData(new File("./saves/" + file.getName() + "/level.dat"));
                }
            }
        }
        else
        {
            if(ConfigurationHandler.checkAllDirectoriesServer)
            {
                List<File> directories = new ArrayList<File>();

                for(File dir : new File("./").listFiles())
                {
                    if(dir.isDirectory()) directories.add(dir);
                }

                Object[] fileObjArray = directories.toArray();

                for(Object fileObj : fileObjArray)
                {
                    File dir = (File) fileObj;

                    for(File file : dir.listFiles())
                    {
                        if(file.isDirectory()) continue;

                        logger.info("Searching file " + file.getName() + " in directory " + file.getParentFile().getName() + " to see if it contains NBT data...");

                        if(file.getName().equals("level.dat"))
                        {
                            logger.info("Found file " + file.getName() + " in directory " + file.getParentFile().getName() + " appending the new mod-id to your items so nothing is lost...");
                            changeModidForLevelData(file);
                        }
                    }
                }
            }
            else if(!ConfigurationHandler.directoryToCheckServer.equals("disabled"))
            {
                changeModidForLevelData(new File("./" + ConfigurationHandler.directoryToCheckServer + "/level.dat"));
            }
            else
            {
                logger.error("All server side checking is disabled. CompactChests legacy support has not been implemented. This may not go well...");
            }

        }

        legacy_instance.postInitialization(event);
    }

    private void changeModidForLevelData(File file)
    {
        try
        {
            NBTTagCompound tagCompound = CompressedStreamTools.readCompressed(new FileInputStream(file));

            NBTTagCompound fml = tagCompound.getCompoundTag("FML");

            NBTTagList list = fml.getTagList("ItemData", 10);

            boolean legacy_save = false;

            for (int id = 0; id < list.tagCount(); id++)
            {
                NBTTagCompound tag = list.getCompoundTagAt(id);

                if (tag.getString("K").contains("compactchests:"))
                {
                    legacy_save = true;
                    logger.info("Replacing " + tag.getString("K") + " in save " + file.getName());
                    tag.setString("K", tag.getString("K").replace("compactchests:", "compactstorage:"));
                }

                list.func_150304_a(id, tag);
            }

            if(!legacy_save) logger.info("Save " + file.getName() + " has already been updated from CompactChests to CompactStorage. :)");

            fml.setTag("ItemData", list);
            tagCompound.setTag("FML", fml);

            CompressedStreamTools.writeCompressed(tagCompound, new FileOutputStream(file));
        }
        catch (Exception exception)
        {
            logger.error("Error when appending new data to level.dat in directory " + file.getParentFile().getName());
            exception.printStackTrace();
        }
    }
}
