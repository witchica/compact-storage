package me.tobystrong.compactstorage.block.entity;

import me.tobystrong.compactstorage.CompactStorage;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.text.Text;
import net.minecraft.util.DefaultedList;

import java.util.Set;

public class BarrelBlockEntity extends BlockEntity implements BlockEntityClientSerializable, Inventory
{
    public class InsertItemsResult
    {
        public ItemStack returned;
        public boolean success;

        public InsertItemsResult() {
            this.returned = ItemStack.EMPTY;
            this.success = false;
        }
    }

    public DefaultedList<ItemStack> barrel_items;

    public BarrelBlockEntity() {
        super(CompactStorage.BARREL_ENTITY_TYPE);
        barrel_items = DefaultedList.ofSize(64, ItemStack.EMPTY);
    }

    public int getTotalItems() {
        int count = 0;

        for(int i = 0; i < 64; i++) {
            count += getInvStack(i).getCount();
        }

        return count;
    }

    public ItemStack getBarrelItem() {
        ItemStack barrelItem = ItemStack.EMPTY;

        for(int i = 0; i < 64; i++) {
            if(!(barrelItem = barrel_items.get(i)).isEmpty()) {
                break;
            }
        }

        return barrelItem;
    }
    

    public ItemStack dropItem() {
        if(getBarrelItem().isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            ItemStack stack = ItemStack.EMPTY;
            for(int i = 63; i >= 0; i--) {
                if(!getInvStack(i).isEmpty()) {
                    stack = takeInvStack(i, getInvStack(i).getCount());
                    break;
                }
            }

            markDirty();
            sync();

            return stack;
        }
    }

    public ItemStack insertItem(ItemStack barrel_item) {
        if(!getBarrelItem().isEmpty()) {
            int amount_left = barrel_item.getCount();

            for(int i = 0; i < 64; i++) {
                ItemStack slot_stack = getInvStack(i);

                if(slot_stack.isEmpty()) {
                    setInvStack(i, barrel_item);
                    amount_left = 0;
                } else {
                    int slotCanTake = slot_stack.getMaxCount() - slot_stack.getCount();
                    getInvStack(i).increment(slotCanTake);
                    amount_left -= slotCanTake;
                }

                barrel_item.setCount(amount_left);

                if(barrel_item.isEmpty()) {
                    break;
                }
            }

            barrel_item.setCount(amount_left);
        } else {
            setInvStack(0, barrel_item);
            barrel_item = ItemStack.EMPTY;
        }

        markDirty();
        sync();

        return barrel_item;
    }

    public int getMaxStorage() {
        if(getBarrelItem() == ItemStack.EMPTY) {
            return 64 * 64;
        } else {
            return getBarrelItem().getMaxCount() * 64;
        }
    }

    public String getText()
    {
        if(getBarrelItem().isEmpty())
        {
            return "Empty";
        }
        else if(getTotalItems() < getBarrelItem().getMaxCount())
        {
            return getTotalItems() + "";
        }
        else
        {
            int numOfStacks = getTotalItems() / getBarrelItem().getMaxCount();

            return numOfStacks + "x" + getBarrelItem().getMaxCount();
        }
    }

    public String getSubText() {
        if(!getBarrelItem().isEmpty() && getTotalItems() > getBarrelItem().getMaxCount()) {
            int remainder = (getTotalItems() % getBarrelItem().getMaxCount());

            if(remainder != 0) {
                return "+ " + remainder;
            }
        }

        return "";
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        Inventories.toTag(tag, barrel_items);

        return super.toTag(tag);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        Inventories.fromTag(tag, barrel_items);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return toTag(tag);
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        fromTag(tag);
    }


    /**
     * Implemented inventory methods so that it can interface with other mods *hopefully*
     */

    @Override
    public void clear() {
        barrel_items.clear();
    }

    @Override
    public boolean canPlayerUseInv(PlayerEntity player) {
        return true;
    }

    @Override
    public int getInvSize() {
        return 64;
    }

    @Override
    public ItemStack getInvStack(int slot) {
        return barrel_items.get(slot);
    }

    @Override
    public boolean isInvEmpty() {
        return barrel_items.stream().allMatch(s -> s == ItemStack.EMPTY);
    }

    @Override
    public ItemStack removeInvStack(int slot) {
        markDirty();
        return barrel_items.remove(slot);
    }

    @Override
    public void setInvStack(int slot, ItemStack stack) {
        System.out.println("this is being called with params " + slot + " : " + stack.toString());
        if(getBarrelItem().isEmpty() || ItemStack.areItemsEqual(stack, getBarrelItem())) {
            System.out.println("setting slot");
            barrel_items.set(slot, stack);
        }

        markDirty();
    }

    @Override
    public ItemStack takeInvStack(int slot, int amount) {
        return barrel_items.get(slot).split(amount);
    }
}
