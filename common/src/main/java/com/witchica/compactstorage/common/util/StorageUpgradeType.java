package com.witchica.compactstorage.common.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public enum StorageUpgradeType {
    ROW(Component.translatable("tooltip.compact_storage.row_upgrade_descriptor").withStyle(ChatFormatting.LIGHT_PURPLE), true),
    COLUMM(Component.translatable("tooltip.compact_storage.column_upgrade_descriptor").withStyle(ChatFormatting.LIGHT_PURPLE), true),
    RETAINING(Component.translatable("tooltip.compact_storage.upgrade_retainer_description").withStyle(ChatFormatting.LIGHT_PURPLE), false),
    DRUM_INCREASE(Component.translatable("tooltip.compact_storage.upgrade_drum_increase_description").withStyle(ChatFormatting.LIGHT_PURPLE), false);

    private final boolean includeBackpackText;
    private final Component tooltip;

    StorageUpgradeType(Component tooltip, boolean includeBackpackText) {
        this.tooltip = tooltip;
        this.includeBackpackText = includeBackpackText;
    }

    public boolean isIncludeBackpackText() {
        return includeBackpackText;
    }

    public Component getTooltip() {
        return tooltip;
    }
}
