package com.tattyseal.compactstorage.client.gui.tab;

import com.tattyseal.compactstorage.client.gui.GuiChest;
import com.tattyseal.compactstorage.util.RenderUtil;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ChestInventoryTab extends ITab 
{
	public int invX;
	public int invY;
	
	public GuiChest chest;
	
	public int[] slotX;
	
	public ChestInventoryTab(GuiChest container, ItemStack item, String name, int x, int y, boolean localizeName, int invX, int invY) 
	{
		super(container, item, name, x, y, localizeName);
		this.chest = container;
		this.invX = invX;
		this.invY = invY;
		
		slotX = new int[container.inventorySlots.inventorySlots.size()];
		
		for(Object obj : container.inventorySlots.inventorySlots)
		{
			Slot slot = (Slot) obj;
			
			slotX[slot.slotNumber] = slot.xDisplayPosition;
		}
	}

	@Override
	public void selected()
	{
		for(Object obj : container.inventorySlots.inventorySlots)
		{
			Slot slot = (Slot) obj;
			if(slot.xDisplayPosition == Integer.MIN_VALUE) slot.xDisplayPosition = slotX[slot.slotNumber];
		}
	}

	@Override
	public void deselected() 
	{
		for(Object obj : container.inventorySlots.inventorySlots)
		{
			Slot slot = (Slot) obj;
			slot.xDisplayPosition = Integer.MIN_VALUE;
		}
 	}

	@Override
	public void drawBackground(int guiLeft, int guiTop) 
	{
        RenderUtil.renderSlots(guiLeft + 7 + ((Math.max(9, invX) * 18) / 2) - (invX * 18) / 2, guiTop + 17, invX, invY);
        RenderUtil.renderSlots(guiLeft + 7 + (((Math.max(9, invX) * 18) / 2) - ((9 * 18) / 2)), guiTop + 17 + (invY * 18) + 13, 9, 3);
        RenderUtil.renderSlots(guiLeft + 7 + (((Math.max(9, invX) * 18) / 2) - ((9 * 18) / 2)), guiTop + 17 + (invY * 18) + 13 + 54 + 4, 9, 1);

	}

	@Override
	public void drawForeground(int guiLeft, int guiTop) 
	{
        mc.fontRenderer.drawString("Chest (" + invX + "x" + invY + ")", 8, 6, 4210752);
        mc.fontRenderer.drawString("Inventory", 8, 15 + (invY * 18) + 5, 4210752);
	}

	@Override
	public void buttonClicked(GuiButton button) 
	{
		
	}

	@Override
	public void mouseClicked(int x, int z, int b) 
	{
		
	}

	@Override
	public void keyTyped(char c, int id) 
	{
		
	}

	@Override
	public boolean areTextboxesInFocus() 
	{
		return false;
	}
}
