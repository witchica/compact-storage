package me.tobystrong.compactstorage.util;

import net.minecraft.inventory.Inventory;

public interface CompactStorageInventoryImpl {
    public default Inventory getInventory() {
        return (Inventory) this;
    }

    public int getInventoryWidth();
    public int getInventoryHeight();
}