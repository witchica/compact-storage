package com.witchica.compactstorage;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixerBuilder;
import com.witchica.compactstorage.block.entity.ModBlockEntities;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.core.BalmRegistrars;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.witchica.compactstorage.block.ModBlocks;
import com.witchica.compactstorage.item.ModItems;

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

        registrars.blocks(ModBlocks::initialize);
        registrars.items(ModItems::initializeItems);
        registrars.creativeModeTabs(ModItems::initializeCreativeTabs);
        registrars.blockEntityTypes(ModBlockEntities::initialize);
    }

}
