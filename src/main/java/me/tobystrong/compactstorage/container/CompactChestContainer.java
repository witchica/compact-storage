package me.tobystrong.compactstorage.container;

import me.tobystrong.compactstorage.CompactStorage;
import me.tobystrong.compactstorage.block.tile.CompactChestTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class CompactChestContainer extends Container {
    public static CompactChestContainer createContainerClientSide(int windowID, PlayerInventory playerInventory, net.minecraft.network.PacketBuffer extraData) {
        CompactChestTileEntity tile = (CompactChestTileEntity) playerInventory.player.world.getTileEntity(extraData.readBlockPos());

        return new CompactChestContainer(windowID, playerInventory, tile.width, tile.height, tile, tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(NullPointerException::new));
    }

    private PlayerInventory playerInventory;
    public IItemHandler chestInventory;
    private CompactChestTileEntity chestTile;
    public int chestWidth;
    public int chestHeight;

    public CompactChestContainer(int windowID, PlayerInventory playerInventory, int chestWidth, int chestHeight, CompactChestTileEntity tile, IItemHandler inventory) {
        super(CompactStorage.COMPACT_CHEST_CONTAINER_TYPE, windowID);
        this.playerInventory = playerInventory;
        this.chestWidth = chestWidth;
        this.chestHeight = chestHeight;
        this.chestInventory = inventory;
        this.chestTile = tile;

        for(int y = 0; y < chestHeight; y++) {
            for(int x = 0; x < chestWidth; x++) {
                int index = x + y * chestWidth;
                addSlot(new SlotItemHandler(inventory, index, 8 + (x * 18), 17 + (y * 18)));
            }
        }

        int playerInvX = (int)(8 + ((chestWidth > 9 ? ((chestWidth - 9) * 18) / 2f : 0)));

        for(int y = 0; y < 3; y++) {
            for(int x = 0; x < 9; x++) {
                int index = x + y * chestWidth;
                addSlot(new Slot(playerInventory, 9 + index, playerInvX + (x * 18), 17 + (y * 18) + (chestHeight * 18) + 13));
            }
        }

        for(int x = 0; x < 9; x++) {
            addSlot(new Slot(playerInventory, x, playerInvX + (x * 18), 17 + (3 * 18) + (chestHeight * 18) + 17));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return chestTile.canPlayerAccess(playerIn);
    }
}
