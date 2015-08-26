package com.tattyseal.compactstorage.client.gui.tab;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class ITab 
{
	private static final ResourceLocation tabTexture = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
	public static final Minecraft mc = Minecraft.getMinecraft();
	
	public ItemStack item;
	public String name;
	public boolean localizeName;
	public boolean selected;
	public GuiContainer container;
	
	public int x;
	public int y;
	
	public ITab(GuiContainer container, ItemStack item, String name, int x, int y)
	{
		this(container, item, name, x, y, true);
	}
	
	public ITab(GuiContainer container, ItemStack item, String name, int x, int y, boolean localizeName)
	{
		this.container = container;
		this.item = item;
		this.name = name;
		this.localizeName = localizeName;
		this.x = x;
		this.y = y;
		this.selected = false;
	}
	
	public void draw()
	{
		mc.renderEngine.bindTexture(tabTexture);
		container.drawTexturedModalRect(x, y, 0, (selected ? 30 : 0) + 2, 28, 28);
		RenderItem.getInstance().renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, item, x + 6, y + 7);
	}
	
	public boolean clickIntersects(int mx, int my)
	{
		return mx > x && mx < (x + 28) && my > y && my < (y + 28);
	}
	
	public abstract void selected();
	public abstract void deselected();
	
	public abstract void drawBackground(int guiLeft, int guiTop);
	public abstract void drawForeground(int guiLeft, int guiTop);
}
