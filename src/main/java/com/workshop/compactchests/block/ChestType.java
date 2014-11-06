package com.workshop.compactchests.block;

import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Toby on 06/11/2014.
 */
public enum ChestType
{
    DOUBLE(0, "double", 0x0066CC),
    TRIPLE(1, "triple", 0x00CC00),
    QUADRUPLE(2, "quadruple", 0xFF9900),
    QUINTUPLE(3, "quintuple", 0xFF0000),
    SEXTUPLE(4, "sextuple", 0xCC0099);

    public int id;
    public String name;
    public int defaultColor;
    public ResourceLocation gui;

    public int width;
    public int height;

    ChestType(int id, String name, int defaultColor)
    {
        this.id = id;
        this.name = name;
        this.defaultColor = defaultColor;

        this.gui = new ResourceLocation("compactstorage", "textures/gui/" + name + "Chest.png");

        ImageIcon icon = new ImageIcon(getClass().getResource("/assets/" + gui.getResourceDomain() + "/" + gui.getResourcePath()));
        this.width = icon.getIconWidth();
        this.height = icon.getIconHeight();
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }
}