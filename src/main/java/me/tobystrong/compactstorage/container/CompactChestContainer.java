package me.tobystrong.compactstorage.container;

import me.tobystrong.compactstorage.CompactStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

public class CompactChestContainer extends Container {
    public static CompactChestContainer createContainerClientSide(int windowID, PlayerInventory playerInventory, net.minecraft.network.PacketBuffer extraData) {
        int width = extraData.getInt(0);
        int height = extraData.getInt(1);

        return new CompactChestContainer(windowID, playerInventory, width, height);
    }

    private PlayerInventory playerInventory;
    private int chestWidth;
    private int chestHeight;

    public CompactChestContainer(int windowID, PlayerInventory playerInventory, int chestWidth, int chestHeight) {
        super(CompactStorage.COMPACT_CHEST_CONTAINER_TYPE, windowID);
        this.playerInventory = playerInventory;
        this.chestWidth = chestWidth;
        this.chestHeight = chestHeight;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}
