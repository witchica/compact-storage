package com.witchica.compactstorage.common.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public enum StorageUpgradeType {
    ROW(Component.translatable("tooltip.compact_storage.row_upgrade_descriptor").withStyle(ChatFormatting.LIGHT_PURPLE), Component.translatable("text.compact_storage.upgrade_success").withStyle(ChatFormatting.GREEN), Component.translatable("text.compact_storage.upgrade_fail_maxsize").withStyle(ChatFormatting.RED),true),
    COLUMM(Component.translatable("tooltip.compact_storage.column_upgrade_descriptor").withStyle(ChatFormatting.LIGHT_PURPLE), Component.translatable("text.compact_storage.upgrade_success").withStyle(ChatFormatting.GREEN), Component.translatable("text.compact_storage.upgrade_fail_maxsize").withStyle(ChatFormatting.RED),true),
    RETAINING(Component.translatable("tooltip.compact_storage.upgrade_retainer_description").withStyle(ChatFormatting.LIGHT_PURPLE), Component.translatable("text.compact_storage.upgrade_success").withStyle(ChatFormatting.GREEN), Component.translatable("text.compact_storage.retainer_applied").withStyle(ChatFormatting.RED),false),
    DRUM_INCREASE(Component.translatable("tooltip.compact_storage.upgrade_drum_increase_description").withStyle(ChatFormatting.LIGHT_PURPLE), Component.translatable("text.compact_storage.upgrade_success").withStyle(ChatFormatting.GREEN), Component.translatable("text.compact_storage.updrade_fail_maxsize").withStyle(ChatFormatting.RED),false);

    private final boolean includeBackpackText;
    private final Component tooltip;
    private final Component successMessage;
    private final Component failureMessage;

    StorageUpgradeType(Component tooltip, Component successMessage, Component failureMessage, boolean includeBackpackText) {
        this.tooltip = tooltip;
        this.includeBackpackText = includeBackpackText;
        this.successMessage = successMessage;
        this.failureMessage = failureMessage;
    }

    public boolean isIncludeBackpackText() {
        return includeBackpackText;
    }

    public Component getTooltip() {
        return tooltip;
    }

    public Component getSuccessMessage() {
        return successMessage;
    }

    public Component getFailureMessage() {
        return failureMessage;
    }
}
