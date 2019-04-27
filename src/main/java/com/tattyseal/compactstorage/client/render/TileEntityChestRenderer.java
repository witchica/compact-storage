package com.tattyseal.compactstorage.client.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.tattyseal.compactstorage.tileentity.TileEntityChest;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Toby on 06/11/2014.
 */
@SideOnly(Side.CLIENT)
public class TileEntityChestRenderer extends TileEntitySpecialRenderer<TileEntityChest> {
	private ModelChest model;
	private static final ResourceLocation texture = new ResourceLocation("compactstorage", "textures/models/chest.png");

	@Override
	public void render(TileEntityChest tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		GL11.glPushMatrix();

		GL11.glTranslatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
		GL11.glScalef(1.0F, -1.0F, -1.0F);

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);

		EnumFacing direction = tile.direction;

		switch (direction) {
		case SOUTH:
			GL11.glRotatef(180f, 0f, 1f, 0f);
			break;
		case WEST:
			GL11.glRotatef(-90f, 0f, 1f, 0f);
			break;
		case EAST:
			GL11.glRotatef(90f, 0f, 1f, 0f);
			break;
		default:
			break;
		}

		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		int color;

		try {
			color = tile.color.brighter().getRGB();
		} catch (Exception exception) {
			color = Color.white.getRGB();
		}

		float r = (color >> 16 & 255) / 255.0F;
		float g = (color >> 8 & 255) / 255.0F;
		float b = (color & 255) / 255.0F;
		GL11.glColor4f(r, g, b, 1F);

		float f = tile.prevLidAngle + (tile.lidAngle - tile.prevLidAngle) * partialTicks;

		f = 1.0F - f;
		f = 1.0F - f * f * f;

		model.chestLid.rotateAngleX = -(f * ((float) Math.PI / 2F));
		model.renderAll();

		GL11.glColor3f(1f, 1f, 1f);

		if (tile.getRetaining()) {
			ItemStack stack = new ItemStack(Items.DIAMOND, 1, 0);
			EntityItem item = new EntityItem(tile.getWorld(), 0D, 0D, 0D, stack);
			item.hoverStart = 0.0F;

			GL11.glRotatef(180, 0, 0, 1);
			GL11.glTranslatef(-0.5f, -1.1f, 0.01f);

			Minecraft.getMinecraft().getRenderManager().renderEntity(item, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F, false);
		}

		GL11.glPopMatrix();
	}
}
