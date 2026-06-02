package com.witchica.compactstorage;

import net.blay09.mods.balm.platform.config.reflection.Comment;
import net.blay09.mods.balm.platform.config.reflection.Config;
import net.blay09.mods.balm.platform.config.reflection.NestedType;
import net.blay09.mods.balm.platform.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.platform.config.schema.ConfigSchemaBuilder;
import net.blay09.mods.balm.platform.config.schema.ConfiguredInt;
import net.minecraft.resources.Identifier;

import java.util.List;

@Config(CompactStorage.MOD_ID)
public class CompactStorageConfig {

    @Comment("Use per-type inventory rendering? eg: Oak Chest has Oak Texture in GUI")
    public boolean useFancyInventoryRendering = true;

    @Comment("Maximum inventory width of Compact Chests (Default Value: 9, Range: 9 - 21)")
    public int compactChestDefaultWidth = 9;
    @Comment("Maximum inventory width of Compact Chests (Default Value: 3, Range 3 - 12)")
    public int compactChestDefaultHeight = 3;

    @Comment("Maximum inventory width of Compact Chests (DefaultValue: 21, Range: 9 - 21)")
    public int compactChestMaxWidth = 21;
    @Comment("Maximum inventory width of Compact Chests (Default Value: 12, Range 3 - 12)")
    public int compactChestMaxHeight = 12;

    @Comment("Maximum inventory width of Compact Barrels (Default Value: 9, Range: 9 - 21)")
    public int compactBarrelDefaultWidth = 9;
    @Comment("Maximum inventory width of Compact Barrels (Default Value: 3 Range 3 - 12)")
    public int compactBarrelDefaultHeight = 3;

    @Comment("Maximum inventory width of Compact Barrels (DefaultValue: 21, Range: 9 - 21)")
    public int compactBarrelMaxWidth = 21;
    @Comment("Maximum inventory width of Compact Barrels (Default Value: 12, Range 3 - 12)")
    public int compactBarrelMaxHeight = 12;

    @Comment("Maximum inventory width of Backpacks (Default Value: 9, Range: 9 - 21)")
    public int backpackDefaultWidth = 9;
    @Comment("Maximum inventory width of Backpacks (Default Value: 3 Range 3 - 12)")
    public int backpackDefaultHeight = 3;

    @Comment("Maximum inventory width of Backpacks (DefaultValue: 21, Range: 9 - 21)")
    public int backpackMaxWidth = 21;
    @Comment("Maximum inventory width of Backpacks (Default Value: 12, Range 3 - 12)")
    public int backpackMaxHeight = 12;

    public int constrainWidth(int value) {
        return Math.clamp(value, 9, 21);
    }

    public int constrainHeight(int value) {
        return Math.clamp(value, 3, 12);
    }
}
