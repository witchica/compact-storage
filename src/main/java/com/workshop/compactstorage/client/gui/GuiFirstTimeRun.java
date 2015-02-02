package com.workshop.compactstorage.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.opengl.GL11;

public class GuiFirstTimeRun extends GuiScreen 
{
	public GuiMainMenu prev;
	
	public GuiFirstTimeRun(GuiMainMenu prev) 
	{
		super();
		this.prev = prev;
	}
	
	@Override
	public boolean doesGuiPauseGame() 
	{
		return true;
	}
	
	@Override
	public void initGui() 
	{
		super.initGui();
		
		GuiButton returnToMenu = new GuiButton(0, width / 2 - 75, height / 8 + 68, 150, 20, "Return to Main Menu.");
		buttonList.add(returnToMenu);
	}
	
	@Override
	public void drawScreen(int i, int j, float k) 
	{
		drawDefaultBackground();
		
		GL11.glPushMatrix();
		
		GL11.glScalef(2.0F, 2.0F, 0.0F);
		fontRendererObj.drawString("WARNING!", width / 4 - fontRendererObj.getStringWidth("WARNING!") / 2, height / 8 - 20, 0xFF0000);
		
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		
		String drawString = "Are you upgrading from CompactChests?";
		fontRendererObj.drawString(drawString, width / 2 - fontRendererObj.getStringWidth(drawString) / 2, height / 8 + 20, 0xFFFFFF);
	
		drawString = "If you haven't ran CompactSTORAGE 1.9 then please do!";
		fontRendererObj.drawString(drawString, width / 2 - fontRendererObj.getStringWidth(drawString) / 2, height / 8 + 32, 0xFFFFFF);
		
		drawString = "If you don't you will lost all CompactChests items and blocks!";
		fontRendererObj.drawString(drawString, width / 2 - fontRendererObj.getStringWidth(drawString) / 2, height / 8 + 44, 0xFFFFFF);
		
		drawString = "This message won't appear again.";
		fontRendererObj.drawString(drawString, width / 2 - fontRendererObj.getStringWidth(drawString) / 2, height / 8 + 56, 0xFFFFFF);
	
		GL11.glPopMatrix();
		
		super.drawScreen(i, j, k);
	}
	
	@Override
	public void actionPerformed(GuiButton button)
	{
		if(button.enabled)
		{
			switch(button.id)
			{
				case 0:
				{
					mc.displayGuiScreen(prev);
				}
			}
		}
	}
}
