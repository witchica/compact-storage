package com.witchica.compactstorage.menu;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.api.inventory.ResizableContainer;
import com.witchica.compactstorage.api.StorageTypeProvider;
import com.witchica.compactstorage.api.inventory.VoidSlotProvider;
import com.witchica.compactstorage.data.CompactStorageOpeningSource;
import com.witchica.compactstorage.inventory.BackpackInventory;
import com.witchica.compactstorage.inventory.ScrollingContainerView;
import com.witchica.compactstorage.menu.slot.BackpackHolderSlot;
import com.witchica.compactstorage.menu.slot.VoidSlot;
import net.blay09.mods.balm.world.inventory.QuickMove;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import com.witchica.compactstorage.data.StorageType;

import java.util.Optional;

public class GenericCompactStorageMenu extends AbstractContainerMenu {
    // Slot.x/y and the Slot's target Container are both final in vanilla, so a storage bigger than
    // this can't just grow its on-screen grid - it scrolls instead, see ScrollingContainerView.
    public static final int MAX_VISIBLE_WIDTH = 21;
    public static final int MAX_VISIBLE_HEIGHT = 14;

    private final Inventory playerInventory;
    private final StorageType storageType;
    public final int inventoryWidth;
    public final int inventoryHeight;
    private final CompactStorageOpeningSource openSource;

    public Container container;
    public ScrollingContainerView storageView;
    private final QuickMove.Routing quickMove;

    public boolean hasVoidSlot;

    public GenericCompactStorageMenu(int containerId, Inventory playerInventory, CompactStorageMenuData data) {
        super(ModMenuTypes.COMPACT_STORAGE_MENU.value(), containerId);
        this.playerInventory = playerInventory;
        this.openSource = data.source();

        switch(data.source()) {
            case BLOCK: {
                BlockEntity entity = playerInventory.player.level().getBlockEntity(data.pos().get());
                this.storageType = ((StorageTypeProvider) entity.getBlockState().getBlock()).getStorageType();
                this.container = (Container) entity;

                break;
            } case BACKPACK_IN_HAND: {
                InteractionHand hand = data.hotbarSlot().orElse(0) == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
                ItemStack backpack = playerInventory.player.getItemInHand(hand);
                this.storageType = ((StorageTypeProvider) backpack.getItem()).getStorageType();
                this.container = new BackpackInventory(playerInventory.player, data.source(), backpack, Optional.of(hand));
                break;
            }
            case BACKPACK_HOT_KEY: {
                ItemStack backpack = CompactStorage.getEquippedBackpackStack(this.playerInventory.player);
                this.storageType = ((StorageTypeProvider) backpack.getItem()).getStorageType();
                this.container = new BackpackInventory(playerInventory.player, data.source(), backpack, Optional.empty());
                break;
            }
            default: {
                this.storageType = StorageType.RED;
            }
        }

        if(container instanceof VoidSlotProvider voidSlotProvider) {
            this.hasVoidSlot = voidSlotProvider.hasVoidSlot();
        }

        if(container instanceof ResizableContainer resizableContainer) {
            this.inventoryWidth = resizableContainer.getWidth();
            this.inventoryHeight = resizableContainer.getHeight();
        } else {
            this.inventoryWidth = 9;
            this.inventoryHeight = 3;
        }

        this.storageView = new ScrollingContainerView(container, inventoryWidth, inventoryHeight,
                Math.min(inventoryWidth, MAX_VISIBLE_WIDTH), Math.min(inventoryHeight, MAX_VISIBLE_HEIGHT));

        container.startOpen(playerInventory.player);

        setupSlots();

        int containerSlotCount = storageView.getContainerSize();

        this.quickMove = QuickMove.create(this::moveItemStackTo)
                .slotRange(QuickMove.CONTAINER, 0, containerSlotCount)
                .slotRange(QuickMove.PLAYER, containerSlotCount, containerSlotCount + 36)
                .slotRange("inventory", containerSlotCount, containerSlotCount + 27)
                .slotRange("hotbar", containerSlotCount + 27, containerSlotCount + 36)
                .disableDefaultRoutes()
                .route(QuickMove.CONTAINER, QuickMove.PLAYER)
                .route(QuickMove.PLAYER, QuickMove.CONTAINER).build();
    }

