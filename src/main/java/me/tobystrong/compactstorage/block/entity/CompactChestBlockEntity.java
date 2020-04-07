package me.tobystrong.compactstorage.block.entity;

import me.tobystrong.compactstorage.CompactStorage;
import me.tobystrong.compactstorage.container.CompactChestContainer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DefaultedList;

public class CompactChestBlockEntity extends LootableContainerBlockEntity {
    private DefaultedList<ItemStack> inventory;

    public int inventoryWidth = 9;
    public int inventoryHeight = 3;

    public CompactChestBlockEntity() {
        super(CompactStorage.COMPACT_CHEST_ENTITY_TYPE);
        this.inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText("container.chest");
    }

    @Override
    protected Container createContainer(int syncId, PlayerInventory playerInventory) {
        return new CompactChestContainer(syncId, playerInventory, (Inventory) this, inventoryWidth, inventoryHeight);
    }

    @Override
    protected DefaultedList<ItemStack> getInvStackList() {
        return inventory;
    }

    @Override
    protected void setInvStackList(DefaultedList<ItemStack> list) {
        this.inventory = list;
    }

    @Override
    public int getInvSize() {
        return inventoryWidth * inventoryHeight;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);

        inventoryWidth = tag.contains("inventory_width") ? tag.getInt("inventory_width") : 9;
        inventoryHeight = tag.contains("inventory_height") ? tag.getInt("inventory_height") : 3;

        this.inventory = DefaultedList.ofSize(inventoryWidth * inventoryHeight, ItemStack.EMPTY);
        if(!this.deserializeLootTable(tag)) {
            Inventories.fromTag(tag, this.inventory);
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);

        if (!this.serializeLootTable(tag)) {
            Inventories.toTag(tag, this.inventory);
        }

        tag.putInt("inventory_width", inventoryWidth);
        tag.putInt("inventory_height", inventoryHeight);

        return tag;
    }
}
