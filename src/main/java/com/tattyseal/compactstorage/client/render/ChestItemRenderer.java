package com.tattyseal.compactstorage.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Toby on 10/02/2015.
 */
/*public class ChestItemRenderer implements IItemRenderer
{
    public ModelChest modelChest;
    public static final ResourceLocation texture = new ResourceLocation("compactstorage", "textures/models/chest.png");

    public ChestItemRenderer()
    {
        this.modelChest = new ModelChest();
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return helper != ItemRendererHelper.ENTITY_ROTATION;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        switch(type)
        {
            case INVENTORY:
            {
                render(item);
                break;
            }
            case EQUIPPED:
            {
                GL11.glRotatef(90F, 0f, -1f, 0f);
                GL11.glTranslatef(1f, 1f, 0f);
                render(item);
                break;
            }
            case ENTITY:
            {
                GL11.glTranslatef(0f, 1f, 0f);
                render(item);
                break;
            }
            case EQUIPPED_FIRST_PERSON:
            {
                GL11.glTranslatef(1f, 1f, 1f);
                render(item);
                break;
            }
        }
    }

    public void render(ItemStack item)
    {
        int color = 0xFFFFFF;

        if(item.hasTagCompound() && item.getTagCompound().hasKey("color"))
        {
            if(item.getTagCompound().getTag("color") instanceof NBTTagString && !item.getTagCompound().getString("color").replace("0x", "").isEmpty())
            {
                color = Integer.decode(item.getTagCompound().getString("color"));
            }
            else if(item.getTagCompound().getTag("color") instanceof NBTTagInt)
            {
                color = item.getTagCompound().getInteger("color");
            }
        }

        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;

        GL11.glColor3f(r, g, b);
        GL11.glRotatef(180F, 0F, 0F, 1f);
        GL11.glRotatef(90F, 0f, 1f, 0f);
        GL11.glTranslatef(0f, -0.1f, 0f);

        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        modelChest.renderAll();
    }
}*/
