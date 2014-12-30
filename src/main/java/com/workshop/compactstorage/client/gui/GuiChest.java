package com.workshop.compactstorage.client.gui;

import com.workshop.compactstorage.util.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Created by Toby on 09/11/2014.
 */
public class GuiChest extends GuiContainer
{
    public World world;
    public EntityPlayer player;
    public BlockPos pos;

    public GuiChest(Container container, World world, EntityPlayer player, BlockPos pos)
    {
        super(container);

        this.world = world;
        this.player = player;
        this.pos = pos;

        this.xSize = 7 + (9 * 18) + 7;
        this.ySize = 6 + (6 * 18) + 5 + (3 * 18) + 4 + 18 + 7;
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float i, int j, int k)
    {
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("compactstorage", "textures/gui/chest.png"));

        drawTexturedModalRect(guiLeft, guiTop, 0, 0, 7, 7);

        for(int xx = 0; xx < 9 * 18; xx++)
        {
            drawTexturedModalRect(guiLeft + 7 + xx, guiTop, 8, 0, 1, 7);
        }

        drawTexturedModalRect(guiLeft + 9 * 18, guiTop, 10, 0, 7, 7);

        for(int yy = 0; yy < 189 /* TODO CHANGE THIS SO IT INCLUDES INVENTORY ETC */; yy++)
        {
            drawTexturedModalRect(guiLeft, guiTop + 7 + yy, 0, 8, 7, 1);
        }

        drawTexturedModalRect(guiLeft, guiTop + 7 + 189, 0, 10, 7, 7);

        for(int xx = 0; xx < 9 * 18; xx++)
        {
            drawTexturedModalRect(guiLeft + 7 + xx, guiTop + 7 + 189, 8, 10, 1, 7);
        }

        drawTexturedModalRect(guiLeft + 9 * 18, guiTop + 7 + 189, 10, 10, 7, 7);

        for(int yy = 0; yy < 189 /* TODO CHANGE THIS SO IT INCLUDES INVENTORY ETC */; yy++)
        {
            drawTexturedModalRect(guiLeft + 18 * 9, guiTop + 7 + yy, 10, 8, 7, 1);
        }

        for(int xx = 0; xx < 9 * 18 - 7; xx++)
        {
            for(int yy = 0; yy < 189; yy++)
            {
                drawTexturedModalRect(guiLeft + 7 + xx, guiTop + 7 + yy, 8, 8, 1, 1);
            }
        }
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
