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

    public ItemStack barrel_item = ItemStack.EMPTY;
    public int stack_size;

    public BarrelBlockEntity() {
        super(CompactStorage.BARREL_ENTITY_TYPE);
    }

    
    public ItemStack dropItems(PlayerEntity player, boolean simulate) {
        return dropItems(player, barrel_item.getMaxCount(), simulate);
    }

    public ItemStack dropItems(PlayerEntity player, int amount, boolean simulate) {
        ItemStack stack = ItemStack.EMPTY;

        if(stack_size > 0) {
            stack = barrel_item.copy();

            if(stack_size < amount) {
                stack.setCount(stack_size);

                if(!simulate) {
                    stack_size = 0;
                    barrel_item = ItemStack.EMPTY;
                }
            } else if (stack_size % barrel_item.getMaxCount() != 0) {
                stack.setCount(stack_size % barrel_item.getMaxCount());

                if(!simulate) {
                    stack_size -= stack.getCount();
                }
            } else {
                stack.setCount(amount);

                if(!simulate) {
                    stack_size -= amount;
                }
            }
        }

        markDirty();
        sync();

        return stack;
    }

    public InsertItemsResult insertItems(ItemStack stack, PlayerEntity player, boolean simulate) {
        ItemStack workingStack = stack.copy();
        InsertItemsResult result = new InsertItemsResult();

        if(barrel_item.isEmpty() && !workingStack.isEmpty())
        {
            if(!simulate)
            {
                barrel_item = workingStack.copy();
                stack_size = barrel_item.getCount();
            }

            workingStack.setCount(0);
            result.returned = workingStack;
            result.success = true;
        }
        else
        {
            if(!workingStack.isEmpty() && ItemStack.areItemsEqual(workingStack, barrel_item))
            {
                    int spaceLeft = getMaxStorage() - stack_size;
                    int willAccept = Math.min(spaceLeft, workingStack.getCount());
                    System.out.println("Will accept " + willAccept);
                    System.out.println("stack size " + workingStack.getCount());

                    if(willAccept > 0) {
                        workingStack.setCount(workingStack.getCount() - willAccept);
                        System.out.println(workingStack.getCount());
                        
                        result.returned = workingStack;
                        result.success = true;

                        if(!simulate) {
                            stack_size += willAccept;
                        }
                    }
            }
        }

        markDirty();
        sync();

        return result;
    }

    public DefaultedList<ItemStack> getItemsAsList() {
        DefaultedList<ItemStack> list = DefaultedList.ofSize(64, ItemStack.EMPTY);

        if(!barrel_item.isEmpty()) {
            int totalFullStacks = stack_size / barrel_item.getMaxCount();

            for(int i = 0; i < totalFullStacks; i++) {
                ItemStack stack = barrel_item.copy();
                stack.setCount(barrel_item.getMaxCount());

                list.set(i, stack);
            }

            if(totalFullStacks < 64) {
                ItemStack partial_stack = barrel_item.copy();
                partial_stack.setCount(stack_size % barrel_item.getMaxCount());
                list.set(totalFullStacks, partial_stack);
            }
        }

        return list;
    }

    public int getMaxStorage() {
        if(barrel_item == ItemStack.EMPTY) {
            return 64 * 64;
        } else {
            return barrel_item.getMaxCount() * 64;
        }
    }

    public String getText()
    {
        if(barrel_item.isEmpty())
        {
            return "Empty";
        }
        else if(stack_size < barrel_item.getMaxCount())
        {
            return stack_size + "";
        }
        else
        {
            int numOfStacks = stack_size / barrel_item.getMaxCount();

            return numOfStacks + "x" + barrel_item.getMaxCount();
        }
    }

    public String getSubText() {
        if(!barrel_item.isEmpty() && stack_size > barrel_item.getMaxCount()) {
            int remainder = (stack_size % barrel_item.getMaxCount());

            if(remainder != 0) {
                return "+ " + remainder;
            }
        }

        return "";
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        if(stack_size == 0) {
            barrel_item = ItemStack.EMPTY;
        }

        tag.put("item", barrel_item.toTag(new CompoundTag()));
        tag.putInt("stack_size", stack_size);

        return super.toTag(tag);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);

        barrel_item = ItemStack.fromTag(tag.getCompound("item"));
        stack_size = tag.getInt("stack_size");

        if(stack_size == 0) {
            barrel_item = ItemStack.EMPTY;
        }
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
        barrel_item = ItemStack.EMPTY;
        stack_size = 0;
    }

    @Override
    public boolean canPlayerUseInv(PlayerEntity player) {
        return true;
    }

    @Override
    public int getInvSize() {
        return getMaxStorage();
    }

    @Override
    public ItemStack getInvStack(int slot) {
        if(!barrel_item.isEmpty()) {
            ItemStack stack = barrel_item.copy();
            stack.setCount(Math.min(stack_size, stack.getMaxCount()));
            return stack;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean isInvEmpty() {
        return barrel_item.isEmpty();
    }

    @Override
    public ItemStack removeInvStack(int slot) {
        if(!barrel_item.isEmpty()) {
            ItemStack stack = barrel_item.copy();
            stack.setCount(Math.min(stack_size, stack.getMaxCount()));
            stack_size -= stack.getCount();

            if(stack_size == 0) {
                barrel_item = ItemStack.EMPTY;
            }

            return stack;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public void setInvStack(int slot, ItemStack stack) {
        insertItems(stack, null, false);
    }

    @Override
    public ItemStack takeInvStack(int slot, int amount) {
        return dropItems(null, amount, false);
    }
}
