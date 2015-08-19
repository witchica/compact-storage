package com.tattyseal.compactstorage.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

public class RenderUtil 
{
    public static final ResourceLocation slotTexture = new ResourceLocation("compactstorage", "textures/gui/chestSlots.png");
    public static final ResourceLocation backgroundTexture = new ResourceLocation("compactstorage", "textures/gui/chest.png");
	public static final ResourceLocation colorTexture = new ResourceLocation("compactstorage", "textures/gui/colorGrid.png");
    private static double slotTextureWidth = 432d;
	private static double slotTextureHeight = 216d;
	private static double chestTextureWidth = 15d;
	private static double chestTextureHeight = 15d;
	
    private static final Minecraft mc = Minecraft.getMinecraft();
    
    public static void renderSlots(int x, int y, int width, int height)
	{
		mc.renderEngine.bindTexture(slotTexture);
		
		int realWidth = (width * 18);
		int realHeight = (height * 18);
		
		double ux = (1D / slotTextureWidth) * realWidth;
		double uz = (1D / slotTextureHeight) * realHeight;
		
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + realHeight, 0, 0, uz);//(1 / slotTextureHeight) * (height));
        tessellator.addVertexWithUV(x + realWidth, y + realHeight, 0, ux, uz);//(1 / slotTextureWidth) * (width), (1 / slotTextureHeight) * (height));
        tessellator.addVertexWithUV(x + realWidth, y + 0, 0, ux, 0);//1 / slotTextureWidth) * (width), 0);
        tessellator.addVertexWithUV(x + 0, y + 0, 0, 0, 0);
        tessellator.draw();
	}
    
    public static void renderChestBackground(GuiContainer gui, int x, int y, int width, int height)
    {
    	renderBackground(gui, x, y, width * 18, height * 18);
    }
    
    public static void renderBackground(GuiContainer gui, int x, int y, int width, int height)
    {
    	mc.renderEngine.bindTexture(backgroundTexture);
		
		int realWidth = 7 + (width) + 7;
		int realHeight = 15 + (height) + 13 + 54 + 4 + 18 + 7;
		
		int by = y + (realHeight - 7);
		
		renderPartBackground(x, y, 0, 0, 7, 7, 7, 7);
		renderPartBackground(x + 7, y, 8, 0, 8, 7, (width), 7);
		renderPartBackground(x + 7 + (width), y, 9, 0, 15, 7, 7, 7);
		
		renderPartBackground(x, by, 0, 8, 7, 15, 7, 7);
		renderPartBackground(x + 7, by, 8, 8, 7, 15, (width), 7);
		renderPartBackground(x + 7 + (width), by, 9, 8, 15, 15, 7, 7);
		
		renderPartBackground(x, y + 7, 0, 7, 7, 7, 7, (realHeight - 14));
		renderPartBackground(x + realWidth - 8, y + 7, 8, 7, 15, 7, 8, (realHeight - 14));

		renderPartBackground(x + 7, y + 7, 8, 8, 8, 8, (width), realHeight - 14);
    }
    
    private static void renderPartBackground(int x, int y, int startX, int startY, int endX, int endY, int width, int height)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        
        tessellator.addVertexWithUV(x, y + height, 0, getEnd(chestTextureWidth, startX), getEnd(chestTextureHeight, endY));
        tessellator.addVertexWithUV(x + width, y + height, 0, getEnd(chestTextureWidth, endX), getEnd(chestTextureHeight, endY));
        tessellator.addVertexWithUV(x + width, y + 0, 0, getEnd(chestTextureWidth, endX), getEnd(chestTextureHeight, startY));
        tessellator.addVertexWithUV(x, y, 0, getEnd(chestTextureWidth, startX), getEnd(chestTextureHeight, startY));
        
        tessellator.draw();
    }
    
    private static double getEnd(double width, double other)
    {
    	return (1D / width) * other;
    }

    public static void drawTexturedQuadFit(double x, double y, double width, double height, double zLevel)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + height, zLevel, 0,1);
        tessellator.addVertexWithUV(x + width, y + height, zLevel, 1, 1);
        tessellator.addVertexWithUV(x + width, y + 0, zLevel, 1,0);
        tessellator.addVertexWithUV(x + 0, y + 0, zLevel, 0, 0);
        tessellator.draw();
    }
}
