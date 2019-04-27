package com.tattyseal.compactstorage.api;

import com.tattyseal.compactstorage.util.StorageInfo;
import net.minecraft.inventory.IInventory;

/**
 * Created by Toby on 07/11/2014.
 *
 * Basically currently only for people who want to check for any CompactChest.
 *
 * In the future stuff will be here.
 */
public interface IChest extends IInventory {
	int getInvX();

	int getInvY();

	StorageInfo getInfo();

	@Deprecated
	int getColor();

	/**
	 *  This is used by my mods if you want chests to connect to the network.
	 *  This is return from configuration files by default but can also be toggle by a switch in a GUI if you want it to.
	 *  You just need to return something.
	 *
	 * Please don't break it.
	 *
	 * @return true if you want this Chest to connect to ES networks.
	 */
	boolean shouldConnectToNetwork();

	/***
	 * @return true if the IChest should retain its contents when broken (for items this will always be true)
	 */
	boolean getRetaining();

	/***
	 * Sets the retaining property
	 * NOTE: On backpacks this will do nothing as they cannot be non-retentive
	 */
	void setRetaining(boolean retaining);

	int getHue();

	void setHue(int hue);
}
