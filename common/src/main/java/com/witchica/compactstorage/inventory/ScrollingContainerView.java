package com.witchica.compactstorage.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * A fixed-size Container view over a larger row-major (y * realWidth + x) backing Container, used
 * so the menu only ever has to create Slot objects for the visible viewport - Slot.x/y and the
 * container it points at are both final in vanilla, so the viewport can't be resized or
 * re-pointed after construction, only scrolled by moving this view's mapping.
 */
public class ScrollingContainerView implements Container {
    private final Container real;
    private final int realWidth;
    private final int realHeight;
    private final int visibleWidth;
    private final int visibleHeight;

    private int scrollX;
    private int scrollY;

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
        return Math.max(0, realWidth - visibleWidth);
    }

    public int getMaxScrollY() {
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

    private int realIndex(int localIndex) {
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
        return real.getItem(realIndex(i));
    }

    @Override
    public ItemStack removeItem(int i, int amount) {
        return real.removeItem(realIndex(i), amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return real.removeItemNoUpdate(realIndex(i));
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        real.setItem(realIndex(i), itemStack);
    }

    @Override
    public void setChanged() {
        real.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return real.stillValid(player);
    }

    @Override
    public boolean canPlaceItem(int i, ItemStack itemStack) {
        return real.canPlaceItem(realIndex(i), itemStack);
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
