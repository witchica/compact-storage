package com.workshop.compactstorage.essential.handler;

import com.google.common.collect.Maps;
import com.workshop.compactstorage.essential.CompactStorage;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

/**
 * Created by Toby on 07/11/2014.
 */
public class ConfigurationHandler
{
    public static Configuration configuration;
    private static boolean initialized = false;

    public static boolean checkAllDirectoriesServer;
    public static boolean changeNBTForWorldsClient;

    public static String directoryToCheckServer;

    public static void init(File configFile)
    {
        if(initialized) return;

        try
        {
            configuration = new Configuration(configFile);


            checkAllDirectoriesServer = configuration.getBoolean("checkAllDirectories", "NBT", true, "Recurse through all server directories checking for a level.dat and changing the mod-id so blocks are not lost.");
            directoryToCheckServer = configuration.getString("directoryToCheckServer", "NBT", "disabled", "The directory to check for level.dat on server side. Disabled if 'checkAllDirectories' is set to true.");
            changeNBTForWorldsClient = configuration.getBoolean("changeNBTForSavesClient", "NBT", true, "Should we recurse through all of your saves and change the mod-id so CompactChests blocks are not lost?");
        }
        catch(Exception e)
        {
            CompactStorage.logger.error("Error when loading configuration file " + e.getClass().getSimpleName());
        }
        finally
        {
            configuration.save();
        }

        initialized = true;
    }
}
