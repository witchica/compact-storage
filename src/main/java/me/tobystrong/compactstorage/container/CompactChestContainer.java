package me.tobystrong.compactstorage.container;

import me.tobystrong.compactstorage.CompactStorage;
import me.tobystrong.compactstorage.block.tile.CompactChestTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.List;

public class CompactChestContainer extends CompactStorageBaseContainer {
    public static CompactChestContainer createContainerClientSide(int windowID, PlayerInventory playerInventory, net.minecraft.network.PacketBuffer extraData) {
        //get the data from the block pos
        CompactChestTileEntity tile = (CompactChestTileEntity) playerInventory.player.world.getTileEntity(extraData.readBlockPos());

        return new CompactChestContainer(windowID, playerInventory, tile.width, tile.height, tile, tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(NullPointerException::new));
    }

    private CompactChestTileEntity chestTile;

    public CompactChestContainer(int windowID, PlayerInventory playerInventory, int chestWidth, int chestHeight, CompactChestTileEntity tile, IItemHandler inventory) {
        super(CompactStorage.COMPACT_CHEST_CONTAINER_TYPE, windowID, playerInventory, chestWidth, chestHeight, inventory);
        this.chestTile = tile;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return chestTile.canPlayerAccess(playerIn);
    }
}
