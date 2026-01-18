package com.witchica.compactstorage.block;

import net.blay09.mods.balm.world.level.block.BalmBlockRegistrar;
import net.blay09.mods.balm.world.level.block.DiscriminatedBlocks;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import util.StorageTypes;

public class ModBlocks {

    public static DiscriminatedBlocks<@NotNull StorageTypes> metalCompactChests;

    public static void initialize(BalmBlockRegistrar blocks) {
        metalCompactChests = blocks.registerDiscriminated(
            StorageTypes.values(),
            StorageTypes::chestNameFactory, CompactChestBlock::new, StorageTypes::chestProeprtiesFactory).withDefaultItems((Item.Properties::useBlockDescriptionPrefix)).asDiscriminatedBlocks();
    }

}
