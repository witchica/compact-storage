package com.tattyseal.compactstorage.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

public class RenderUtil 
{
    public static final ResourceLocation slotTexture = new ResourceLocation("compactstorage", "textures/gui/chestSlots.png");
	public static final ResourceLocation colorTexture = new ResourceLocation("compactstorage", "textures/gui/colorGrid.png");
    private static double slotTextureWidth = 432d;
	private static double slotTextureHeight = 216d;
	
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
        tessellator.addVertexWithUV(x + 0, y + realHeight, 0, 0, uz);//(1 / slotTextureHeight) * (height * 18));
        tessellator.addVertexWithUV(x + realWidth, y + realHeight, 0, ux, uz);//(1 / slotTextureWidth) * (width * 18), (1 / slotTextureHeight) * (height * 18));
        tessellator.addVertexWithUV(x + realWidth, y + 0, 0, ux, 0);//1 / slotTextureWidth) * (width * 18), 0);
        tessellator.addVertexWithUV(x + 0, y + 0, 0, 0, 0);
        tessellator.draw();
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