    public StorageType getStorageType() {
        return storageType;
    }

    public void setScroll(int x, int y) {
        storageView.setScroll(x, y);
    }

    public void setupSlots() {
        int visibleWidth = storageView.getVisibleWidth();
        int visibleHeight = storageView.getVisibleHeight();

        int chestSizeX = (7+7+(visibleWidth * 18));
        int offsetX = (chestSizeX / 2) - ((14+(9*18)) / 2);
        int playerInvStartY = 17 + (visibleHeight * 18) + 7 + 4 + 18;

        for(int y = 0; y < visibleHeight; y++) {
            for(int x = 0; x < visibleWidth; x++) {
                addSlot(new BackpackHolderSlot(storageView, (y * visibleWidth) + x, (x * 18) + 8, (y * 18) + 18, openSource==CompactStorageOpeningSource.BACKPACK_IN_HAND));
            }
        }
        for(int y = 0; y < 3; y++) {
            for(int x = 0; x < 9; x++) {
                addSlot(new BackpackHolderSlot(playerInventory, 9+(y * 9) + x, offsetX + 8 + (x * 18), playerInvStartY + (y * 18), openSource==CompactStorageOpeningSource.BACKPACK_IN_HAND));
            }
        }

        for(int x = 0; x < 9; x++) {
            addSlot(new BackpackHolderSlot(playerInventory, x, offsetX + 8 + (x * 18), playerInvStartY + (3 * 18) + 4, openSource==CompactStorageOpeningSource.BACKPACK_IN_HAND));
        }

        if(hasVoidSlot) {
            Slot slot = addSlot(new VoidSlot(new SimpleContainer(1) {
                @Override
                public void setItem(int slot, ItemStack itemStack) {
                    super.setItem(slot, itemStack);
                    this.getItems().set(0, ItemStack.EMPTY);
                    setChanged();
                }
            }, 0, chestSizeX+4 + 8, 8, playerInventory.player) {

            });
        }
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        container.stopOpen(playerInventory.player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        Slot slot = this.slots.get(i);
        if(slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        // Shift-clicking out of storage only ever targets a visible slot, so the viewport-based
        // QuickMove routing is fine as-is.
        if(i < storageView.getContainerSize()) {
            return quickMove.transfer(this, player, i);
        }

        // Shift-clicking into storage needs the real container's full range: QuickMove only knows
        // about the viewport Slots, so it would report "no room" once those fill up even with
        // space free off-screen.
        ItemStack slotStack = slot.getItem();
        ItemStack originalStack = slotStack.copy();

        if(!insertIntoContainer(slotStack)) {
            return ItemStack.EMPTY;
        }

        if(slotStack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        return originalStack;
    }

    /**
     * Merges/inserts as much of stack as fits into the real (not viewport-limited) container,
     * mutating stack's count down as progress is made. Returns whether anything moved.
     */
    private boolean insertIntoContainer(ItemStack stack) {
        int originalCount = stack.getCount();
        int size = container.getContainerSize();

        for(int i = 0; i < size && !stack.isEmpty(); i++) {
            ItemStack existing = container.getItem(i);
            if(!existing.isEmpty() && ItemStack.isSameItemSameComponents(existing, stack) && container.canPlaceItem(i, stack)) {
                int room = container.getMaxStackSize(existing) - existing.getCount();
                if(room > 0) {
                    int moved = Math.min(room, stack.getCount());
                    existing.grow(moved);
                    stack.shrink(moved);
                }
            }
        }

        for(int i = 0; i < size && !stack.isEmpty(); i++) {
            if(container.getItem(i).isEmpty() && container.canPlaceItem(i, stack)) {
                int moved = Math.min(container.getMaxStackSize(stack), stack.getCount());
                ItemStack placed = stack.copy();
                placed.setCount(moved);
                container.setItem(i, placed);
                stack.shrink(moved);
            }
        }

        boolean moved = stack.getCount() != originalCount;
        if(moved) {
            container.setChanged();
        }

        return moved;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
