package com.workshop.compactstorage.client.gui;

import com.workshop.compactstorage.block.ChestType;
import com.workshop.compactstorage.util.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

/**
 * Created by Toby on 09/11/2014.
 */
public class GuiChest extends GuiContainer
{
    public ChestType type;
    public World world;
    public EntityPlayer player;
    public BlockPos pos;

    public GuiChest(Container container, ChestType type, World world, EntityPlayer player, BlockPos pos)
    {
        super(container);

        this.type = type;
        this.world = world;
        this.player = player;
        this.pos = pos;

        this.xSize = 7 + (type.getWidth() * 18) + 7;
        this.ySize = 6 + (type.getHeight() * 18) + 5 + (3 * 18) + 4 + 18 + 7;
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float i, int j, int k)
    {
        Minecraft.getMinecraft().renderEngine.bindTexture(type.gui);
        drawTexturedQuadFit(guiLeft, guiTop, xSize, ySize, 0);
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
