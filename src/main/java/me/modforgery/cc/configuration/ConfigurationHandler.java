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
        String recipe = configuration.getString("doubleChestRecipe", "Crafting", "1=WWW;2=WCW;3=WWW;W=tile.planks.name;C=tile.chest.name", "Format");

        if(configuration.hasChanged())
        {
            configuration.save();
        }
    }

    public static HashMap<String, Item> getItemList()
    {
        HashMap map = Maps.newHashMap();

        for(Object o : Item.itemRegistry)
        {
            Item i = (Item) o;
            map.put(i.getUnlocalizedName(), i);
        }

        for(Object o : Block.blockRegistry)
        {
            Block block = (Block) o;
            map.put(block.getUnlocalizedName(), (Item.getItemFromBlock(block)));
        }

        return map;
    }

    public static boolean parseRecipeString(String string, ItemStack output)
    {
        HashMap itemList = getItemList();

        String[] elements = string.split(";");

        String line1, line2, line3;
        Map<String, String> map = Maps.newHashMap();

        for(String sub : elements)
        {
            String key = sub.split("=")[0];
            String value = sub.split("=")[1];

            if(key.equals("1"))
            {
                line1 = value;
            }
            else if(key.equals("2"))
            {
                line2 = value;
            }
            else if(key.equals("3"))
            {
                line3 = value;
            }
            else
            {
                map.put(key, value);
            }
        }

        //GameRegistry.addRecipe(output,);

        return true;
    }
}
