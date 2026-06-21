package com.witchica.compactstorage.item;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.data.StorageUpgrade;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

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

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if(!level.isClientSide()) {
            ItemStack potentialBackpackStack = player.getItemInHand(hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
            ItemStack thisStack = player.getItemInHand(hand);

            InteractionResult upgradeResult = InteractionResult.PASS;

            if(potentialBackpackStack.getItem() instanceof BackpackItem) {
                upgradeResult = StorageUpgrade.applyUpgradeToItem(thisStack, potentialBackpackStack, player, level);
            } else {
                ItemStack curiosBackpack = CompactStorage.getEquippedBackpackStack(player);

                if(!curiosBackpack.isEmpty()) {
                    upgradeResult = StorageUpgrade.applyUpgradeToItem(thisStack, curiosBackpack, player, level);
                }
            }

            if(upgradeResult != InteractionResult.PASS) {
                return upgradeResult;
            }
        }
        return super.use(level, player, hand);
    }
}
