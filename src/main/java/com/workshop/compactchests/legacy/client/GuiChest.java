package com.workshop.compactchests.legacy.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Created by Toby on 19/08/2014.
 */
public class GuiChest extends GuiContainer
{
    public Container container;
    public EntityPlayer player;
    public World world;

    public int x;
    public int y;
    public int z;

    public boolean big;

    public ResourceLocation bg;

    public GuiChest(String name, int w, int h, Container serverGuiElement, EntityPlayer player, World world, int x, int y, int z, boolean big)
    {
        super(serverGuiElement);

        this.container = serverGuiElement;
        this.player = player;
        this.world = world;

        this.x = x;
        this.y = y;
        this.z = z;

        this.big = big;

        this.xSize = w;
        this.ySize = h;

        this.bg = new ResourceLocation("compactchests", "textures/gui/container/" + name + "_chest.png");
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float i, int j, int k)
    {
        this.mc.renderEngine.bindTexture(bg);

        if(big) this.drawTexturedQuadFit(guiLeft, guiTop, xSize, ySize, 0);
        else this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
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
