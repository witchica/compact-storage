package com.witchica.compactstorage.api.inventory;

import com.witchica.compactstorage.data.StorageUpgrade;

public interface UpgradeCheckProvider {
    boolean isUpgradeAccepted(StorageUpgrade upgrade);
}
