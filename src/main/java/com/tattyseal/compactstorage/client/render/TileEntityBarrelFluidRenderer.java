package com.tattyseal.compactstorage.client.render;

import com.tattyseal.compactstorage.tileentity.TileEntityBarrelFluid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class TileEntityBarrelFluidRenderer extends TileEntitySpecialRenderer<TileEntityBarrelFluid>
{
    public static final ResourceLocation blockSheet = new ResourceLocation("textures/atlas/blocks.png");

    @Override
    public void render(TileEntityBarrelFluid te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        FluidStack stack = te.tank.getFluid();

        if (stack != null)
        {
            TextureAtlasSprite tex = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(stack.getFluid().getStill().toString());

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder builder = tessellator.getBuffer();


            bindTexture(blockSheet);

            GL11.glPushMatrix();

            GL11.glTranslated(x, y, z);

            GL11.glDisable(GL11.GL_LIGHTING);

            float increments = (1f / 16f);

            float minXZ = increments * 3f;
            float maxXZ = increments * 13f;

            float baseHeight = increments;
            float height = ((increments * 14f) / te.tank.getCapacity()) * te.tank.getFluidAmount() + baseHeight;

            double minU = (double)tex.getInterpolatedU(3D);
            double maxU = (double)tex.getInterpolatedU(13D);
            double minV = (double)tex.getInterpolatedV(3D);
            double maxV = (double)tex.getInterpolatedV(13D);

            double minV_side = (double)tex.getInterpolatedV(0D);
            double maxV_side = (double)tex.getInterpolatedV((te.tank.getFluidAmount() - 0) * (16 - 0) / (te.tank.getCapacity() - 0) + 0);

            builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            builder.pos(maxXZ, height, maxXZ).tex(maxU, maxV).endVertex();
            builder.pos(maxXZ, height, minXZ).tex(maxU, minV).endVertex();
            builder.pos(minXZ, height, minXZ).tex(minU, minV).endVertex();
            builder.pos(minXZ, height, maxXZ).tex(minU, maxV).endVertex();

            builder.pos(minXZ, baseHeight, maxXZ).tex(maxU, maxV_side).endVertex();
            builder.pos(minXZ, height, maxXZ).tex(maxU, minV_side).endVertex();
            builder.pos(minXZ, height, minXZ).tex(minU, minV_side).endVertex();
            builder.pos(minXZ, baseHeight, minXZ).tex(minU, maxV_side).endVertex();

            builder.pos(maxXZ, baseHeight, minXZ).tex(maxU, maxV_side).endVertex();
            builder.pos(maxXZ, height, minXZ).tex(maxU, minV_side).endVertex();
            builder.pos(maxXZ, height, maxXZ).tex(minU, minV_side).endVertex();
            builder.pos(maxXZ, baseHeight, maxXZ).tex(minU, maxV_side).endVertex();

            builder.pos(minXZ, baseHeight, minXZ).tex(maxU, maxV_side).endVertex();
            builder.pos(minXZ, height, minXZ).tex(maxU, minV_side).endVertex();
            builder.pos(maxXZ, height, minXZ).tex(minU, minV_side).endVertex();
            builder.pos(maxXZ, baseHeight, minXZ).tex(minU, maxV_side).endVertex();

            builder.pos(maxXZ, baseHeight, maxXZ).tex(maxU, maxV_side).endVertex();
            builder.pos(maxXZ, height, maxXZ).tex(maxU, minV_side).endVertex();
            builder.pos(minXZ, height, maxXZ).tex(minU, minV_side).endVertex();
            builder.pos(minXZ, baseHeight, maxXZ).tex(minU, maxV_side).endVertex();

            tessellator.draw();

            GL11.glPopMatrix();
        }
    }
}
