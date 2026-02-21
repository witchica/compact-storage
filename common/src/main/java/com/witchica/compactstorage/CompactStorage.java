package com.witchica.compactstorage;

import com.witchica.compactstorage.mod.CompactStorageBlockEntities;
import com.witchica.compactstorage.mod.CompactStorageMenuTypes;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.core.BalmRegistrars;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.witchica.compactstorage.mod.CompactStorageBlocks;
import com.witchica.compactstorage.mod.CompactStorageItems;

public class CompactStorage {

    public static final Logger logger = LoggerFactory.getLogger(CompactStorage.class);

    public static final String MOD_ID = "compact_storage";

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static CompactStorageConfig config() {
        return Balm.config().getActiveConfig(CompactStorageConfig.class);
    }

    public static void initialize(BalmRegistrars registrars) {
        Balm.config().registerConfig(CompactStorageConfig.class);

        registrars.blocks(CompactStorageBlocks::initialize);
        registrars.items(CompactStorageItems::initializeItems);
        registrars.creativeModeTabs(CompactStorageItems::initializeCreativeTabs);
        registrars.blockEntityTypes(CompactStorageBlockEntities::initialize);
        registrars.menuTypes(CompactStorageMenuTypes::initialize);
    }

}
