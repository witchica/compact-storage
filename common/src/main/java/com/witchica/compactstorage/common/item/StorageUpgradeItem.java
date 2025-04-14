package com.witchica.compactstorage.common.item;

import com.witchica.compactstorage.common.util.StorageUpgradeType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class StorageUpgradeItem extends Item {
    private final StorageUpgradeType type;

    public StorageUpgradeItem(StorageUpgradeType type, Properties settings) {
        super(settings);
        this.type = type;
    }

    public StorageUpgradeType getType() {
        return type;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        super.appendHoverText(stack, world, tooltip, context);
        tooltip.add(type.getTooltip());

        if(type.isIncludeBackpackText()) {
            tooltip.add(Component.translatable("tooltip.compact_storage.upgrade_backpack").withStyle(ChatFormatting.DARK_PURPLE));
        }
    }
}
