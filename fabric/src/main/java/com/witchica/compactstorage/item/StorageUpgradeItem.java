package com.witchica.compactstorage.item;

import com.witchica.compactstorage.CompactStorage;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class StorageUpgradeItem extends Item {
    public StorageUpgradeItem(Properties settings) {
        super(settings);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        super.appendHoverText(stack, world, tooltip, context);

        if(stack.getItem() == CompactStorage.UPGRADE_COLUMN_ITEM.get()) {
            tooltip.add(Component.translatable("tooltip.compact_storage.column_upgrade_descriptor").withStyle(ChatFormatting.LIGHT_PURPLE));
        } else if(stack.getItem() == CompactStorage.UPGRADE_ROW_ITEM.get()) {
            tooltip.add(Component.translatable("tooltip.compact_storage.row_upgrade_descriptor").withStyle(ChatFormatting.LIGHT_PURPLE));
        }

        tooltip.add(Component.translatable("tooltip.compact_storage.upgrade_backpack").withStyle(ChatFormatting.DARK_PURPLE));
    }
}
