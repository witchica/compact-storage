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
 
public interface IChest extends IInventory {
    public int getInvX();
    public int getInvY();
    public int getInvZ();

    public StorageInfo getInfo();

    public int getColor();

    /**
     *  This is used by my mods if you want chests to connect to the network.
     *  This is return from configuration files by default but can also be toggle by a switch in a GUI if you want it to.
     *  You just need to return something.
     *
     * Please don't break it.
     *
     * @return true if you want this Chest to connect to ES networks.
     */
    public boolean shouldConnectToNetwork();
}
