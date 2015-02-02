package com.workshop.compactstorage.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.workshop.compactstorage.essential.CompactStorage;
import com.workshop.compactstorage.network.packet.C01PacketUpdateBuilder;
import com.workshop.compactstorage.network.packet.C02PacketCraftChest;
import com.workshop.compactstorage.tileentity.TileEntityChestBuilder;
import com.workshop.compactstorage.util.BlockPos;
import com.workshop.compactstorage.util.StorageInfo;

/**
 * Created by Toby on 09/11/2014.
 */
public class GuiChestBuilder extends GuiContainer
{
    public World world;
    public EntityPlayer player;
    public BlockPos pos;
    
    public int list;
    
    public GuiButton buttonAddX;
    public GuiButton buttonMinusX;

    public GuiButton buttonAddY;
    public GuiButton buttonMinusY;
    
    public GuiButton buttonSubmit;
    
    public TileEntityChestBuilder builder;

    public static final ResourceLocation realTexture = new ResourceLocation("compactstorage", "textures/gui/chest.png");
    
    public GuiChestBuilder(Container container, World world, EntityPlayer player, BlockPos pos)
    {
        super(container);

        this.world = world;
        this.player = player;
        this.pos = pos;
        
        this.builder = ((TileEntityChestBuilder) world.getTileEntity(pos.getX(), pos.getY(), pos.getZ()));

        this.xSize = 7 + 162 + 7;
        this.ySize = 7 + 54 + 13 + 54 + 4 + 18 + 7;
    }
    
