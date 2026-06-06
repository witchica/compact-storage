package com.witchica.compactstorage.api.inventory;

import com.witchica.compactstorage.api.StorageUpgrade;

public interface UpgradeCheckProvider {
    boolean isUpgradeAccepted(StorageUpgrade upgrade);
}
