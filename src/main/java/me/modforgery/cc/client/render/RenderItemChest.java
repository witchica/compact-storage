package me.modforgery.cc.client.render;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.model.ModelChest;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

/**
 * Created by Toby on 24/08/2014.
 */
public class RenderItemChest implements IItemRenderer
{
    public String type;

    public ModelChest chest;

    public RenderItemChest(String type)
    {
        super();
        this.type = type;

        this.chest = new ModelChest();
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        float scale = 1.08f;

        switch(type)
        {
            case ENTITY:
            {
                renderChest(0f, 0f, 0f, scale);
                break;
            }
            case EQUIPPED:
            {
                renderChest(2f, 1.5f, 0.8f, scale);
                break;
            }
            case INVENTORY:
            {
                renderChest(0f, 0.15f, 0.8f, scale);
                break;
            }
            default: return;
        }
    }

    private void renderChest(float x, float y, float z, float scale)
    {
        GL11.glPushMatrix();

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glTranslatef(x, y, z);
        GL11.glScalef(scale, scale, scale);
        GL11.glRotatef(180f, 0f, 0f, 0f);
        ResourceLocation test = new ResourceLocation("compactchests", "textures/models/chest_" + type + ".png");

        GL11.glMatrixMode(GL11.GL_PROJECTION);

        FMLClientHandler.instance().getClient().renderEngine.bindTexture(test);
        chest.renderAll();

        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
}
