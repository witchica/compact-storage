package com.tattyseal.compactstorage.event;

import com.tattyseal.compactstorage.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

/**
 * Created by tobystrong on 27/04/2017.
 */
public class GuiOverlayEvent
{
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer r = mc.fontRendererObj;

        if(mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            BlockPos pos = mc.objectMouseOver.getBlockPos();
            IBlockState state = mc.world.getBlockState(pos);

            if(state != null && state.getBlock() != null && state.getBlock() instanceof BlockChest)
            {
                GL11.glPushMatrix();
                GL11.glScalef(0.7f, 0.7f, 0.7f);
                r.drawString("Did you know can SHIFT click on Compact Chests with a Diamond to make them retain their contents when destroyed?", 5, 5, 0xFFFFFF);
                GL11.glPopMatrix();
            }
        }
    }
}
