package com.witchica.compactstorage.upgrades;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.CompactStorageConfig;
import com.witchica.compactstorage.api.inventory.ResizableItemDrum;
import com.witchica.compactstorage.data.StorageUpgrade;
import com.witchica.compactstorage.mod.CompactStorageComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Consumer;

public class ItemDrumUpgrade extends StorageUpgrade {
    public ItemDrumUpgrade(String name) {
        super(name);
    }

    @Override
    public DataComponentType<?> getDataComponent() {
        return CompactStorageComponents.ITEM_DRUM_SIZE.value();
    }

    public int upgradeAmount() {
        return Math.clamp(CompactStorage.config().itemDrumUpgradeAmount, 1, 16);
    }

    @Override
    public boolean applyToItemStack(ItemStack stack) {
        return false;
    }

    @Override
    public boolean applyToBlockEntity(BlockEntity blockEntity) {
        if(blockEntity instanceof ResizableItemDrum resizableItemDrum && resizableItemDrum.getSize() < resizableItemDrum.getMaximumSize()) {
            resizableItemDrum.setSize(Math.min(resizableItemDrum.getSize() + upgradeAmount(), resizableItemDrum.getMaximumSize()));
            return true;
        }

        return false;
    }

    @Override
    public boolean isUpgradeValidForItemStack(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isUpgradeValidForBlockEntity(BlockEntity blockEntity) {
        if(blockEntity instanceof ResizableItemDrum resizableItemDrum) {
            return resizableItemDrum.getSize() < resizableItemDrum.getMaximumSize();
        }
        return false;
    }

    @Override
    public Component getFailedUpgradeMessage() {
        return Component.translatable("message.compact_storage.upgrades.item_drum.fail").withStyle(ChatFormatting.RED);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
        tooltipAdder.accept(Component.translatable("tooltip.compact_storage.upgrade_item_drum").withStyle(ChatFormatting.GRAY));
    }
}
