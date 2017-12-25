package com.tattyseal.compactstorage.client.gui;

import com.tattyseal.compactstorage.api.IChest;
import com.tattyseal.compactstorage.inventory.InventoryBackpack;
import com.tattyseal.compactstorage.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

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
    
    public KeyBinding[] HOTBAR;
    public int backpackSlot;

    
    public GuiChest(Container container, IChest chest, World world, EntityPlayer player, BlockPos pos)
    {
        super(container);

        this.world = world;
        this.player = player;
        this.pos = pos;

        this.chest = chest;
        
        this.HOTBAR = Minecraft.getMinecraft().gameSettings.keyBindsHotbar;
        
        backpackSlot = -1;
        if(chest instanceof InventoryBackpack)
	    {
        	backpackSlot = player.inventory.currentItem;
	    }

        this.invX = chest.getInvX();
        this.invY = chest.getInvY();

        this.xSize = 7 + (Math.max(9, invX) * 18) + 7;
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
    	
        this.fontRenderer.drawString("Chest (" + invX + "x" + invY + ")", 8, 6, 4210752);
        this.fontRenderer.drawString("Inventory", 8, 15 + (invY * 18) + 5, 4210752);
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float i, int j, int k)
    {    	
    	GL11.glPushMatrix();
    	
    	GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor3f(1, 1, 1);
        
        RenderUtil.renderChestBackground(this, guiLeft, guiTop, invX, invY);

        RenderUtil.renderSlots(guiLeft + 7 + ((Math.max(9, invX) * 18) / 2) - (invX * 18) / 2, guiTop + 17, invX, invY);
        RenderUtil.renderSlots(guiLeft + 7 + ((((Math.max(9, invX)) * 18) / 2) - ((9 * 18) / 2)), guiTop + 17 + (invY * 18) + 13, 9, 3);
        RenderUtil.renderSlots(guiLeft + 7 + ((((Math.max(9, invX)) * 18) / 2) - ((9 * 18) / 2)), guiTop + 17 + (invY * 18) + 13 + 54 + 4, 9, 1);

        GL11.glPopMatrix();
    }
    
    @Override
    protected void keyTyped(char c, int id)  throws IOException
    {
    	if (backpackSlot != -1 && HOTBAR[backpackSlot].getKeyCode() == id) 
    	{
    		return;
    	}
    	
    	super.keyTyped(c, id);
    }

    @Override
    public void onGuiClosed()
    {
        //chest.closeInventory();
        super.onGuiClosed();
    }
}
