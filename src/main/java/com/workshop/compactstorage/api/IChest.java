package com.workshop.compactstorage.api;

import com.workshop.compactstorage.util.StorageInfo;
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

    public StorageInfo getInfo();

    public int getColor();
}
