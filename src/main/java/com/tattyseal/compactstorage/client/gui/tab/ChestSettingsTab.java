package com.tattyseal.compactstorage.client.gui.tab;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.tattyseal.compactstorage.util.RenderUtil;

public class ChestSettingsTab extends ITab 
{
	public int invX;
	public int invY;
	
	public ChestSettingsTab(GuiContainer container, ItemStack item, String name, int x, int y, boolean localizeName, int invX, int invY) 
	{
		super(container, item, name, x, y, localizeName);
		this.invX = invX;
		this.invY = invY;
	}

	@Override
	public void selected()
	{
		
	}

	@Override
	public void deselected() 
	{
		
 	}

	@Override
	public void drawBackground(int guiLeft, int guiTop) 
	{
		
	}

	@Override
	public void drawForeground(int guiLeft, int guiTop) 
	{
        
	}
}
