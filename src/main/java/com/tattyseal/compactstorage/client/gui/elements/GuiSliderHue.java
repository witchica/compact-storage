package com.tattyseal.compactstorage.client.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

/**
 * Created by tobystrong on 02/05/2017.
 */
public class GuiSliderHue extends GuiSlider
{
    public GuiSliderHue(GuiPageButtonList.GuiResponder guiResponder, int idIn, int x, int y, String nameIn, float minIn, float maxIn, float defaultValue, FormatHelper formatter)
    {
        super(guiResponder, idIn, x, y, nameIn, minIn, maxIn, defaultValue, formatter);
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY)
    {
        super.mouseDragged(mc, mouseX, mouseY);

        if (this.visible)
        {
            Color color = getSliderPosition() == 0f ? Color.white : Color.getHSBColor(getSliderPosition(), 1f, 1f);

            GlStateManager.color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);

            this.drawTexturedModalRect(this.xPosition + (int)(this.getSliderPosition() * (float)(this.width - 8)), this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int)(this.getSliderPosition() * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }
}
