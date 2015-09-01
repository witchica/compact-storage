package com.tattyseal.compactstorage.api;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Created by Toby on 07/11/2014.
 *
 * Use this as a way to create your own chests that [b]might[/b] be able to use the GuiChest interface.
 */
public interface IChest extends IInventory
{
    public int getInvX();
    public int getInvY();
    
    public void setInvX(int invX);
    public void setInvY(int invY);

    public int getColor();
    public void setColor(int color);

    public int getStartUpgradeSlots();
    public int getAmountUpgradeSlots();
    public ItemStack[] getRequiredUpgrades(int invX, int invY);
}
