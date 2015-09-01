package com.tattyseal.compactstorage.client.gui.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

/**
 * Created by tattyseal on 01/09/15.
 */
public class SlotChangePosition extends Slot
{
    public final int x;
    public final int y;
    public SlotChangePosition(IInventory inventory, int id, int x, int y)
    {
        super(inventory, id, x, y);

        this.x = x;
        this.y = y;
    }

    public void changeToDefaultPosition()
    {
        xDisplayPosition = x;
        yDisplayPosition = y;
    }

    public void setPosition(int x, int y)
    {
        xDisplayPosition = x;
        yDisplayPosition = y;
    }

    public void addToPosition(int x, int y)
    {
        xDisplayPosition = xDisplayPosition + x;
        yDisplayPosition = yDisplayPosition + y;
    }

    public void subtractFromPosition(int x, int y)
    {
        xDisplayPosition = xDisplayPosition - x;
        yDisplayPosition = yDisplayPosition - y;
    }
}
