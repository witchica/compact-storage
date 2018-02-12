package com.tattyseal.compactstorage.client.render;

import com.tattyseal.compactstorage.block.BlockBarrel;
import com.tattyseal.compactstorage.tileentity.TileEntityBarrel;
import com.tattyseal.compactstorage.util.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

import javax.swing.text.html.parser.Entity;

public class TileEntityBarrelRenderer extends TileEntitySpecialRenderer<TileEntityBarrel>
{
    public RenderItem renderItem;

    public TileEntityBarrelRenderer()
    {
        super();
        this.renderItem = Minecraft.getMinecraft().getRenderItem();
    }

    @Override
    public void render(TileEntityBarrel te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        renderText(te, x, y, z, 0.0075f);
    }

    public void renderText(TileEntityBarrel tileEntity, double coordX, double coordY, double coordZ, float scale)
    {
        EnumFacing facing = (EnumFacing) tileEntity.getWorld().getBlockState(tileEntity.getPos()).getProperties().get(BlockBarrel.FACING);

        GL11.glPushMatrix();
        GL11.glTranslatef((float) coordX + 0.5f, (float) coordY + 0.5f, (float) coordZ + 0.5f);

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

        GL11.glTranslatef(0f, 0, -0.44f);

        GL11.glScalef(scale, scale, scale);

        FontRenderer fontrenderer = this.getFontRenderer();
        byte b0 = 0;

        String s = tileEntity.getText();

        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, b0);

        GL11.glPopMatrix();

        GL11.glPushMatrix();
        EntityItem ent = new EntityItem(tileEntity.getWorld(), coordX, coordY, coordZ, tileEntity.item);

        ent.hoverStart = 0;

        GL11.glTranslatef((float) coordX, (float) coordY + 2, (float) coordZ);

        Minecraft.getMinecraft().getRenderManager().renderEntity(ent, 0, 0, 0,0,0, false);
        //renderItem.renderItemIntoGUI(tileEntity.item, 0, 0);
        GL11.glPopMatrix();
    }
}
