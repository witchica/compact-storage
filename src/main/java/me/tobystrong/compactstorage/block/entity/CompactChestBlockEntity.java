package me.tobystrong.compactstorage.block.entity;

import me.tobystrong.compactstorage.CompactStorage;
import me.tobystrong.compactstorage.container.CompactChestContainer;
import me.tobystrong.compactstorage.util.CompactStorageInventoryImpl;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DefaultedList;

public class CompactChestBlockEntity extends LootableContainerBlockEntity implements BlockEntityClientSerializable, CompactStorageInventoryImpl{
    private DefaultedList<ItemStack> inventory;

    public int inventoryWidth = 9;
    public int inventoryHeight = 3;

    public CompactChestBlockEntity() {
        super(CompactStorage.COMPACT_CHEST_ENTITY_TYPE);
        this.inventory = DefaultedList.ofSize(inventoryWidth * inventoryHeight, ItemStack.EMPTY);
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

    public void test() {
        this.inventory = DefaultedList.ofSize(inventoryWidth * inventoryHeight, ItemStack.EMPTY);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);

        inventoryWidth = tag.contains("inventory_width") ? tag.getInt("inventory_width") : 9;
        inventoryHeight = tag.contains("inventory_height") ? tag.getInt("inventory_height") : 3;

        this.inventory = DefaultedList.ofSize(inventoryWidth * inventoryHeight, ItemStack.EMPTY);
        ListTag listTag = tag.getList("Items", 10);

        for(int i = 0; i < listTag.size(); ++i) {
            CompoundTag compoundTag = listTag.getCompound(i);
            int j = compoundTag.getInt("Slot");
            if (j >= 0 && j < inventory.size()) {
                inventory.set(j, ItemStack.fromTag(compoundTag));
            }
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);

        //custom Inventories.toTag implementation because the byte limit got hit lol

        ListTag listTag = new ListTag();

        for(int i = 0; i < inventory.size(); ++i) {
            ItemStack itemStack = (ItemStack)inventory.get(i);
            if (!itemStack.isEmpty()) {
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.putInt("Slot", i);
                itemStack.toTag(compoundTag);
                listTag.add(compoundTag);
            }
        }

        tag.put("Items", listTag);

        tag.putInt("inventory_width", inventoryWidth);
        tag.putInt("inventory_height", inventoryHeight);

        return tag;
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return toTag(tag);
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        fromTag(tag);
    }

    @Override
    public int getInventoryWidth() {
        return inventoryWidth;
    }

    @Override
    public int getInventoryHeight() {
        return inventoryHeight;
    }
}
