package com.tattyseal.compactstorage.api;

import com.tattyseal.compactstorage.util.StorageInfo;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Created by Toby on 07/11/2014.
 *
 * Basically currently only for people who want to check for any CompactChest.
 *
 * In the future stuff will be here.
 */
public interface IChest {

	int getInvX();

	int getInvY();

	StorageInfo getInfo();

	int getHue();

	void setHue(int hue);

	void onOpened(EntityPlayer player);

	void onClosed(EntityPlayer player);

	ItemStackHandler getItems();
}
