package com.tattyseal.compactstorage.client.gui.slider;

import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.resources.I18n;

/**
 * Created by tobystrong on 02/05/2017.
 */
public class ColumnFormatType implements GuiSlider.FormatHelper
{
    @Override
    public String getText(int id, String name, float value)
    {
        return String.format("%d %s", (int) value, I18n.format("text.columns"));
    }
}
