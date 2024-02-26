package com.witchica.compactstorage.common.util;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public interface CompactStorageInventoryImpl {
    public default Container getInventory() {
        return (Container) this;
    }

    public NonNullList<ItemStack> getItemList();

    public int getInventoryWidth();
    public int getInventoryHeight();

    public boolean increaseSize(int x, int y);
    public void applyRetainingUpgrade();
    public boolean canUpgradeTypeBeApplied(CompactStorageUpgradeType upgradeType);

    public boolean hasUpgrade(CompactStorageUpgradeType upgradeType);

    public boolean applyUpgrade(CompactStorageUpgradeType upgradeType);

}
