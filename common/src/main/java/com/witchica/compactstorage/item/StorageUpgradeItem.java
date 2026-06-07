package com.witchica.compactstorage.item;

import com.witchica.compactstorage.data.StorageUpgrade;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class StorageUpgradeItem extends Item {
    private final StorageUpgrade upgradeType;

    public StorageUpgradeItem(Properties properties, StorageUpgrade upgradeType) {
        super(properties);
        this.upgradeType = upgradeType;
    }

    public StorageUpgrade getUpgradeType() {
        return upgradeType;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
        upgradeType.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
    }
}
