package com.tattyseal.compactstorage.client.gui;

import java.util.Arrays;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.tattyseal.compactstorage.api.IChest;
import com.tattyseal.compactstorage.client.gui.tab.ChestInventoryTab;
import com.tattyseal.compactstorage.client.gui.tab.ChestSettingsTab;
import com.tattyseal.compactstorage.client.gui.tab.ITab;
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
    
    public ITab[] tabs;
    public ITab activeTab;
    
    public GuiChest(Container container, IChest chest, World world, EntityPlayer player, BlockPos pos)
    {
        super(container);

        this.world = world;
        this.player = player;
        this.pos = pos;

        this.chest = chest;

        this.invX = chest.getInvX();
        this.invY = chest.getInvY();

        this.xSize = 7 + (invX * 18) + 7;
        this.ySize = 15 + (invY * 18) + 13 + 54 + 4 + 18 + 7;
    }
    
    @Override
    public void initGui()
    {
        super.initGui();
        
        tabs = new ITab[2];
        tabs[0] = new ChestInventoryTab(this, new ItemStack(Blocks.chest), "tab.chest", guiLeft + 1, guiTop - 28, true, invX, invY);
        tabs[1] = new ChestSettingsTab(this, new ItemStack(Items.redstone), "tab.settings", guiLeft + 28, guiTop - 28, true, invX, invY);
   
        activeTab = tabs[0];
        tabs[0].selected = true;
    }
    
    @Override
    public void drawGuiContainerForegroundLayer(int arg0, int arg1) 
    {
    	super.drawGuiContainerForegroundLayer(arg0, arg1);
    	activeTab.drawForeground(guiLeft, guiTop);
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float i, int j, int k)
    {    	
    	GL11.glPushMatrix();
    	
    	GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor3f(1, 1, 1);

		RenderUtil.renderChestBackground(this, guiLeft, guiTop, invX, invY);
        activeTab.drawBackground(guiLeft, guiTop);
        
        for(ITab tab : tabs)
        {
        	tab.draw();
        }
        
        for(ITab tab : tabs)
        {
        	if(tab.clickIntersects(j, k))
        	{
        		drawHoveringText(Arrays.asList(I18n.format(tab.name)), j, k, fontRendererObj);
        	}
        }
        
        GL11.glPopMatrix();
    }
    
    @Override
    protected void mouseClicked(int x, int y, int b) 
    {
    	ITab iTab = null;
    	System.out.println(x + ":" + y);
    	
    	for(ITab tab : tabs)
    	{
    		if(tab.clickIntersects(x, y))
    		{
    			System.out.println("intersects");
    			iTab = tab;
    			break;
    		}
    	}
    	
    	if(iTab != null)
    	{
        	for(ITab tab : tabs)
        	{
        		tab.selected = false;
        		tab.deselected();
        	}
        	
    		iTab.selected = true;
        	iTab.selected();
        	this.activeTab = iTab;
    	}
    	
    	super.mouseClicked(x, y, b);
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
    }
}
