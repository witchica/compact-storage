package com.workshop.compactchests.block;

import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Toby on 06/11/2014.
 */
public enum ChestType {
    DOUBLE(0, "double", 0x0066CC, 9, 6),
    TRIPLE(1, "triple", 0x00CC00, 9, 9),
    QUADRUPLE(2, "quadruple", 0xFF9900, 12, 9),
    QUINTUPLE(3, "quintuple", 0xFF0000, 15, 9),
    SEXTUPLE(4, "sextuple", 0xCC0099, 18, 9);

    public int id;
    public String name;
    public int defaultColor;
    public ResourceLocation gui;

    public int width;
    public int height;

    public int iconWidth;
    public int iconHeight;

    ChestType(int id, String name, int defaultColor, int width, int height) {
        this.id = id;
        this.name = name;
        this.defaultColor = defaultColor;

        this.gui = new ResourceLocation("compactstorage", "textures/gui/" + name + "Chest.png");

        ImageIcon icon = new ImageIcon(getClass().getResource("/assets/" + gui.getResourceDomain() + "/" + gui.getResourcePath()));
        this.iconWidth = icon.getIconWidth();
        this.iconHeight = icon.getIconHeight();

        this.width = width;
        this.height = height;
    }

    public int getIconWidth() {
        return iconWidth;
    }

    public int getIconHeight() {
        return iconHeight;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight()
    {
        return height;
    }
}