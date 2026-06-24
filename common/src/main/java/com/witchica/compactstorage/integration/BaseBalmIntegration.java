package com.witchica.compactstorage.integration;

import com.witchica.compactstorage.item.BackpackItem;
import net.blay09.mods.balm.Balm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BaseBalmIntegration implements CompactStorageTrinketsSupport {
    @Override
    public ItemStack getEquippedBackpackStack(Player player) {
        ItemStack curiosStack = Balm.modSupport().trinkets().findEquipped(player, itemStack -> itemStack.getItem() instanceof BackpackItem);

        if(!curiosStack.isEmpty()) {
            return curiosStack;
        }

        return CompactStorageTrinketsSupport.getVanillaBackpackStack(player);
    }
}
