package com.tattyseal.compactstorage.client.render;

import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;

import com.tattyseal.compactstorage.block.BlockBarrel;
import com.tattyseal.compactstorage.tileentity.TileEntityBarrel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TileEntityBarrelRenderer extends TileEntitySpecialRenderer<TileEntityBarrel>
{
    public RenderItem renderItem;
    public TextureManager textureManager;

    public TileEntityBarrelRenderer()
    {
        super();
        this.renderItem = Minecraft.getMinecraft().getRenderItem();
    }

    @Override
    public void render(TileEntityBarrel te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        renderText(te, x, y, z, 0.01f);
        renderItem(te, x, y, z, 1f, 0.5f);
    }

    public void renderText(TileEntityBarrel tileEntity, double coordX, double coordY, double coordZ, float scale)
    {
        EnumFacing facing = (EnumFacing) tileEntity.getWorld().getBlockState(tileEntity.getPos()).getProperties().get(BlockBarrel.FACING);

        GL11.glPushMatrix();
        GL11.glTranslatef((float) coordX + 0.5f, (float) coordY + 0.5f, (float) coordZ + 0.5f);

        rotateElement(facing);

        GL11.glTranslatef(0f, -0.225f, -0.44f);

        GL11.glScalef(scale, scale, scale);

        FontRenderer fontrenderer = this.getFontRenderer();
        byte b0 = 0;

        String s = tileEntity.getText();

        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, b0);

        GL11.glPopMatrix();
    }
    
    public void rotateElement(EnumFacing facing)
    {
        switch(facing)
        {
            case WEST:
            {
                GL11.glRotatef(180f, 1F, 0.0F, 0f);
                GL11.glRotatef(270f, 0F, 1F, 0f);
                break;
            }
            case EAST:
            {
                GL11.glRotatef(180f, 1F, 0.0F, 0f);
                GL11.glRotatef(90f, 0F, 1F, 0f);
                break;
            }
            case SOUTH:
            {
                GL11.glRotatef(180f, 1F, 0.0F, 0f);
                GL11.glRotatef(180f, 0F, 1F, 0f);
                break;
            }
            case NORTH:
            {
                GL11.glRotatef(180f, 1F, 0.0F, 0f);
                GL11.glRotatef(0f, 0F, 1F, 0f);
                break;
            }
            default:
            {
                GL11.glRotatef(180F, -1F, 0.0F, 3F);
                break;
            }
        }
    }
    
	public void renderItem(TileEntityBarrel tileEntity, double coordX, double coordY, double coordZ, float scale, float size)
	{
		EnumFacing facing = (EnumFacing) tileEntity.getWorld().getBlockState(tileEntity.getPos()).getProperties().get(BlockBarrel.FACING);
		BlockPos pos = tileEntity.getPos();
		ItemStack stack = tileEntity.item.copy();
		
		if (stack.isEmpty())
		{
			return;
		}
		
		stack.setCount(1);
		
		GL11.glPushMatrix();
        EntityItem ent = new EntityItem(tileEntity.getWorld(), coordX, coordY, coordZ, stack);

        ent.hoverStart = 0;

        RenderHelper.enableGUIStandardItemLighting();

        GL11.glTranslatef((float) coordX + 0.5f, (float) coordY +  0.5f, (float) coordZ +  0.5f);
        rotateElement(facing);
        //GL11.glRotatef(180f, 0, 0, 0);
        GL11.glTranslatef(-(size / 3), -0.1f, -0.55f);
        GL11.glScalef(size / 24, size / 24, 0.001f);

        //Minecraft.getMinecraft().getRenderManager().renderEntity(ent, 0, 0, 0,0,0, false);
        this.renderItem.renderItemIntoGUI(stack, 0, 0);
        GL11.glPopMatrix();

		/*GL11.glPushMatrix();

		GL11.glTranslatef(pos.getX(), pos.getY(), pos.getZ());     // We align the rendering on the center of the block
        //GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);

        rotateElement(facing);

        GL11.glTranslated(-0.5F, -0.5F, -0.5f);
        GL11.glScalef(scale, scale, 0.0001f);			  // We flatten the rendering and scale it to the right size
        GL11.glTranslatef(0f, 2f, -0.44f);
        //GL11.glScalef(size, size, 1.0f);

        this.renderItem.renderItemIntoGUI(stack, 0, 0);

		GL11.glPopMatrix();  */
    }

}
