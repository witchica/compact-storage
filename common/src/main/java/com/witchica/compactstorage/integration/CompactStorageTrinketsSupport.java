package com.witchica.compactstorage.integration;

import com.witchica.compactstorage.item.BackpackItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface CompactStorageTrinketsSupport {
    default ItemStack getEquippedBackpackStack(Player player) {
        return getVanillaBackpackStack(player);
    }

    static ItemStack getVanillaBackpackStack(Player player) {
        ItemStack chestStack = player.getItemBySlot(EquipmentSlot.CHEST);

        if(chestStack.getItem() instanceof BackpackItem) {
            return chestStack;
        }

        return ItemStack.EMPTY;
    }
}
