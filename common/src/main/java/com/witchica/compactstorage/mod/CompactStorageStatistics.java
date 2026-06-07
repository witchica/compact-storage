package com.witchica.compactstorage.mod;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.data.StorageUpgrade;
import net.blay09.mods.balm.stats.BalmCustomStatRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import org.intellij.lang.annotations.Identifier;

public class CompactStorageStatistics {
    public static void initialize(BalmCustomStatRegistrar balmCustomStatRegistrar) {
        for(StorageUpgrade upgrade : CompactStorageUpgrades.values()) {
            upgrade.setStatisticIdentifier(balmCustomStatRegistrar.register(upgrade.getStatisticName(), StatFormatter.DEFAULT));
        }
    }
}
