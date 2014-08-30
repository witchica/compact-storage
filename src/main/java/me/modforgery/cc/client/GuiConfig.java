package me.modforgery.cc.client;

import cpw.mods.fml.client.config.IConfigElement;
import me.modforgery.cc.configuration.ConfigurationHandler;
import me.modforgery.cc.init.ChestReferences;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

import java.util.List;

/**
 * Created by Toby on 29/08/2014.
 */
public class GuiConfig extends cpw.mods.fml.client.config.GuiConfig
{

    public GuiConfig(GuiScreen parentScreen)
    {
        super(parentScreen, new ConfigElement(ConfigurationHandler.configuration.getCategory("Crafting")).getChildElements(), ChestReferences.ID, true, true, "CompactChests Crafting");
    }
}
