package com.witchica.compactstorage.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Locale;

/**
 * A fixed-size Container view over a larger row-major (y * realWidth + x) backing Container,
 * used so the menu only ever has to create Slot objects for the visible viewport - Slot.x/y and
 * the container it points at are both final in vanilla, so the viewport can't be resized or
 * re-pointed after construction, only scrolled (or filtered) by moving this view's mapping.
 *
 * With no filter set, local cells map onto a scrolled rectangular window of the real grid. With a
 * filter set, the real grid's matching slots (in real index order) are instead packed row-major
 * into the viewport as a flat list - horizontal scroll doesn't apply to a packed list, so it's
 * disabled while filtering.
 */
public class ScrollingContainerView implements Container {
    private final Container real;
    private final int realWidth;
    private final int realHeight;
    private final int visibleWidth;
    private final int visibleHeight;

    private int scrollX;
    private int scrollY;

    private String filter = "";
    private int[] matchingIndices;

    public ScrollingContainerView(Container real, int realWidth, int realHeight, int visibleWidth, int visibleHeight) {
        this.real = real;
        this.realWidth = realWidth;
        this.realHeight = realHeight;
        this.visibleWidth = Math.min(visibleWidth, realWidth);
        this.visibleHeight = Math.min(visibleHeight, realHeight);
    }

    public int getRealWidth() {
        return realWidth;
    }

    public int getRealHeight() {
        return realHeight;
    }

    public int getMaxScrollX() {
        return isFiltering() ? 0 : Math.max(0, realWidth - visibleWidth);
    }

    public int getMaxScrollY() {
        if(isFiltering()) {
            int rows = (getMatchingIndices().length + visibleWidth - 1) / visibleWidth;
            return Math.max(0, rows - visibleHeight);
        }

        return Math.max(0, realHeight - visibleHeight);
    }

    public int getScrollX() {
        return scrollX;
    }

    public int getScrollY() {
        return scrollY;
    }

    public void setScroll(int x, int y) {
        this.scrollX = Math.clamp(x, 0, getMaxScrollX());
        this.scrollY = Math.clamp(y, 0, getMaxScrollY());
    }

    public boolean needsHorizontalScroll() {
        return getMaxScrollX() > 0;
    }

    public boolean needsVerticalScroll() {
        return getMaxScrollY() > 0;
    }

    public int getVisibleWidth() {
        return visibleWidth;
    }

    public int getVisibleHeight() {
        return visibleHeight;
    }

    public boolean isFiltering() {
        return !filter.isEmpty();
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        String normalized = filter == null ? "" : filter.toLowerCase(Locale.ROOT);
        if(!normalized.equals(this.filter)) {
            this.filter = normalized;
            this.matchingIndices = null;
            this.scrollX = 0;
            this.scrollY = 0;
        }
    }

    public int getMatchCount() {
        return isFiltering() ? getMatchingIndices().length : realWidth * realHeight;
    }

    private int[] getMatchingIndices() {
        if(matchingIndices == null) {
            int[] found = new int[real.getContainerSize()];
            int count = 0;

            for(int i = 0; i < real.getContainerSize(); i++) {
                ItemStack stack = real.getItem(i);
                if(!stack.isEmpty() && stack.getHoverName().getString().toLowerCase(Locale.ROOT).contains(filter)) {
                    found[count++] = i;
                }
            }

            matchingIndices = new int[count];
            System.arraycopy(found, 0, matchingIndices, 0, count);
        }

        return matchingIndices;
    }

    /** Call after any change to the real container's contents made without going through this view's setItem. */
    public void invalidateFilterCache() {
        matchingIndices = null;
    }

    private int realIndex(int localIndex) {
        if(isFiltering()) {
            int[] matches = getMatchingIndices();
            int flatIndex = (scrollY * visibleWidth) + localIndex;
            return flatIndex < matches.length ? matches[flatIndex] : -1;
        }

        int col = (localIndex % visibleWidth) + scrollX;
        int row = (localIndex / visibleWidth) + scrollY;
        return (row * realWidth) + col;
    }

    @Override
    public int getContainerSize() {
        return visibleWidth * visibleHeight;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < getContainerSize(); i++) {
            if (!getItem(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int i) {
        int index = realIndex(i);
        return index < 0 ? ItemStack.EMPTY : real.getItem(index);
    }

    @Override
    public ItemStack removeItem(int i, int amount) {
        int index = realIndex(i);
        return index < 0 ? ItemStack.EMPTY : real.removeItem(index, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        int index = realIndex(i);
        return index < 0 ? ItemStack.EMPTY : real.removeItemNoUpdate(index);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        int index = realIndex(i);
        if(index >= 0) {
            real.setItem(index, itemStack);
        }
    }

    @Override
    public void setChanged() {
        real.setChanged();
        matchingIndices = null;
    }

    @Override
    public boolean stillValid(Player player) {
        return real.stillValid(player);
    }

    @Override
    public boolean canPlaceItem(int i, ItemStack itemStack) {
        int index = realIndex(i);
        return index >= 0 && real.canPlaceItem(index, itemStack);
    }

    @Override
    public int getMaxStackSize() {
        return real.getMaxStackSize();
    }

    @Override
    public int getMaxStackSize(ItemStack itemStack) {
        return real.getMaxStackSize(itemStack);
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < getContainerSize(); i++) {
            setItem(i, ItemStack.EMPTY);
        }
    }
}
