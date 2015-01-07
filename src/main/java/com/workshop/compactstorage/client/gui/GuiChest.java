package com.workshop.compactstorage.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.ResourceIndex;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import cofh.api.tileentity.ISecurable;
import cofh.core.gui.GuiBaseAdv;
import cofh.core.gui.element.TabInfo;
import cofh.core.gui.element.TabSecurity;
import cofh.lib.gui.element.TabBase;

import com.workshop.compactstorage.tileentity.TileEntityChest;
import com.workshop.compactstorage.util.BlockPos;

/**
 * Created by Toby on 09/11/2014.
 */
public class GuiChest extends GuiBaseAdv
{
    public World world;
    public EntityPlayer player;
    public BlockPos pos;

    public int invX;
    public int invY;
    
    public int list;
    
    public TabInfo tabInfo;
    public TabSecurity tabSecurity;

    public static final ResourceLocation realTexture = new ResourceLocation("compactstorage", "textures/gui/chest.png");
    
    public GuiChest(Container container, World world, EntityPlayer player, BlockPos pos)
    {
        super(container);

        this.world = world;
        this.player = player;
        this.pos = pos;

        this.invX = ((TileEntityChest) world.getTileEntity(pos.getX(), pos.getY(), pos.getZ())).invX;
        this.invY = ((TileEntityChest) world.getTileEntity(pos.getX(), pos.getY(), pos.getZ())).invY;

        this.xSize = 7 + (invX * 18) + 7;
        this.ySize = 7 + (invY * 18) + 13 + 54 + 4 + 18 + 7;
    }
    
    @Override
    public void initGui()
    {
        super.initGui();
        
        this.texture = new ResourceLocation("compactstorage", "textures/empty.png");
        
        tabInfo = new TabInfo(this, 1, "Amount of Slots: "  + invX * invY + "\nMaximum Items: " + (invX * invY) * 64 + "\nMode: Player + Pipe");
        addTab(tabInfo);
        
        tabSecurity = new TabSecurity(this, (ISecurable) world.getTileEntity(pos.getX(), pos.getY(), pos.getZ()), player.getCommandSenderName());
        addTab(tabSecurity);
        
        list = GL11.glGenLists(1);
        
        GL11.glNewList(list, GL11.GL_COMPILE);
        {
        	drawTexturedModalRect(guiLeft, guiTop, 0, 0, 7, 7);

            for(int xx = 0; xx < invX * 18; xx++)
            {
                drawTexturedModalRect(guiLeft + 7 + xx, guiTop, 8, 0, 1, 7);
            }

            drawTexturedModalRect(guiLeft + 7 + invX * 18, guiTop, 10, 0, 7, 7);

            for(int yy = 0; yy < ySize - 14; yy++)
            {
                drawTexturedModalRect(guiLeft, guiTop + 7 + yy, 0, 8, 7, 1);
            }

            drawTexturedModalRect(guiLeft, guiTop + 7 + ySize - 14, 0, 10, 7, 7);

            for(int xx = 0; xx < xSize - 14; xx++)
            {
                drawTexturedModalRect(guiLeft + 7 + xx, guiTop + 7 + (ySize - 14), 8, 10, 1, 7);
            }

            drawTexturedModalRect(guiLeft + xSize - 7, guiTop + 7 + (ySize - 14), 10, 10, 7, 7);

            for(int yy = 0; yy < ySize - 14; yy++)
            {
                drawTexturedModalRect(guiLeft + xSize - 7, guiTop + 7 + yy, 10, 8, 7, 1);
            }

            for(int xx = 0; xx < xSize - 14; xx++)
            {
                for(int yy = 0; yy < ySize - 14; yy++)
                {
                    drawTexturedModalRect(guiLeft + 7 + xx, guiTop + 7 + yy, 8, 8, 1, 1);
                }
            }

            int slotX = (xSize / 2) - ((invX * 18) / 2);
            int slotY = 7; //(ySize / 2) - ((invY * 18) / 2);

            for(int x = 0; x < invX; x++)
            {
                for(int y = 0; y < invY; y++)
                {
                    drawTexturedModalRect(guiLeft + slotX + (x * 18), guiTop + slotY + (y * 18), 18, 0, 18, 18);
                }
            }

            slotX = (xSize / 2) - ((9 * 18) / 2);
            slotY = slotY + (invY * 18) + 13;

            for(int x = 0; x < 9; x++)
            {
                for(int y = 0; y < 3; y++)
                {
                    drawTexturedModalRect(guiLeft + slotX + (x * 18), guiTop + slotY + (y * 18), 18, 0, 18, 18);
                }
            }

            slotY = slotY + (3 * 18) + 4;

            for(int x = 0; x < 9; x++)
            {
                drawTexturedModalRect(guiLeft + slotX + (x * 18), guiTop + slotY, 18, 0, 18, 18);
            }
        }
        GL11.glEndList();
    }
    
    @Override
    public void drawGuiContainerForegroundLayer(int arg0, int arg1) 
    {
    	super.drawGuiContainerForegroundLayer(arg0, arg1);
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float i, int j, int k)
    {    	
        super.drawGuiContainerBackgroundLayer(i, j, k);

        Minecraft.getMinecraft().renderEngine.bindTexture(realTexture);
        GL11.glCallList(list);
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
