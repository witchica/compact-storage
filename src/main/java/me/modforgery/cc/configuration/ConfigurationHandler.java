package me.modforgery.cc.configuration;

import com.google.common.collect.Maps;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import me.modforgery.cc.CompactChests;
import me.modforgery.cc.init.ChestReferences;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
