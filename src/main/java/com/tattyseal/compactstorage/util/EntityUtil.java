package com.tattyseal.compactstorage.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

/**
 * Created by Toby on 07/11/2014.
 */
public class EntityUtil
{
    public static EnumFacing get2dOrientation(EntityLivingBase entityliving)
    {
        EnumFacing[] orientationTable = {EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.EAST};
        int orientationIndex = MathHelper.floor_double((entityliving.rotationYaw + 45.0) / 90.0) & 3;

        return orientationTable[orientationIndex];
    }
}
