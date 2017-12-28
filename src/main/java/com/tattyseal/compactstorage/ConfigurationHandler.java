package com.tattyseal.compactstorage;

import com.google.common.collect.Lists;
import com.tattyseal.compactstorage.exception.InvalidConfigurationException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.io.File;
import java.util.List;

/**
 * Created by Toby on 07/11/2014.
 */
public class ConfigurationHandler
{
    public static Configuration configuration;
    public static File configFile;

    public static boolean firstTimeRun;

    public static ItemStack storage;
    public static ItemStack storageBackpack;

    public static ItemStack[] primary;
    public static ItemStack[] secondary;

    public static ItemStack binder;
    public static ItemStack binderBackpack;

    public static float storageModifier;
    public static float primaryModifier;
    public static float secondaryModifier;
    public static float binderModifier;

    public static boolean shouldConnect;

    public static boolean newFeatures;
    public static Property newFeaturesField;

    public class EventHandler
    {
        @SubscribeEvent
        public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
            if(eventArgs.getModID().equals("compactstorage"))
                ConfigurationHandler.init();
        }
    }

    public static void disableMessage()
    {
        newFeatures = false;
        newFeaturesField.set(false);
        configuration.save();
    }

    public static void init()
    {
        configuration = new Configuration(configFile);

        firstTimeRun = configuration.getBoolean("firstTimeRun", "internal", false, "This is used internally for the GUI shown when you first start the game.");

        newFeaturesField = configuration.get("internal", "newFeatures", true, "Should the text be shown on startup informing you about new features?");
        newFeatures = newFeaturesField.getBoolean();

        storage = getItemFromConfig(configuration, "chestStorage", "builder", "minecraft:chest", "This is used as the first component in the Builder when building a CHEST.");
        storageBackpack = getItemFromConfig(configuration, "backpackStorage", "builder", "minecraft:wool", "This is used as the first component in the Builder when building a BACKPACK.");

        primary = getItemsFromConfig(configuration, "primaryItem", "builder", new String[]{"minecraft:iron_ingot"}, "These values are used for the first material cost in the chest builder, you can add as many values as you like, it will configure itself to use all of them.");
        secondary = getItemsFromConfig(configuration, "secondaryItem", "builder", new String[]{"minecraft:iron_bars"}, "These values are used for the second material cost in the chest builder, you can add as many values as you like, it will configure itself to use all of them.");

        binder = getItemFromConfig(configuration, "chestBinder", "builder", "minecraft:clay_ball", "This is used as the binder material when making a CHEST.");
        binderBackpack = getItemFromConfig(configuration, "backpackBinder", "builder", "minecraft:string", "This is used as the binder material when making a BACKPACK.");

        storageModifier = configuration.getFloat("storageModifier", "builder", 1F, 0F, 1F, "This determines how much of the item is required.");
        primaryModifier = configuration.getFloat("primaryModifier", "builder", 1F, 0F, 1F, "This determines how much of the item is required.");
        secondaryModifier = configuration.getFloat("secondaryModifier", "builder", 1F, 0F, 1F, "This determines how much of the item is required.");
        binderModifier = configuration.getFloat("binderModifier", "builder", 1F, 0F, 1F, "This determines how much of the item is required.");

        configuration.setCategoryComment("builder", "Format for item names is modid:name@meta or leave @meta for all possible metadata of that item. These are not unlocalized names. If you do something wrong or it uses the defaut values check your log!!! Look for an InvalidConfigurationException and it will tell you why!");

        if(firstTimeRun) configuration.get("internal", "firstTimeRun", false).set(false);

        if(configuration.hasChanged())
        {
            configuration.save();
        }

        shouldConnect = configuration.getBoolean("shouldConnectToNetworks", "chest", true, "This determines whether chests will connect to ES networks.");
    }

    public static ItemStack getItemFromConfig(Configuration config, String name, String category, String defaultString, String comment)
    {
        String itemName = config.getString(name, category, defaultString, comment);

        String modId = itemName.contains(":") ? itemName.split(":", 2)[0] : "minecraft";
        String itemId = itemName.contains(":") ? itemName.split(":", 2)[1] : itemName;
        int meta;

        if(itemName.contains("@"))
        {
            meta = Integer.parseInt(itemId.split("@")[1]);
            itemId = itemId.split("@")[0];
        }
        else
        {
            meta = OreDictionary.WILDCARD_VALUE;
        }

        Item item = Item.REGISTRY.getObject(new ResourceLocation(modId, itemId));

        if(item == null)
        {
            new InvalidConfigurationException("Could not find item " + itemName + " for value " + name + " in the CompactStorage config! Reverting to default.").printStackTrace();


            modId = defaultString.contains(":") ? defaultString.split(":", 2)[0] : "minecraft";
            itemId = defaultString.contains(":") ? defaultString.split(":", 2)[1] : defaultString;

            if(itemName.contains("@"))
            {
                meta = Integer.parseInt(itemId.split("@")[1]);
                itemId = itemId.split("@")[0];
            }
            else
            {
                meta = OreDictionary.WILDCARD_VALUE;
            }
            item = Item.REGISTRY.getObject(new ResourceLocation(modId, itemId));

            return new ItemStack(item, 1, meta);
        }

        return new ItemStack(item, 1, meta);
    }

    public static ItemStack[] getItemsFromConfig(Configuration config, String key, String category, String[] defaultItems, String comment)
    {
        List<ItemStack> items = Lists.newArrayList();
        String[] itemNames = config.getStringList(key, category, defaultItems, comment);

        boolean breakOff = false;

        for(String itemName : itemNames)
        {
            String modId = itemName.contains(":") ? itemName.split(":", 2)[0] : "minecraft";
            String itemId = itemName.contains(":") ? itemName.split(":", 2)[1] : itemName;
            int meta;

            if(itemName.contains("@"))
            {
                meta = Integer.parseInt(itemId.split("@")[1]);
                itemId = itemId.split("@")[0];
            }
            else
            {
                meta = OreDictionary.WILDCARD_VALUE;
            }

            Item item = Item.REGISTRY.getObject(new ResourceLocation(modId, itemId));

            if(item == null)
            {
                new InvalidConfigurationException("Could not find item " + itemName + " for value " + key + " in the CompactStorage config! Reverting to default.").printStackTrace();
                breakOff = true;
                break;
            }

            items.add(new ItemStack(item, 1, meta));
        }

        if(breakOff)
        {
            items.clear();

            for(String itemName : defaultItems)
            {
                String modId = itemName.contains(":") ? itemName.split(":", 2)[0] : "minecraft";
                String itemId = itemName.contains(":") ? itemName.split(":", 2)[1] : itemName;
                int meta;

                if(itemName.contains("@"))
                {
                    meta = Integer.parseInt(itemId.split("@")[1]);
                    itemId = itemId.split("@")[0];
                }
                else
                {
                    meta = OreDictionary.WILDCARD_VALUE;
                }
                Item item = Item.REGISTRY.getObject(new ResourceLocation(modId, itemId));
                items.add(new ItemStack(item, 1, meta));
            }
        }

        return items.toArray(new ItemStack[items.size()]);
    }
}
