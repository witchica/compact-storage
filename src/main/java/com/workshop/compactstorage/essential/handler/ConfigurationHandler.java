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

    public static boolean firstTimeRun;
    public static String directoryToCheckServer;

    public static void init(File configFile)
    {
        if(initialized) return;

        try
        {
            configuration = new Configuration(configFile);

            firstTimeRun = configuration.get("internal", "firstTime", true).getBoolean();
            configuration.get("internal", "firstTime", true).set(false);
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
