package com.tattyseal.compactstorage.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface IBarrel
{
    public void dropItems(EntityPlayer player);
    public boolean insertItems(@Nonnull ItemStack stack, EntityPlayer player);

    public int color();
}
