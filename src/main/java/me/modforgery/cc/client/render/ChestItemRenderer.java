package me.modforgery.cc.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Created by Toby on 24/08/2014.
 */
public class ChestItemRenderer implements IItemRenderer
{
    public String name;
    public ModelChest chest;
    public ResourceLocation texture;

    public ChestItemRenderer(String name)
    {
        super();

        this.name = name;
        this.chest = new ModelChest();
        this.texture = new ResourceLocation("compactchests", "textures/models/chest_" + name + ".png");
    }

    @Override
    public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type)
    {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper)
    {
        return true;
    }

    @Override
    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data)
    {
        Minecraft.getMinecraft().renderEngine.bindTexture(this.texture);

        GL11.glPushMatrix();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);

        GL11.glTranslatef(0.5f, -0.5f, -0.5f);
        GL11.glRotatef(270F, 0f, 1f, 0f);

        chest.renderAll();

        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
