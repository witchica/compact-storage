package com.witchica.compactstorage.item;

import com.witchica.compactstorage.CompactStorage;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StorageUpgradeItem extends Item {
    public StorageUpgradeItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        if(stack.getItem() == CompactStorage.UPGRADE_COLUMN_ITEM.get()) {
            tooltip.add(Text.translatable("tooltip.compact_storage.column_upgrade_descriptor").formatted(Formatting.LIGHT_PURPLE));
        } else if(stack.getItem() == CompactStorage.UPGRADE_ROW_ITEM.get()) {
            tooltip.add(Text.translatable("tooltip.compact_storage.row_upgrade_descriptor").formatted(Formatting.LIGHT_PURPLE));
        }

        tooltip.add(Text.translatable("tooltip.compact_storage.upgrade_backpack").formatted(Formatting.DARK_PURPLE));
    }
}
