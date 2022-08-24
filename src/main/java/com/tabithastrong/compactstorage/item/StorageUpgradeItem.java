package com.tabithastrong.compactstorage.item;

import com.tabithastrong.compactstorage.CompactStorage;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class StorageUpgradeItem extends Item {
    public enum StorageUpgradeLevel {
        COPPER,
        IRON,
        GOLD,
        DIAMOND,
        EMERALD;
    }

    public enum StorageUpgradeDirection {
        COLUMN,
        ROW;
    }

    public StorageUpgradeLevel upgradeLevel;
    public StorageUpgradeDirection upgradeDirection;

    public StorageUpgradeItem(Properties settings, StorageUpgradeLevel level,StorageUpgradeDirection direction) {
        super(settings);
        this.upgradeLevel = level;
        this.upgradeDirection = direction;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        super.appendHoverText(stack, world, tooltip, context);

        if(stack.getItem() instanceof StorageUpgradeItem storageUpgradeItem) {
            if(storageUpgradeItem.upgradeDirection == StorageUpgradeDirection.COLUMN) {
                tooltip.add(Component.translatable("tooltip.compact_storage.column_upgrade_descriptor").withStyle(ChatFormatting.LIGHT_PURPLE));
            } else if(storageUpgradeItem.upgradeDirection == StorageUpgradeDirection.ROW) {
                tooltip.add(Component.translatable("tooltip.compact_storage.row_upgrade_descriptor").withStyle(ChatFormatting.LIGHT_PURPLE));
            }
        }

        tooltip.add(Component.translatable("tooltip.compact_storage.upgrade_backpack").withStyle(ChatFormatting.DARK_PURPLE));
    }
}
