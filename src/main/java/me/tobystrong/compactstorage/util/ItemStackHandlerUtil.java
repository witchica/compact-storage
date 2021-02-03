package me.tobystrong.compactstorage.util;

import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerUtil {
    public static ItemStackHandler validateHandlerSize(ItemStackHandler handler, int width, int height) {
        //check if the size matches
        if(handler.getSlots() != width * height) {
            //if not make a new handler with the right size
            ItemStackHandler newInventory = new ItemStackHandler(width * height);

            //copy everything across
            for(int x = 0; x < handler.getSlots(); x++) {
                newInventory.setStackInSlot(x, handler.getStackInSlot(x));
            }

            return newInventory;
        } else {
            return handler;
        }
    }

}
