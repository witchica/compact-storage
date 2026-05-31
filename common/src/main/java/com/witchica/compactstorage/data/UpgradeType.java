package com.witchica.compactstorage.data;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public record UpgradeType(String name, Consumer<Consumer<Component>> tooltip, Component upgradeFailMessage, Component upgradeNotCompatibleMessage) {
    public static UpgradeType WIDTH_UPGRADE = new UpgradeType("upgrade_row", (componentConsumer) -> {

    }, Component.translatable("message.compact_storage.upgrades.width.fail").withStyle(ChatFormatting.RED), Component.translatable("message.compact_storage.upgrades.not_allowed").withStyle(ChatFormatting.RED));

    public static UpgradeType HEIGHT_UPGRADE = new UpgradeType("upgrade_column", componentConsumer -> {

    }, Component.translatable("message.compact_storage.upgrades.height.fail").withStyle(ChatFormatting.RED), Component.translatable("message.compact_storage.upgrades.not_allowed").withStyle(ChatFormatting.RED));

    public static UpgradeType RETAINING_UPGRADE = new UpgradeType("upgrade_retainer", componentConsumer -> {

    }, Component.translatable("message.compact_storage.upgrades.retaining.fail").withStyle(ChatFormatting.RED), Component.translatable("message.compact_storage.upgrades.not_allowed").withStyle(ChatFormatting.RED));

    public static UpgradeType VOID_SLOT_UPGRADE = new UpgradeType("void_slot_upgrade", componentConsumer -> {

    }, Component.translatable("message.compact_storage.upgrades.void_slot.fail").withStyle(ChatFormatting.RED), Component.translatable("message.compact_storage.upgrades.not_allowed").withStyle(ChatFormatting.RED));

    public static UpgradeType CHUNK_LOADER_UPGRADE = new UpgradeType("chunk_loader_upgrade", componentConsumer -> {

    }, Component.translatable("message.compact_storage.upgrades.chunk_loader.fail").withStyle(ChatFormatting.RED), Component.translatable("message.compact_storage.upgrades.not_allowed").withStyle(ChatFormatting.RED));

    public static UpgradeType NAME_TAG_UPGRADE = new UpgradeType("name_tag_upgrade", componentConsumer -> {

    }, Component.translatable("message.compact_storage.upgrades.name_tag.fail").withStyle(ChatFormatting.RED), Component.translatable("message.compact_storage.upgrades.not_allowed").withStyle(ChatFormatting.RED));
}
