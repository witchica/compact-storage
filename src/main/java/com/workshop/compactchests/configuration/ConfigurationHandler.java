package com.workshop.compactchests.configuration;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import com.workshop.compactchests.init.ChestReferences;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Toby on 29/08/2014.
 */
public class ConfigurationHandler
{
    public static Configuration configuration;

    public static void init(File configFile)
    {
        if(configuration == null)
        {
            configuration = new Configuration(configFile);
            loadConfiguration();
        }
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent configChangedEvent)
    {
        if(configChangedEvent.modID.equalsIgnoreCase(ChestReferences.ID))
        {
            loadConfiguration();
        }
    }

    private static void loadConfiguration()
    {

    }
}
