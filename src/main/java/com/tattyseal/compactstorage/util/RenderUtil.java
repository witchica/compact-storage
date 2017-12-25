package com.tattyseal.compactstorage.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class RenderUtil
{
    public static final ResourceLocation slotTexture = new ResourceLocation("compactstorage", "textures/gui/chestslots.png");
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

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();

        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(x + 0, y + realHeight, 0).tex(0, uz).endVertex();
        worldRenderer.pos(x + realWidth, y + realHeight, 0).tex(ux, uz).endVertex();//(1 / slotTextureWidth) * (width), (1 / slotTextureHeight) * (height));
        worldRenderer.pos(x + realWidth, y + 0, 0).tex(ux, 0).endVertex();//1 / slotTextureWidth) * (width), 0);
        worldRenderer.pos(x + 0, y + 0, 0).tex(0, 0).endVertex();
        tessellator.draw();
    }

    public static void renderChestBackground(GuiContainer gui, int x, int y, int width, int height)
    {
        renderBackground(gui, x, y, Math.max(9, width) * 18, height * 18);
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
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);

        worldRenderer.pos((double) x, (double) y + height, 0).tex(getEnd(chestTextureWidth, startX), getEnd(chestTextureHeight, endY)).endVertex();
        worldRenderer.pos((double) x + width, (double) y + height, 0).tex(getEnd(chestTextureWidth, endX), getEnd(chestTextureHeight, endY)).endVertex();
        worldRenderer.pos((double) x + width, (double) y + 0, 0).tex(getEnd(chestTextureWidth, endX), getEnd(chestTextureHeight, startY)).endVertex();
        worldRenderer.pos((double) x, (double) y, 0).tex(getEnd(chestTextureWidth, startX), getEnd(chestTextureHeight, startY)).endVertex();

        tessellator.draw();
    }

    private static double getEnd(double width, double other)
    {
        return (1D / width) * other;
    }

    public static void drawTexturedQuadFit(double x, double y, double width, double height, double zLevel)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x + 0, y + height, zLevel).tex(0,1).endVertex();
        worldRenderer.pos(x + width, y + height, zLevel).tex(1, 1).endVertex();
        worldRenderer.pos(x + width, y + 0, zLevel).tex(1,0).endVertex();
        worldRenderer.pos(x + 0, y + 0, zLevel).tex(0, 0).endVertex();
        tessellator.draw();
    }
}
