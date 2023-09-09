package com.tabithastrong.compactstorage.inventory;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class DrumItemStackHandler extends ItemStackHandler {
    private BlockEntity owner;
    public DrumItemStackHandler(BlockEntity owner) {
        super(64);
        this.owner = owner;
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return super.insertItem(getFirstStackableSlot(), stack, simulate);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return super.extractItem(getLastSlotWithStack(), amount, simulate);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        int firstSlot = getFirstSlotWithStack();
        if(!getStackInSlot(firstSlot).isEmpty())
        {
            return getStackInSlot(firstSlot).getItem() == stack.getItem();
        }

        return super.isItemValid(slot, stack);
    }

    public int getLastSlotWithStack() {
        for(int i = 1; i < stacks.size(); i++) {
            if(getStackInSlot(i).isEmpty()) {
                return i-1;
            }
        }

        return stacks.size() - 1;
    }

    public int getFirstSlotWithStack() {
        for(int i = 0; i < stacks.size(); i++) {
            if(!getStackInSlot(i).isEmpty()) {
                return i;
            }
        }

        return 0;
    }

    public int getLastSlotStackSize() {
        return getStackInSlot(getLastSlotWithStack()).getCount();
    }

    public int getFirstStackableSlot() {
        for(int i = 0; i < stacks.size(); i++) {
            if(getStackInSlot(i).getCount() < getStackInSlot(i).getMaxStackSize() || getStackInSlot(i).isEmpty()) {
                return i;
            }
        }

        return 0;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        owner.setChanged();
    }


    public ItemStack getDisplayStack() {
        return getStackInSlot(getFirstSlotWithStack()).copy();
    }

    public int getTotalItemCount() {
        int count = 0;

        for(int i = 0; i < stacks.size(); i++) {
            count += getStackInSlot(i).getCount();
        }

        return count;
    }

    public int getMaxStackSize() {
        return getStackInSlot(getFirstSlotWithStack()).getMaxStackSize();
    }
}
