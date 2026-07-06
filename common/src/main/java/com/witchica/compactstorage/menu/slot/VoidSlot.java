package com.witchica.compactstorage.menu.slot;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class VoidSlot extends Slot {
    private final Player player;

    public VoidSlot(Container container, int slot, int x, int y, Player player) {
        super(container, slot, x, y);
        this.player = player;
    }

    @Override
    public void setByPlayer(ItemStack itemStack, ItemStack previous) {
        super.setByPlayer(itemStack, previous);
        player.playSound(SoundEvents.ENDERMAN_TELEPORT, 1f, 1f);
    }
}
