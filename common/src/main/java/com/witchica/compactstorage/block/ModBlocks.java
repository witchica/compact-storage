package com.witchica.compactstorage.block;

import net.blay09.mods.balm.world.level.block.BalmBlockRegistrar;
import net.blay09.mods.balm.world.level.block.DiscriminatedBlocks;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import com.witchica.compactstorage.data.StorageType;

public class ModBlocks {

    public static DiscriminatedBlocks<@NotNull StorageType> compactChests;
    public static DiscriminatedBlocks<@NotNull StorageType> compactBarrels;
    public static DiscriminatedBlocks<@NotNull StorageType> itemDrums;

    public static void initialize(BalmBlockRegistrar blocks) {
        compactChests = blocks.registerDiscriminated(
            StorageType.values(),
            StorageType::chestNameFactory, CompactChestBlock::new, StorageType::chestProeprtiesFactory).withDefaultItems((Item.Properties::useBlockDescriptionPrefix)).asDiscriminatedBlocks();

        compactBarrels = blocks.registerDiscriminated(
                StorageType.values(),
                StorageType::barrelNameFactory, CompactBarrelBlock::new, StorageType::barrelPropertiesFactory).withDefaultItems((Item.Properties::useBlockDescriptionPrefix)).asDiscriminatedBlocks();

        itemDrums = blocks.registerDiscriminated(
                StorageType.values(),
                StorageType::itemDrumNameFactory, ItemDrumBlock::new, StorageType::barrelPropertiesFactory).withDefaultItems((Item.Properties::useBlockDescriptionPrefix)).asDiscriminatedBlocks();
    }

}
