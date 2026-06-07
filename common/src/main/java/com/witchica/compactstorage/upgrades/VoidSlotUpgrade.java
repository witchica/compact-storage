package com.witchica.compactstorage.upgrades;

import com.witchica.compactstorage.data.StorageUpgrade;
import com.witchica.compactstorage.api.inventory.VoidSlotProvider;
import com.witchica.compactstorage.mod.CompactStorageComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class VoidSlotUpgrade extends StorageUpgrade {
    public VoidSlotUpgrade(String name) {
        super(name);
    }

    @Override
    public DataComponentType<Boolean> getDataComponent() {
        return CompactStorageComponents.VOID_SLOT.value();
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
        if(blockEntity instanceof VoidSlotProvider voidSlotProvider) {
            if(!voidSlotProvider.hasVoidSlot()) {
                voidSlotProvider.setHasVoidSlot(true);
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
        if(blockEntity instanceof VoidSlotProvider voidSlotProvider) {
            return !voidSlotProvider.hasVoidSlot();
        }

        return false;
    }

    @Override
    public Component getFailedUpgradeMessage() {
        return Component.translatable("message.compact_storage.upgrades.void_slot.fail").withStyle(ChatFormatting.RED);
    }

    @Override
    public SoundEvent getAppliedUpgradeSoundEvent() {
        return SoundEvents.ENDERMAN_TELEPORT;
    }
}
