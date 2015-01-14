package com.workshop.compactstorage.essential.handler;

import com.workshop.compactstorage.client.gui.GuiFirstTimeRun;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class FirstTimeRunHandler 
{
	public boolean ran = false;
	
	@SubscribeEvent
	public void onMenuOpen(TickEvent.ClientTickEvent event)
	{
		if(!ran && Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu && ConfigurationHandler.firstTimeRun)
		{
			Minecraft.getMinecraft().displayGuiScreen(new GuiFirstTimeRun((GuiMainMenu) Minecraft.getMinecraft().currentScreen));
			ran = true;
		}
	}
}
