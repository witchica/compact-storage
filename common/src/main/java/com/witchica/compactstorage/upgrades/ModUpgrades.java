package com.witchica.compactstorage.upgrades;

import com.witchica.compactstorage.data.StorageUpgrade;

import java.util.ArrayList;
import java.util.List;

public class ModUpgrades {
    public static ResizableUpgrade WIDTH_UPGRADE = new ResizableUpgrade("width", ResizableUpgrade.ResizeType.WIDTH);
    public static ResizableUpgrade HEIGHT_UPGRADE = new ResizableUpgrade("height", ResizableUpgrade.ResizeType.HEIGHT);
    public static RetainingUpgrade RETAINING_UPGRADE = new RetainingUpgrade("retainer");
    public static ItemDrumUpgrade ITEM_DRUM_UPGRADE = new ItemDrumUpgrade("item_drum");
    public static VoidSlotUpgrade VOID_SLOT_UPGRADE = new VoidSlotUpgrade("void_slot");

    public static final ArrayList<StorageUpgrade> UPGRADES = new ArrayList<>();

    static {
        register(WIDTH_UPGRADE);
        register(HEIGHT_UPGRADE);
        register(RETAINING_UPGRADE);
        register(ITEM_DRUM_UPGRADE);
        register(VOID_SLOT_UPGRADE);
    }

    private static void register(StorageUpgrade upgrade) {
        UPGRADES.add(upgrade);
    }

    public static List<StorageUpgrade> values() {
        return UPGRADES;
    }
}
