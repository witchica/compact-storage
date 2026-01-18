package com.witchica.compactstorage.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class GenericCompactStorageMenu extends AbstractContainerMenu {
    private final Inventory playerInventory;
    private final Container container;

    public GenericCompactStorageMenu(@Nullable MenuType<?> menuType, int containerId, Inventory playerInventory, Container container) {
        super(menuType, containerId);
        this.playerInventory = playerInventory;
        this.container = container;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
