package com.tattyseal.compactstorage.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.tattyseal.compactstorage.api.IChest;
import com.tattyseal.compactstorage.util.BlockPos;
import com.tattyseal.compactstorage.util.RenderUtil;

/**
 * Created by Toby on 09/11/2014.
 */
public class GuiChest extends GuiContainer
{
    public World world;
    public EntityPlayer player;
    public BlockPos pos;

    public int invX;
    public int invY;

    public IChest chest;

    
    public GuiChest(Container container, IChest chest, World world, EntityPlayer player, BlockPos pos)
    {
        super(container);

        this.world = world;
        this.player = player;
        this.pos = pos;

        this.chest = chest;

        this.invX = chest.getInvX();
        this.invY = chest.getInvY();

        this.xSize = 7 + ((invX) < 9 ? (9 * 18) : (invX * 18)) + 7;
        this.ySize = 15 + (invY * 18) + 13 + 54 + 4 + 18 + 7;
    }
    
    @Override
    public void initGui()
    {
        super.initGui();
    }
    
    @Override
    public void drawGuiContainerForegroundLayer(int arg0, int arg1) 
    {
    	super.drawGuiContainerForegroundLayer(arg0, arg1);
    	
        this.fontRendererObj.drawString("Chest (" + invX + "x" + invY + ")", 8, 6, 4210752);
        this.fontRendererObj.drawString("Inventory", 8, 15 + (invY * 18) + 5, 4210752);
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float i, int j, int k)
    {    	
    	GL11.glPushMatrix();
    	
    	GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor3f(1, 1, 1);
        RenderUtil.renderSlots(guiLeft + 7, guiTop + 17, invX, invY);
        RenderUtil.renderSlots(guiLeft + 7 + (((invX * 18) / 2) - ((9 * 18) / 2)), guiTop + 17 + (invY * 18) + 13, 9, 3);
        RenderUtil.renderSlots(guiLeft + 7 + (((invX * 18) / 2) - ((9 * 18) / 2)), guiTop + 17 + (invY * 18) + 13 + 54 + 4, 9, 1);
        RenderUtil.renderSlots(guiLeft + 7 + (invX * 18) + 9, guiTop + 17, 1, 3);
        GL11.glPopMatrix();
    }

    @Override
    public void onGuiClosed()
    {
        //chest.closeInventory();
        super.onGuiClosed();
    }
}
