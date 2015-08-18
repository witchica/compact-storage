package com.tattyseal.compactstorage.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Toby on 07/11/2014.
 */
public class EntityUtil
{
    public static ForgeDirection get2dOrientation(EntityLivingBase entityliving)
    {
        ForgeDirection[] orientationTable = {ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.NORTH, ForgeDirection.EAST};
        int orientationIndex = MathHelper.floor_double((entityliving.rotationYaw + 45.0) / 90.0) & 3;

        return orientationTable[orientationIndex];
    }
}
