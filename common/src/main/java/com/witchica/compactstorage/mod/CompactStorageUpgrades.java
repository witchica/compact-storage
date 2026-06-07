package com.witchica.compactstorage.mod;

import com.witchica.compactstorage.data.StorageUpgrade;
import com.witchica.compactstorage.upgrades.ResizableUpgrade;
import com.witchica.compactstorage.upgrades.RetainingUpgrade;
import com.witchica.compactstorage.upgrades.VoidSlotUpgrade;

import java.util.ArrayList;

public class CompactStorageUpgrades {
    public static ResizableUpgrade WIDTH_UPGRADE = new ResizableUpgrade("width", ResizableUpgrade.ResizeType.WIDTH);
    public static ResizableUpgrade HEIGHT_UPGRADE = new ResizableUpgrade("height", ResizableUpgrade.ResizeType.HEIGHT);
    public static RetainingUpgrade RETAINING_UPGRADE = new RetainingUpgrade("retainer");
    public static VoidSlotUpgrade VOID_SLOT_UPGRADE = new VoidSlotUpgrade("void_slot");

    public static final ArrayList<StorageUpgrade> UPGRADES = new ArrayList<>();

    static {
        UPGRADES.add(WIDTH_UPGRADE);
        UPGRADES.add(HEIGHT_UPGRADE);
        UPGRADES.add(RETAINING_UPGRADE);
        UPGRADES.add(VOID_SLOT_UPGRADE);
    }
}
