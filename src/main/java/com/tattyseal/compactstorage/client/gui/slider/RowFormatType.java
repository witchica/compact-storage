package com.tattyseal.compactstorage.client.gui.slider;

import javax.annotation.Nonnull;

import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.resources.I18n;

/**
 * Created by tobystrong on 02/05/2017.
 */
public class RowFormatType implements GuiSlider.FormatHelper {
	@Override
	@Nonnull
	public String getText(int id, @Nonnull String name, float value) {
		return String.format("%d %s", (int) value, I18n.format("text.rows"));
	}
}
