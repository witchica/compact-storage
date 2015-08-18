package com.tattyseal.compactstorage.util;

import java.awt.*;

/**
 * Created by Toby on 07/11/2014.
 */
public class ColorUtil
{
    public static int colorToHex(Color color)
    {
        String hex = String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());

        return Integer.parseInt(hex, 16);
    }

    public static Color hexToColor(int hex)
    {
        return Color.decode("#" + Integer.toHexString(hex));
    }

}
