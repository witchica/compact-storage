package com.witchica.compactstorage.api.inventory;

import com.witchica.compactstorage.data.UpgradeType;

public interface UpgradableContainer {
    boolean canApplyUpgrade(UpgradeType upgradeType);
    boolean applyUpgrade(UpgradeType upgradeType);
}
