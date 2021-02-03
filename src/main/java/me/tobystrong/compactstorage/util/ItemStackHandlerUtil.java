package me.tobystrong.compactstorage.util;

import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerUtil {
    public static ItemStackHandler validateHandlerSize(ItemStackHandler handler, int width, int height) {
        if(handler.getSlots() != width * height) {
            ItemStackHandler newInventory = new ItemStackHandler(width * height);

            for(int x = 0; x < handler.getSlots(); x++) {
                newInventory.setStackInSlot(x, handler.getStackInSlot(x));
            }

            return newInventory;
        } else {
            return handler;
        }
    }

}
