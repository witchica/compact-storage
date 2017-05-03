package com.tattyseal.compactstorage.client.gui.slider;

import net.minecraft.client.gui.GuiSlider;

/**
 * Created by tobystrong on 02/05/2017.
 */
public class HueFormatType implements GuiSlider.FormatHelper {
    @Override
    public String getText(int id, String name, float value)
    {
        if(value == -1)
        {
            return String.format("%s", "White");
        }

        return String.format("%s: %d", name, (int) value);
    }
}
