package com.witchica.compactstorage.menu;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.api.StorageTypeProvider;
import com.witchica.compactstorage.api.inventory.ArrangementPreservingContainer;
import com.witchica.compactstorage.api.inventory.ResizableContainer;
import com.witchica.compactstorage.api.inventory.SortPreferenceContainer;
import com.witchica.compactstorage.api.inventory.VoidSlotProvider;
import com.witchica.compactstorage.data.CompactStorageOpeningSource;
import com.witchica.compactstorage.data.StorageType;
import com.witchica.compactstorage.inventory.BackpackInventory;
import com.witchica.compactstorage.inventory.ScrollingContainerView;
import com.witchica.compactstorage.menu.slot.BackpackHolderSlot;
import com.witchica.compactstorage.menu.slot.VoidSlot;
import net.blay09.mods.balm.world.inventory.QuickMove;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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

        // Balm's QuickMove special-cases a target range literally named "player": it tries the
        // hotbar sub-range alone first, and only falls back to the inventory sub-range if NOTHING
        // moved in the hotbar at all. An empty hotbar slot alone counts as "moved", so an item
        // never gets the chance to top off an existing stack in the main inventory first - it just
        // goes straight to an empty hotbar slot. Vanilla's real behavior is one merge-then-fill pass
        // across the whole combined range with no hotbar priority. Registering the container->player
        // target under any other name sidesteps that special case entirely (the player->container
        // reverse route is unaffected - the special case only triggers on the TARGET name).
        this.quickMove = QuickMove.create(this::moveItemStackTo)
                .slotRange(QuickMove.CONTAINER, 0, containerSlotCount)
                .slotRange(QuickMove.PLAYER, containerSlotCount, containerSlotCount + 36)
                .slotRange("playerCombined", containerSlotCount, containerSlotCount + 36)
                .disableDefaultRoutes()
                .route(QuickMove.CONTAINER, "playerCombined")
                .route(QuickMove.PLAYER, QuickMove.CONTAINER).build();
    }

    public StorageType getStorageType() {
        return storageType;
    }

    // broadcastChanges() runs every server tick and diffs every visible Slot against what it last
    // told the client - since the viewport Slots all share this one storageView, moving its
    // scroll offset alone (no real data changed) still looks like every visible slot changed, and
    // gets resent in full. For BlockEntity-backed storages the client already has the storage's
    // complete, accurate contents via block-entity NBT sync, entirely independent of this Slot
    // path - so there's nothing to actually send. setRemoteSlot tells broadcastChanges "the client
    // already has this value" without sending anything, which makes scroll updates free (see
    // setItem below for the client-side half of this).
    public void setScroll(int x, int y) {
        storageView.setScroll(x, y);

        if(container instanceof BlockEntity) {
            for(int i = 0; i < storageView.getContainerSize(); i++) {
                setRemoteSlot(i, this.slots.get(i).getItem());
            }
        }
    }

    // Filtering re-flows which real slots the viewport represents exactly like scrolling does
    // (ScrollingContainerView.realIndex packs matching slots into the viewport while filtering) -
    // same reasoning as setScroll above applies here too.
    public void setFilter(String filter) {
        storageView.setFilter(filter);

        if(container instanceof BlockEntity) {
            for(int i = 0; i < storageView.getContainerSize(); i++) {
                setRemoteSlot(i, this.slots.get(i).getItem());
            }
        }
    }

    public void setPreservesArrangement(boolean preservesArrangement) {
        if(container instanceof ArrangementPreservingContainer arrangementPreservingContainer) {
            arrangementPreservingContainer.setPreservesArrangement(preservesArrangement);
        }
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

    /**
     * Applies a server-pushed slot correction (from the client's normal AbstractContainerMenu
     * networking). For storage viewport slots backed by a BlockEntity, the client's real container
     * is already accurate via block-entity NBT sync, entirely independent of this per-slot path -
     * broadcastChanges() still fires every tick and pushes "corrections" for the whole viewport
     * whenever the server's own scroll offset changes, purely because its view shifted, not
     * because any real data did. If the player scrolls again before one of those arrives, applying
     * it through the CLIENT's now-different offset writes that item into the wrong real slot -
     * duplicating/ghosting it. Since the client doesn't need this path for correctness here, feed
     * the correction back its own already-correct value instead of dropping it outright, so the
     * state-id reconciliation above still runs normally and the server never has a reason to
     * suspect desync.
     */
    @Override
    public void setItem(int slotId, int stateId, ItemStack stack) {
        if(slotId >= 0 && slotId < storageView.getContainerSize() && container instanceof BlockEntity) {
            super.setItem(slotId, stateId, this.slots.get(slotId).getItem());
            return;
        }

        super.setItem(slotId, stateId, stack);
    }

    /**
     * Double-click ("collect all") is handled by vanilla's private doClick, which only scans
     * this.slots - our fixed viewport Slot list - so it can never reach real storage slots
     * currently scrolled out of view. Let vanilla run its normal collection first (covers the
     * visible viewport and the whole player inventory correctly), then top off whatever it
     * couldn't reach from the rest of the real container.
     */
    @Override
    public void clicked(int slotIndex, int buttonNum, ContainerInput containerInput, Player player) {
        super.clicked(slotIndex, buttonNum, containerInput, player);

        if(containerInput == ContainerInput.PICKUP_ALL) {
            collectRemainingFromStorage();
        }
    }

    private void collectRemainingFromStorage() {
        ItemStack carried = getCarried();
        if(carried.isEmpty() || carried.getCount() >= carried.getMaxStackSize()) {
            return;
        }

        int size = container.getContainerSize();
        boolean changed = false;

        for(int i = 0; i < size && carried.getCount() < carried.getMaxStackSize(); i++) {
            ItemStack stack = container.getItem(i);
            if(stack.isEmpty() || !ItemStack.isSameItemSameComponents(stack, carried)) {
                continue;
            }

            int moved = Math.min(carried.getMaxStackSize() - carried.getCount(), stack.getCount());
            if(moved <= 0) {
                continue;
            }

            stack.shrink(moved);
            carried.grow(moved);
            changed = true;

            if(stack.isEmpty()) {
                container.setItem(i, ItemStack.EMPTY);
            }
        }

        if(changed) {
            container.setChanged();
            storageView.invalidateFilterCache();
        }
    }

    /**
     * Shift+double-click is vanilla's own "move every matching stack" gesture, resolved entirely
     * client-side (AbstractContainerScreen#mouseReleased) by looping this.menu.slots - our fixed
     * viewport list - so it has the same off-screen blind spot as plain double-click. This sweeps
     * the real container's full range for the same item type into the player's main inventory and
     * hotbar, the same destination vanilla's own sweep uses.
     */
    public void collectMatchingIntoPlayer(ItemStack itemType) {
        int size = container.getContainerSize();
        boolean changed = false;

        for(int i = 0; i < size; i++) {
            ItemStack stack = container.getItem(i);
            if(stack.isEmpty() || !ItemStack.isSameItemSameComponents(stack, itemType)) {
                continue;
            }

            if(insertIntoPlayer(stack)) {
                changed = true;
                if(stack.isEmpty()) {
                    container.setItem(i, ItemStack.EMPTY);
                }
            }
        }

        if(changed) {
            container.setChanged();
            playerInventory.setChanged();
            storageView.invalidateFilterCache();
        }
    }

    /**
     * Merges/inserts as much of stack as fits into the player's main inventory and hotbar,
     * mutating stack's count down as progress is made. Returns whether anything moved.
     * Inventory.getContainerSize() (43) also covers armor/offhand/body/saddle equipment slots
     * (indices 36-42) - Inventory.INVENTORY_SIZE (36) is just the main+hotbar range this should
     * touch.
     */
    private boolean insertIntoPlayer(ItemStack stack) {
        int originalCount = stack.getCount();

        for(int i = 0; i < Inventory.INVENTORY_SIZE && !stack.isEmpty(); i++) {
            ItemStack existing = playerInventory.getItem(i);
            if(!existing.isEmpty() && ItemStack.isSameItemSameComponents(existing, stack)) {
                int room = playerInventory.getMaxStackSize(existing) - existing.getCount();
                if(room > 0) {
                    int moved = Math.min(room, stack.getCount());
                    existing.grow(moved);
                    stack.shrink(moved);
                }
            }
        }

        for(int i = 0; i < Inventory.INVENTORY_SIZE && !stack.isEmpty(); i++) {
            if(playerInventory.getItem(i).isEmpty()) {
                int moved = Math.min(playerInventory.getMaxStackSize(stack), stack.getCount());
                ItemStack placed = stack.copy();
                placed.setCount(moved);
                playerInventory.setItem(i, placed);
                stack.shrink(moved);
            }
        }

        return stack.getCount() != originalCount;
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
            storageView.invalidateFilterCache();
        }

        return moved;
    }

    public enum SortKey { ITEM_ID, CATEGORY, NAME, SOURCE, COUNT }
    public enum SortArrangement { CLASSIC, ROWS, COLUMNS }

    /** The last sort key/arrangement this storage was actually sorted by, or (ITEM_ID, CLASSIC) if never. */
    public int getSortPreference() {
        if(container instanceof SortPreferenceContainer sortPreferenceContainer) {
            return sortPreferenceContainer.getSortPreference();
        }

        return 0;
    }

    public void sort(SortKey key, SortArrangement arrangement) {
        List<ItemStack> merged = collectMergedStacks();
        merged.sort(comparatorFor(key));

        applySortedStacks(merged, arrangement);

        if(container instanceof SortPreferenceContainer sortPreferenceContainer) {
            sortPreferenceContainer.setSortPreference((key.ordinal() * SortArrangement.values().length) + arrangement.ordinal());
        }
    }

    /**
     * Finds the smallest subdivision factor (1, 2, 4, ...) that lets every distinct item get a
     * fully dedicated line (or several, for an item too big for one), never sharing a line with
     * another item. Subdivision 1 is the plain "one row/column per item" case. Subdivision N
     * splits the container into N vertical (ROWS) or horizontal (COLUMNS) bands, each
     * independently behaving as its own complete set of lines but only 1/N as wide/tall - trading
     * line width for more of them, so Rows/Columns stays visually distinct from Sequential on a
     * full, high-diversity storage instead of collapsing into one continuous packed fill. At
     * subdivision equal to the arrangement's own span (eg. inventoryWidth for ROWS), every item
     * gets a minimum 1-wide dedicated line, which by definition always fits (every piece already
     * came from this same container) - so this always terminates with a fit.
     */
    private int findSubdivision(List<ItemStack> mergedStacks, SortArrangement arrangement) {
        int maxSpan = arrangement == SortArrangement.ROWS ? inventoryWidth : inventoryHeight;
        int otherSpan = arrangement == SortArrangement.ROWS ? inventoryHeight : inventoryWidth;

        for(int subdivision = 1; subdivision <= maxSpan; subdivision *= 2) {
            int bandSpan = maxSpan / subdivision;
            if(bandSpan < 1) {
                break;
            }

            int linesNeeded = 0;
            for(ItemStack merged : mergedStacks) {
                int max = container.getMaxStackSize(merged);
                int pieceCount = (merged.getCount() + max - 1) / max;
                linesNeeded += (pieceCount + bandSpan - 1) / bandSpan;
            }

            if(linesNeeded <= subdivision * otherSpan) {
                return subdivision;
            }
        }

        return maxSpan;
    }

    private List<ItemStack> collectMergedStacks() {
        List<ItemStack> merged = new ArrayList<>();
        int size = container.getContainerSize();

        for(int i = 0; i < size; i++) {
            ItemStack stack = container.getItem(i);
            if(stack.isEmpty()) {
                continue;
            }

            ItemStack match = null;
            for(ItemStack candidate : merged) {
                if(ItemStack.isSameItemSameComponents(candidate, stack)) {
                    match = candidate;
                    break;
                }
            }

            if(match != null) {
                match.grow(stack.getCount());
            } else {
                merged.add(stack.copy());
            }
        }

        return merged;
    }

    private Comparator<ItemStack> comparatorFor(SortKey key) {
        return switch(key) {
            case ITEM_ID -> Comparator.comparing(stack -> BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
            case NAME -> Comparator.comparing(stack -> stack.getHoverName().getString());
            case SOURCE -> Comparator.comparing((ItemStack stack) -> BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace())
                    .thenComparing(stack -> BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
            // Registry id order roughly tracks vanilla's own thematic item groupings, without
            // depending on creative-tab contents being populated (unreliable server-side).
            case CATEGORY -> Comparator.comparingInt(stack -> BuiltInRegistries.ITEM.getId(stack.getItem()));
            case COUNT -> Comparator.<ItemStack>comparingInt(ItemStack::getCount).reversed();
        };
    }

    private List<ItemStack> splitStack(ItemStack merged) {
        List<ItemStack> pieces = new ArrayList<>();
        int max = container.getMaxStackSize(merged);
        int remaining = merged.getCount();

        while(remaining > 0) {
            int amount = Math.min(max, remaining);
            pieces.add(merged.copyWithCount(amount));
            remaining -= amount;
        }

        return pieces;
    }

    private void applySortedStacks(List<ItemStack> mergedStacks, SortArrangement arrangement) {
        int size = container.getContainerSize();

        // Computed into a staging array first - the real container is only touched in the final
        // commit loop below, so an interruption during computation (a crash, a server restart mid-
        // sort) leaves the container's real contents completely untouched instead of half-rebuilt.
        ItemStack[] result = new ItemStack[size];
        Arrays.fill(result, ItemStack.EMPTY);
        boolean[] used = new boolean[size];

        int fallbackFrom = 0;

        if(arrangement == SortArrangement.CLASSIC) {
            int index = 0;
            for(ItemStack merged : mergedStacks) {
                for(ItemStack piece : splitStack(merged)) {
                    int target = index < size && !used[index] ? index : -1;

                    if(target < 0) {
                        // Shouldn't happen (every piece originated from this same container, so
                        // there's always a free slot somewhere) - never drop the item.
                        while(fallbackFrom < size) {
                            if(!used[fallbackFrom]) {
                                target = fallbackFrom;
                                break;
                            }
                            fallbackFrom++;
                        }
                    }

                    if(target < 0) {
                        break;
                    }

                    result[target] = piece;
                    used[target] = true;
                    index++;
                }
            }
        } else {
            // Split the container into `subdivision` bands along its own axis (vertical strips
            // for ROWS, horizontal strips for COLUMNS), each acting as an independent, complete
            // set of lines but only 1/subdivision as wide/tall. A band boundary is just another
            // line boundary from the placement loop's point of view, so one continuous "line"
            // counter walks through band 0's lines, then band 1's, etc; only the coordinate math
            // differs between ROWS and COLUMNS (COLUMNS is the same thing transposed).
            boolean rows = arrangement == SortArrangement.ROWS;
            int subdivision = findSubdivision(mergedStacks, arrangement);
            int maxSpan = rows ? inventoryWidth : inventoryHeight;
            int otherSpan = rows ? inventoryHeight : inventoryWidth;
            int bandSpan = Math.max(1, maxSpan / subdivision);

            int line = 0;
            int pos = 0;

            for(ItemStack merged : mergedStacks) {
                if(pos != 0) {
                    pos = 0;
                    line++;
                }

                for(ItemStack piece : splitStack(merged)) {
                    int band = line / otherSpan;
                    int lineInBand = line % otherSpan;
                    int target = band < subdivision
                            ? (rows
                                    ? (lineInBand * inventoryWidth) + (band * bandSpan) + pos
                                    : ((band * bandSpan) + pos) * inventoryWidth + lineInBand)
                            : -1;

                    if(target < 0 || target >= size || used[target]) {
                        // Shouldn't happen - findSubdivision guarantees a fit - never drop the item.
                        target = -1;
                        while(fallbackFrom < size) {
                            if(!used[fallbackFrom]) {
                                target = fallbackFrom;
                                break;
                            }
                            fallbackFrom++;
                        }
                    }

                    if(target < 0) {
                        break;
                    }

                    result[target] = piece;
                    used[target] = true;

                    pos++;
                    if(pos >= bandSpan) {
                        pos = 0;
                        line++;
                    }
                }
            }
        }

        for(int i = 0; i < size; i++) {
            container.setItem(i, result[i]);
        }

        container.setChanged();
        storageView.invalidateFilterCache();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
