package com.tattyseal.compactstorage.api;

import net.minecraft.inventory.IInventory;

/**
 * Created by Toby on 07/11/2014.
 *
 * Basically currently only for people who want to check for any CompactChest.
 *
 * In the future stuff will be here.
 */
public interface IChest extends IInventory
{
    public int getInvX();
    public int getInvY();
    
    public void setInvX(int invX);
    public void setInvY(int invY);

    public int getColor();
    public void setColor(int color);
}
