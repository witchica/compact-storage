package com.witchica.compactstorage.common.util;

public enum CompactStorageUpgradeType {
    WIDTH_INCREASE("text.compact_storage.upgrade_success", "text.compact_storage.upgrade_fail_maxsize"),
    HEIGHT_INCREASE("text.compact_storage.upgrade_success", "text.compact_storage.upgrade_fail_maxsize"),
    RETAINING("text.compact_storage.upgrade_success", "text.compact_storage.retainer_applied");

    public final String upgradeSuccess;
    public final String upgradeFail;

    CompactStorageUpgradeType(String upgradeSuccess, String upgradeFail) {
        this.upgradeSuccess = upgradeSuccess;
        this.upgradeFail = upgradeFail;
    }
}
