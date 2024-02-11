package com.witchica.compactstorage.block.entity;

import com.witchica.compactstorage.CompactStorage;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class DrumBlockEntity extends BlockEntity {
    public ItemStack clientItem = ItemStack.EMPTY;
    public int clientStackSize;
    public int clientStoredItems;

    public DrumBlockEntity(BlockPos pos, BlockState state) {
        super(CompactStorage.DRUM_BLOCK_ENTITY_TYPE.get(), pos, state);
    }

    public final SimpleInventory inventory = new SimpleInventory(64) {
        @Override
        public int getMaxCountPerStack() {
            if(getStack(0).isEmpty()) {
                return 64;
            }

            return getStack(0).getMaxCount();
        }

        @Override
        public boolean isValid(int slot, ItemStack stack) {
            return getStack(0).isEmpty() || ItemStack.areItemsEqual(getStack(0), stack);
        }

        @Override
        public void markDirty() {
            super.markDirty();
            DrumBlockEntity.this.markDirty();
        }

        @Override
        public ItemStack removeStack(int slot) {
            for(int i = size()-1; i >=0; i--) {
                ItemStack stack = getStack(i);

                if(stack != null && !stack.isEmpty()) {
                    slot = i;
                    break;
                }
            }

            ItemStack stack =  super.removeStack(slot);
            markDirty();
            return stack;
        }

        @Override
        public ItemStack removeStack(int slot, int amount) {
            for(int i = size()-1; i >=0; i--) {
                ItemStack stack = getStack(i);

                if(stack != null && !stack.isEmpty()) {
                    slot = i;
                    break;
                }
            }

            ItemStack stack = super.removeStack(slot, amount);
            markDirty();
            return stack;
        }
    };

    public final InventoryStorage inventoryWrapper = InventoryStorage.of(inventory, null);


    public boolean hasAnyItems() {
        return !inventory.getStack(0).isEmpty();
    }

    public Item getStoredType() {
        return hasAnyItems() ? inventory.getStack(0).getItem() : ItemStack.EMPTY.getItem();
    }

    public int getTotalItemCount() {
        return hasAnyItems() ? inventory.count(getStoredType()) : 0;
    }
    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.put("Inventory", inventory.toNbtList());
        nbt.put("ClientItem", new ItemStack(getStoredType(), 1).writeNbt(new NbtCompound()));
        nbt.putInt("ClientStackSize", getStoredType().getMaxCount());
        nbt.putInt("ClientStoredItems", getTotalItemCount());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        inventory.readNbtList(nbt.getList("Inventory", NbtElement.COMPOUND_TYPE));

        this.clientItem = ItemStack.fromNbt(nbt.getCompound("ClientItem"));
        this.clientStackSize = nbt.getInt("ClientStackSize");
        this.clientStoredItems = nbt.getInt("ClientStoredItems");
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if(world != null) {
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
        }
    }

    public String getTextToDisplay() {
        if(clientStackSize == 0) {
            return "Empty";
        }

        int numStacks = clientStoredItems / clientStackSize;
        int leftover = clientStoredItems % clientStackSize;

        if(numStacks == 0 & leftover == 0) {
            return "Empty";
        } else if(numStacks == 0) {
            return "" + leftover;
        } else if(leftover == 0) {
            return clientStackSize + " x " + numStacks;
        } else {
            return (clientStackSize + " x " + numStacks + " + " + leftover);
        }
    }
}
