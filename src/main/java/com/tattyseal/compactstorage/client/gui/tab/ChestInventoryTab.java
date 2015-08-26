package com.tattyseal.compactstorage.client.gui.tab;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.tattyseal.compactstorage.util.RenderUtil;

public class ChestInventoryTab extends ITab 
{
	public int invX;
	public int invY;
	
	public int[] slotX;
	
	public ChestInventoryTab(GuiContainer container, ItemStack item, String name, int x, int y, boolean localizeName, int invX, int invY) 
	{
		super(container, item, name, x, y, localizeName);
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
        RenderUtil.renderSlots(guiLeft + 7, guiTop + 17, invX, invY);
        RenderUtil.renderSlots(guiLeft + 7 + (((invX * 18) / 2) - ((9 * 18) / 2)), guiTop + 17 + (invY * 18) + 13, 9, 3);
        RenderUtil.renderSlots(guiLeft + 7 + (((invX * 18) / 2) - ((9 * 18) / 2)), guiTop + 17 + (invY * 18) + 13 + 54 + 4, 9, 1);

	}

	@Override
	public void drawForeground(int guiLeft, int guiTop) 
	{
        mc.fontRenderer.drawString("Chest (" + invX + "x" + invY + ")", 8, 6, 4210752);
        mc.fontRenderer.drawString("Inventory", 8, 15 + (invY * 18) + 5, 4210752);
	}
}
