package com.witchica.compactstorage.common.item;

import com.witchica.compactstorage.CompactStoragePlatform;
import com.witchica.compactstorage.common.util.CompactStorageUpgradeType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class StorageUpgradeItem extends Item {
    private final CompactStorageUpgradeType upgradeType;

    public StorageUpgradeItem(Properties settings, CompactStorageUpgradeType upgradeType) {
        super(settings);
        this.upgradeType = upgradeType;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        if (stack.getItem() == CompactStoragePlatform.getStorageColumnUpgradeItem()) {
            tooltip.add(Component.translatable("tooltip.compact_storage.column_upgrade_descriptor").withStyle(ChatFormatting.LIGHT_PURPLE));
            tooltip.add(Component.translatable("tooltip.compact_storage.upgrade_backpack").withStyle(ChatFormatting.DARK_PURPLE));

        } else if (stack.getItem() == CompactStoragePlatform.getStorageRowUpgradeItem()) {
            tooltip.add(Component.translatable("tooltip.compact_storage.row_upgrade_descriptor").withStyle(ChatFormatting.LIGHT_PURPLE));
            tooltip.add(Component.translatable("tooltip.compact_storage.upgrade_backpack").withStyle(ChatFormatting.DARK_PURPLE));

        } else if (stack.getItem() == CompactStoragePlatform.getStorageRetainerUpgradeItem()) {
            tooltip.add(Component.translatable("tooltip.compact_storage.upgrade_retainer_description").withStyle(ChatFormatting.LIGHT_PURPLE));
            tooltip.add(Component.translatable("tooltip.compact_storage.new_item_description").withStyle(ChatFormatting.GREEN, ChatFormatting.ITALIC));
        }
        super.appendHoverText(stack, world, tooltip, context);
    }

    public CompactStorageUpgradeType getUpgradeType() {
        return upgradeType;
    }
}
