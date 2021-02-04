package me.tobystrong.compactstorage.container;

import me.tobystrong.compactstorage.CompactStorage;
import me.tobystrong.compactstorage.block.tile.CompactBarrelTileEntity;
import me.tobystrong.compactstorage.block.tile.CompactChestTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class CompactBarrelContainer extends CompactStorageBaseContainer {
    public static CompactBarrelContainer createContainerClientSide(int windowID, PlayerInventory playerInventory, net.minecraft.network.PacketBuffer extraData) {
        //get the data from the block pos
        CompactBarrelTileEntity tile = (CompactBarrelTileEntity) playerInventory.player.world.getTileEntity(extraData.readBlockPos());

        return new CompactBarrelContainer(windowID, playerInventory, tile.width, tile.height, tile, tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(NullPointerException::new));
    }

    private CompactBarrelTileEntity barrelTile;

    public CompactBarrelContainer(int windowID, PlayerInventory playerInventory, int chestWidth, int chestHeight, CompactBarrelTileEntity tile, IItemHandler inventory) {
        super(CompactStorage.COMPACT_CHEST_CONTAINER_TYPE, windowID, playerInventory, chestWidth, chestHeight, inventory);
        this.barrelTile = tile;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return barrelTile.canPlayerAccess(playerIn);
    }
}