    @Override
    public void initGui()
    {
        super.initGui();
        
        list = GL11.glGenLists(1);
        
        GL11.glNewList(list, GL11.GL_COMPILE);
        {
        	drawTexturedModalRect(guiLeft, guiTop, 0, 0, 7, 7);

            for(int xx = 0; xx < 162; xx++)
            {
                drawTexturedModalRect(guiLeft + 7 + xx, guiTop, 8, 0, 1, 7);
            }

            drawTexturedModalRect(guiLeft + 7 + 162, guiTop, 10, 0, 7, 7);

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

            int slotX = (xSize / 2) - (162 / 2);
            int slotY = 7; //(ySize / 2) - ((invY * 18) / 2);

            /*for(int x = 0; x < 162; x++)
            {
                for(int y = 0; y < 54; y++)
                {
                    drawTexturedModalRect(guiLeft + slotX + (x * 18), guiTop + slotY + (y * 18), 18, 0, 18, 18);
                }
            }*/

            slotX = (xSize / 2) - ((9 * 18) / 2);
            slotY = slotY + 54 + 13;

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
        
        buttonAddX = new GuiButton(0, guiLeft + 162 / 2 + getFontRenderer().getStringWidth("Size X: 12 "), guiTop + 10, 20, 20, "+");
        buttonList.add(buttonAddX);
        
        buttonMinusX = new GuiButton(1, guiLeft + 162 / 2 + getFontRenderer().getStringWidth("Size X: 12 ") + 20, guiTop + 10, 20, 20, "-");
        buttonList.add(buttonMinusX);
        
        buttonAddY = new GuiButton(2, guiLeft + 162 / 2 + getFontRenderer().getStringWidth("Size Y: 12 "), guiTop + 30, 20, 20, "+");
        buttonList.add(buttonAddY);
        
        buttonMinusY = new GuiButton(3, guiLeft + 162 / 2 + getFontRenderer().getStringWidth("Size Y: 12 ") + 20, guiTop + 30, 20, 20, "-");
        buttonList.add(buttonMinusY);
        
        buttonSubmit = new GuiButton(4, guiLeft + 162 / 2 + getFontRenderer().getStringWidth("Size Y: 12 ") + 20, guiTop + 50, 20, 20, "-");
        buttonList.add(buttonSubmit);
    }
    
    @Override
    public void drawGuiContainerForegroundLayer(int arg0, int arg1) 
    {
    	
    }
    
    @Override
    public void drawScreen(int i, int j, float k)
    {
    	super.drawScreen(i, j, k);
    	
    	int mouseX = i;
    	int mouseY = j;
    	
    	if(builder != null && builder.info != null)
    	{
    		for(int x = 0; x < 4; x++)
            {
    			List<ItemStack> stackList = builder.info.getMaterialCost().get(x);
    			
                ItemStack stack = stackList.get(0);
            	int startX = guiLeft + 7 + x * 18 + 1;
                int startY = guiTop + 18;
                
                int endX = startX + 18;
                int endY = startY + 18;
                
                if(mouseX >= startX && mouseX <= endX)
                {
                	if(mouseY >= startY && mouseY <= endY)
                	{
                		ArrayList<String> toolList = new ArrayList<String>();
                		toolList.add(stack.getDisplayName());
                		toolList.add(EnumChatFormatting.AQUA + "Amount Required: " + stack.stackSize);
                		
                		drawHoveringText(toolList, mouseX, mouseY, getFontRenderer());
                	}
                }
                
                RenderHelper.disableStandardItemLighting();
            }
    	}
    }
    
    @Override
    public void updateScreen() 
    {
    	super.updateScreen();
    	
    	if(builder != null && builder.info != null)
    	{
    		if(builder.info.getSizeX() >= 24)
        	{
        		buttonAddX.enabled = false;
        		if(builder.info.getSizeX() > 24) builder.info.setSizeX(12);
        	}
        	else
        	{
        		buttonAddX.enabled = true;
        	}
        	
        	if(builder.info.getSizeX() <= 1)
        	{
        		buttonMinusX.enabled = false;
        		if(builder.info.getSizeX() < 1) builder.info.setSizeX(1);
        	}
        	else
        	{
        		buttonMinusX.enabled = true;
        	}
        	
        	if(builder.info.getSizeY() >= 12)
        	{
        		buttonAddY.enabled = false;
        		if(builder.info.getSizeY() > 12) builder.info.setSizeY(12);
        	}
        	else
        	{
        		buttonAddY.enabled = true;
        	}
        	
        	if(builder.info.getSizeY() <= 1)
        	{
        		buttonMinusY.enabled = false;
        		if(builder.info.getSizeY() < 1) builder.info.setSizeY(1);
        	}
        	else
        	{
        		buttonMinusY.enabled = true;
        	}
    	}
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float i, int j, int k)
    {    	
        super.drawGuiContainerForegroundLayer(j, k);
        
        RenderHelper.disableStandardItemLighting();
        GL11.glColor3f(1, 1, 1);    
        
        int mouseX = j;
        int mouseY = k;
        
        Minecraft.getMinecraft().renderEngine.bindTexture(realTexture);
        GL11.glCallList(list);
        
        fontRendererObj.drawString("Required Materials", guiLeft + 7, guiTop + 7, 0x404040);
        Minecraft.getMinecraft().renderEngine.bindTexture(realTexture);
        GL11.glColor3f(1, 1, 1);
        
        StorageInfo info = builder.info;
        
        if(info == null)
        {
        	return;
        }

        for(int x = 0; x < 4; x++)
        {
            Minecraft.getMinecraft().renderEngine.bindTexture(realTexture);
            GL11.glColor3f(1, 1, 1);
            
            drawTexturedModalRect(guiLeft + 7 + (x * 18), guiTop + 7 + (18 * 2), 18, 0, 18, 18);
        }
        
        for(int x = 0; x < info.getMaterialCost().size(); x++)
        {
            drawTexturedModalRect(guiLeft + 7 + (x * 18), guiTop + 17, 18, 0, 18, 18);
            
            ItemStack stack = info.getMaterialCost().get(x).get(0);
            
            RenderHelper.enableGUIStandardItemLighting();
            itemRender.renderItemIntoGUI(fontRendererObj, mc.renderEngine, stack, guiLeft + 7 + x * 18 + 1, guiTop + 18);
            
            Minecraft.getMinecraft().renderEngine.bindTexture(realTexture);
            RenderHelper.disableStandardItemLighting();
        }
        
        
        
        getFontRenderer().drawString("Size X: " + info.getSizeX(), guiLeft + 162 / 2, guiTop + 15, 0x404040);
        getFontRenderer().drawString("Size Y: " + info.getSizeY(), guiLeft + 162 / 2, guiTop + 35, 0x404040);
    }

    @Override
    public void actionPerformed(GuiButton button) 
    {
    	super.actionPerformed(button);
        StorageInfo info = new StorageInfo(builder.info.getSizeX(), builder.info.getSizeY());

    	switch(button.id)
    	{
    		case 0:
    		{
    			info.setSizeX(info.getSizeX() + 1);
    			CompactStorage.instance.wrapper.sendToServer(new C01PacketUpdateBuilder(pos, builder.dimension, info));
    			
    			break;
    		}
    		case 1:
    		{
    			info.setSizeX(info.getSizeX() - 1);
    			CompactStorage.instance.wrapper.sendToServer(new C01PacketUpdateBuilder(pos, builder.dimension, info));

    			break;
    		}
    		case 2:
    		{
    			info.setSizeY(info.getSizeY() + 1);
    			CompactStorage.instance.wrapper.sendToServer(new C01PacketUpdateBuilder(pos, builder.dimension, info));

    			break;
    		}
    		case 3:
    		{
    			info.setSizeY(info.getSizeY() - 1);
    			CompactStorage.instance.wrapper.sendToServer(new C01PacketUpdateBuilder(pos, builder.dimension, info));

    			break;
    		}
    		case 4:
    		{
    			CompactStorage.instance.wrapper.sendToServer(new C02PacketCraftChest(pos, builder.dimension, info));
    			
    			break;
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
    
    public FontRenderer getFontRenderer()
    {
    	return fontRendererObj;
    }
}
