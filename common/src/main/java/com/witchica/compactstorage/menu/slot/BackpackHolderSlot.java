package com.witchica.compactstorage.menu.slot;

import com.witchica.compactstorage.item.BackpackItem;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class BackpackHolderSlot extends Slot {
    private final boolean shouldFreezeBackpacks;

    public BackpackHolderSlot(Container container, int slot, int x, int y, boolean shouldFreezeBackpacks) {
        super(container, slot, x, y);
        this.shouldFreezeBackpacks = shouldFreezeBackpacks;
    }

    @Override
    public boolean mayPickup(Player player) {
        boolean isBackpack = getItem().getItem() instanceof BackpackItem;
        if(isBackpack && shouldFreezeBackpacks) {
            return false;
        }

        return super.mayPickup(player);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        boolean isBackpack = stack.getItem() instanceof BackpackItem;
        if(isBackpack && shouldFreezeBackpacks) {
            return false;
        }

        return super.mayPlace(stack);
    }
}
