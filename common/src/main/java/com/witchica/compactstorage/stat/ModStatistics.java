package com.witchica.compactstorage.stat;

import com.witchica.compactstorage.data.StorageUpgrade;
import com.witchica.compactstorage.upgrades.ModUpgrades;
import net.blay09.mods.balm.stats.BalmCustomStatRegistrar;
import net.minecraft.stats.StatFormatter;

public class ModStatistics {
    public static void initialize(BalmCustomStatRegistrar balmCustomStatRegistrar) {
        for(StorageUpgrade upgrade : ModUpgrades.values()) {
            upgrade.setStatisticIdentifier(balmCustomStatRegistrar.register(upgrade.getStatisticName(), StatFormatter.DEFAULT));
        }
    }
}
