package com.witchica.compactstorage.integration;

import com.witchica.compactstorage.item.BackpackItem;
import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.TrinketsApi;
import net.blay09.mods.balm.Balm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class TrinketsUpdatedModSupport extends BaseBalmIntegration {
    @Override
    public ItemStack getEquippedBackpackStack(Player player) {
        if(Balm.platform().isModLoaded("trinkets_updated")) {
            List<TrinketSlotAccess> equipped = TrinketsApi.getAttachment(player).equipped(itemstack -> itemstack.getItem() instanceof BackpackItem, true);

            for(TrinketSlotAccess slot : equipped) {
                return slot.get();
            }
        }

        return super.getEquippedBackpackStack(player);
    }
}
