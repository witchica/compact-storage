package com.witchica.compactstorage.data;

import com.witchica.compactstorage.api.inventory.UpgradeCheckProvider;
import com.witchica.compactstorage.item.StorageUpgradeItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Consumer;

public abstract class StorageUpgrade {
    private String name;

    public StorageUpgrade(String name) {
        this.name = name;
    }

    public abstract DataComponentType<?> getDataComponent();

    public abstract boolean applyToItemStack(ItemStack stack);
    public abstract boolean applyToBlockEntity(BlockEntity blockEntity);

    public abstract boolean isUpgradeValidForItemStack(ItemStack stack);
    public abstract boolean isUpgradeValidForBlockEntity(BlockEntity blockEntity);

    public Component getIncompatibleUpgradeMessage() {
        return Component.translatable("message.compact_storage.upgrades.not_allowed").withStyle(ChatFormatting.RED);
    }

    public abstract Component getFailedUpgradeMessage();
    public SoundEvent getFailedUpgradeSoundEvent() {
        return SoundEvents.FIRE_EXTINGUISH;
    }
    public SoundEvent getAppliedUpgradeSoundEvent() {
        return SoundEvents.EXPERIENCE_ORB_PICKUP;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {

    }

    public String getName() {
        return this.name;
    }

    public String getItemName() {
        return getName() + "_upgrade";
    }

    public SoundEvent getIncompatibleUpgradeSound() {
        return SoundEvents.FIRE_EXTINGUISH;
    }

    public static InteractionResult applyUpgradeToItem(ItemStack upgradeStack, ItemStack stackToUpgrade, Player player, Level level) {
        if(upgradeStack.getItem() instanceof StorageUpgradeItem storageUpgradeItem) {
            StorageUpgrade upgrade = storageUpgradeItem.getUpgradeType();

            if(stackToUpgrade.getItem() instanceof UpgradeCheckProvider checkProvider) {
                if(checkProvider.isUpgradeAccepted(upgrade)) {
                    if(upgrade.isUpgradeValidForItemStack(stackToUpgrade)) {
                        if(upgrade.applyToItemStack(stackToUpgrade)) {
                            upgradeStack.setCount(upgradeStack.getCount() - 1);
                            level.playSound(null, player.getOnPos(), upgrade.getAppliedUpgradeSoundEvent(), SoundSource.NEUTRAL, 1f, 1f);
                            return InteractionResult.CONSUME;
                        }
                    }

                    level.playSound(null, player.getOnPos(), upgrade.getFailedUpgradeSoundEvent(), SoundSource.NEUTRAL, 1f, 1f);
                    player.sendOverlayMessage(upgrade.getFailedUpgradeMessage());
                    return InteractionResult.FAIL;
                } else {
                    level.playSound(null, player.getOnPos(), upgrade.getIncompatibleUpgradeSound(), SoundSource.NEUTRAL, 1f, 1f);
                    player.sendOverlayMessage(upgrade.getIncompatibleUpgradeMessage());
                    return InteractionResult.FAIL;
                }
            }
        }

        return InteractionResult.PASS;
    }

    public static InteractionResult applyToBlockEntity(ItemStack upgradeStack, BlockEntity blockEntity, Player player, Level level) {
        if(upgradeStack.getItem() instanceof StorageUpgradeItem upgradeItem) {
            StorageUpgrade upgrade = upgradeItem.getUpgradeType();

            if(blockEntity instanceof UpgradeCheckProvider checkProvider) {
                if(checkProvider.isUpgradeAccepted(upgrade)) {
                    if(upgrade.isUpgradeValidForBlockEntity(blockEntity)) {
                        if(upgrade.applyToBlockEntity(blockEntity)) {
                            upgradeStack.setCount(upgradeStack.getCount() - 1);
                            level.playSound(null, player.getOnPos().above(1), upgrade.getAppliedUpgradeSoundEvent(), SoundSource.BLOCKS, 1f, 1f);
                            return InteractionResult.CONSUME;
                        }
                    }

                    level.playSound(null, player.getOnPos().above(1), upgrade.getFailedUpgradeSoundEvent(), SoundSource.BLOCKS, 1f, 1f);
                    player.sendOverlayMessage(upgrade.getFailedUpgradeMessage());
                    return InteractionResult.FAIL;
                } else {
                    level.playSound(null, player.getOnPos().above(1), upgrade.getIncompatibleUpgradeSound(), SoundSource.BLOCKS, 1f, 1f);
                    player.sendOverlayMessage(upgrade.getIncompatibleUpgradeMessage());
                    return InteractionResult.FAIL;
                }
            }
        }

        return InteractionResult.PASS;
    }
}
