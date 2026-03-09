package com.witchica.compactstorage.inventory;

import com.witchica.compactstorage.block.base.BaseItemDrumBlock;
import com.witchica.compactstorage.block.entity.base.BaseItemDrumBlockEntity;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class DrumInventory extends SimpleContainer {
    private BaseItemDrumBlockEntity baseItemDrumBlockEntity;

    public DrumInventory(int size, BaseItemDrumBlockEntity baseItemDrumBlockEntity) {
        super(size);
        this.baseItemDrumBlockEntity = baseItemDrumBlockEntity;
    }

    public ItemStack getItemType() {
        return getItem(0);
    }

    @Override
    public int getMaxStackSize() {
        if(getItemType().isEmpty()) {
            return 64;
        }

        return getItemType().getMaxStackSize();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return getItemType().isEmpty() || ItemStack.isSameItem(stack, getItemType());
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        for(int i = getContainerSize()-1; i >=0; i--) {
            ItemStack stack = getItem(i);

            if(stack != null && !stack.isEmpty()) {
                slot = i;
                break;
            }
        }

        ItemStack stack =  super.removeItemNoUpdate(slot);
        setChanged();
        return stack;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        for(int i = getContainerSize()-1; i >=0; i--) {
            ItemStack stack = getItem(i);

            if(stack != null && !stack.isEmpty()) {
                slot = i;
                break;
            }
        }

        ItemStack stack = super.removeItem(slot, amount);
        setChanged();
        return stack;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        baseItemDrumBlockEntity.inventoryChanged();
    }
}
