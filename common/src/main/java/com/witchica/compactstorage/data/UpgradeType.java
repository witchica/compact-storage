package com.witchica.compactstorage.data;

import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public record UpgradeType(String name, Consumer<Consumer<Component>> tooltip) {
    public static UpgradeType WIDTH_UPGRADE = new UpgradeType("upgrade_row", (componentConsumer) -> {

    });
    public static UpgradeType HEIGHT_UPGRADE = new UpgradeType("upgrade_column", componentConsumer -> {

    });

    public static UpgradeType RETAINING_UPGRADE = new UpgradeType("upgrade_retainer", componentConsumer -> {

    });

    public static UpgradeType VOID_SLOT_UPGRADE = new UpgradeType("void_slot_upgrade", componentConsumer -> {

    });

    public static UpgradeType CHUNK_LOADER_UPGRADE = new UpgradeType("chunk_loader_upgrade", componentConsumer -> {

    });

    public static UpgradeType NAME_TAG_UPGRADE = new UpgradeType("name_tag_upgrade", componentConsumer -> {

    });
}
