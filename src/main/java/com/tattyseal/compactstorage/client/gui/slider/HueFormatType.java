package com.tattyseal.compactstorage.client.gui.slider;

import net.minecraft.client.gui.GuiSlider;

import javax.annotation.Nonnull;

/**
 * Created by tobystrong on 02/05/2017.
 */
public class HueFormatType implements GuiSlider.FormatHelper {
	@Override
	@Nonnull
	public String getText(int id, @Nonnull String name, float value) {
		if (value == -1) { return String.format("%s", "White"); }

		return String.format("%s: %d", name, (int) value);
	}
}
