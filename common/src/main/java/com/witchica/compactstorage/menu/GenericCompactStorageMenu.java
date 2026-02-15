package com.witchica.compactstorage.menu;

import com.witchica.compactstorage.block.CompactChestBlock;
import com.witchica.compactstorage.block.CompactStorageBlocks;
import com.witchica.compactstorage.util.StorageTypeProvider;
import net.blay09.mods.balm.world.inventory.QuickMove;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import util.CompactStorageOpeningSource;
import util.StorageTypes;

import java.util.Optional;

public class GenericCompactStorageMenu extends AbstractContainerMenu {
    private final Inventory playerInventory;
    private final StorageTypes storageType;
    private Container container;
    private final QuickMove.Routing quickMove;

    public GenericCompactStorageMenu(int containerId, Inventory playerInventory, CompactStorageMenuData data) {
        super(CompactStorageMenuTypes.COMPACT_STORAGE_MENU.value(), containerId);
        this.playerInventory = playerInventory;

        switch(data.source()) {
            case BLOCK: {
                BlockEntity entity = playerInventory.player.level().getBlockEntity(data.pos().get());

                this.storageType = ((StorageTypeProvider) entity.getBlockState().getBlock()).getStorageType();
                this.container = (Container) entity;
                break;
            } default: {
                this.storageType = StorageTypes.RED;
            }
        }

        setupSlots();
        int containerSlotCount = 9 * 3;

        this.quickMove = QuickMove.create(this, this::moveItemStackTo).slotRange(QuickMove.CONTAINER, 0, containerSlotCount)
                .slotRange(QuickMove.PLAYER, containerSlotCount, containerSlotCount + 36)
                .slotRange("inventory", containerSlotCount, containerSlotCount + 27)
                .slotRange("hotbar", containerSlotCount + 27, containerSlotCount + 36)
                .disableDefaultRoutes()
                .route(QuickMove.CONTAINER, QuickMove.PLAYER)
                .route(QuickMove.PLAYER, QuickMove.CONTAINER).build();
    }

    public StorageTypes getStorageType() {
        return storageType;
    }

    public void setupSlots() {
        int invSizeX = 9;
        int invSizeY = 3;

        int chestSizeX = (7+7+(invSizeX * 18));
        int offsetX = (chestSizeX / 2) - ((14+(9*18)) / 2);
        int playerInvStartY = 17 + (invSizeY * 18) + 7 + 4 + 18;

        for(int y = 0; y < 3; y++) {
            for(int x = 0; x < 9; x++) {
                addSlot(new Slot(container, (y * 9) + x, (x * 18) + 8, (y * 18) + 18));
            }
        }
        for(int y = 0; y < 3; y++) {
            for(int x = 0; x < 9; x++) {
                addSlot(new Slot(playerInventory, 9+(y * 9) + x, offsetX + 8 + (x * 18), playerInvStartY + (y * 18)));
            }
        }
        for(int x = 0; x < 9; x++) {
            addSlot(new Slot(playerInventory, x, offsetX + 8 + (x * 18), playerInvStartY + (3 * 18) + 4));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return quickMove.transfer(this, player, i);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
