package com.witchica.compactstorage.common.inventory.slot;

import com.witchica.compactstorage.common.item.BackpackItem;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class BackpackHoldSlot extends Slot {
    private final boolean isBackpack;

    public BackpackHoldSlot(Container container, int slot, int x, int y, boolean holdBackpack) {
        super(container, slot, x, y);

        if(holdBackpack) {
            if(hasItem() && getItem().getItem() instanceof BackpackItem) {
                this.isBackpack = true;
            } else {
                this.isBackpack = false;
            }
        } else {
            this.isBackpack = false;
        }
    }

    @Override
    public boolean mayPickup(Player player) {
        if(isBackpack) {
            return false;
        }

        return super.mayPickup(player);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if(isBackpack) {
            return false;
        }

        return super.mayPlace(stack);
    }
}
