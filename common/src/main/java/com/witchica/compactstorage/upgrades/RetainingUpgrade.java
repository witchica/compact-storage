package com.witchica.compactstorage.upgrades;

import com.witchica.compactstorage.data.StorageUpgrade;
import com.witchica.compactstorage.api.inventory.RetainingContainer;
import com.witchica.compactstorage.mod.CompactStorageComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RetainingUpgrade extends StorageUpgrade {
    public RetainingUpgrade(String name) {
        super(name);
    }

    @Override
    public DataComponentType<Boolean> getDataComponent() {
        return CompactStorageComponents.RETAINING_DATA.value();
    }

    @Override
    public boolean applyToItemStack(ItemStack stack) {
        if(!stack.getOrDefault(getDataComponent(), false)) {
            stack.set(getDataComponent(), true);
            return true;
        }

        return false;
    }

    @Override
    public boolean applyToBlockEntity(BlockEntity blockEntity) {
        if(blockEntity instanceof RetainingContainer retainingContainer) {
            if(!retainingContainer.isRetaining()) {
                retainingContainer.setRetaining(true);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isUpgradeValidForItemStack(ItemStack stack) {
        return !stack.getOrDefault(getDataComponent(), false);
    }

    @Override
    public boolean isUpgradeValidForBlockEntity(BlockEntity blockEntity) {
        if(blockEntity instanceof RetainingContainer retainingContainer) {
            return !retainingContainer.isRetaining();
        }

        return false;
    }

    @Override
    public Component getFailedUpgradeMessage() {
        return Component.translatable("message.compact_storage.upgrades.retaining.fail").withStyle(ChatFormatting.RED);
    }
}
