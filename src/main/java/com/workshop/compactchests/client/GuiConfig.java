package com.workshop.compactchests.client;

import com.workshop.compactchests.configuration.ConfigurationHandler;
import com.workshop.compactchests.init.ChestReferences;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

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
