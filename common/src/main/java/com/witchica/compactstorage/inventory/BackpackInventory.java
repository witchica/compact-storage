package com.witchica.compactstorage.inventory;

import com.witchica.compactstorage.api.StorageTypeProvider;
import com.witchica.compactstorage.api.inventory.ResizableContainer;
import com.witchica.compactstorage.api.inventory.UpgradableContainer;
import com.witchica.compactstorage.components.ResizableInventoryComponent;
import com.witchica.compactstorage.data.CompactStorageOpeningSource;
import com.witchica.compactstorage.data.StorageType;
import com.witchica.compactstorage.data.UpgradeType;
import com.witchica.compactstorage.mod.CompactStorageComponents;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemContainerContents;
import org.jspecify.annotations.NonNull;
import java.util.Optional;

public class BackpackInventory implements Container, ResizableContainer {
    private final Player player;
    private final CompactStorageOpeningSource openingSource;
    private final ItemStack backpackStack;
    private final Optional<InteractionHand> hand;
    private final @NonNull StorageType storageType;

    private int inventoryWidth = 9;
    private int inventoryHeight = 3;
    private NonNullList<ItemStack> items;


    public BackpackInventory(Player player, CompactStorageOpeningSource openingSource, ItemStack backpackStack, Optional<InteractionHand> hand) {
        this.player = player;
        this.openingSource = openingSource;
        this.backpackStack = backpackStack;
        this.hand = hand;
        this.storageType = ((StorageTypeProvider) backpackStack.getItem()).getStorageType();

        fromItemStack(backpackStack);
    }

    public void fromItemStack(ItemStack stack) {
        if(stack.has(CompactStorageComponents.RESIZABLE_INVENTORY_DATA.value())) {
            ResizableInventoryComponent resizableInventoryComponent = stack.get(CompactStorageComponents.RESIZABLE_INVENTORY_DATA.value());

            if(resizableInventoryComponent != null) {
                resizableInventoryComponent.apply(this);
            }
        } else if(stack.has(DataComponents.CUSTOM_DATA)) {
            CustomData data = stack.get(DataComponents.CUSTOM_DATA);

            if(data != null) {
                CompoundTag tag = data.copyTag();

                this.inventoryWidth = tag.getIntOr(tag.contains("inventory_width") ? "inventory_width" : "InventoryWidth", 9);
                this.inventoryHeight = tag.getIntOr(tag.contains("inventory_height") ? "inventory_height" : "InventoryHeight", 3);
            }

        }

        this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);

        if(stack.has(DataComponents.CONTAINER)) {
            ItemContainerContents contents = stack.get(DataComponents.CONTAINER);

            if(contents != null) {
                contents.copyInto(getItems());
            }
        }
    }

    @Override
    public void startOpen(ContainerUser user) {
        Container.super.startOpen(user);
        player.playSound(storageType.backpackOpenSound);
    }

    @Override
    public void stopOpen(ContainerUser user) {
        Container.super.stopOpen(user);
        player.playSound(storageType.backpackCloseSound);

        saveBackpackData();
    }

    public void saveBackpackData() {
        if(openingSource == CompactStorageOpeningSource.BACKPACK_IN_HAND && hand.isPresent()) {
            ItemStack handStack = player.getItemInHand(hand.get());
            handStack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(getItems()));
            handStack.set(CompactStorageComponents.RESIZABLE_INVENTORY_DATA.value(), new ResizableInventoryComponent(inventoryWidth, inventoryHeight));
        }
    }

    @Override
    public int getWidth() {
        return inventoryWidth;
    }

    @Override
    public int getHeight() {
        return inventoryHeight;
    }

    @Override
    public int getMaximumWidth() {
        return 21;
    }

    @Override
    public int getMaximumHeight() {
        return 12;
    }

    @Override
    public void setSize(int width, int height) {
        this.inventoryWidth = width;
        this.inventoryHeight = height;
    }

    @Override
    public int getContainerSize() {
        return getWidth() * getHeight();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack item : getItems()) {
            if(!item.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getItem(int i) {
        return getItems().get(i);
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack itemstack = ContainerHelper.removeItem(this.getItems(), slot, amount);
        if (!itemstack.isEmpty()) {
            this.setChanged();
        }

        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.getItems(), slot);
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        this.getItems().set(slot, itemStack);
        itemStack.limitSize(this.getMaxStackSize(itemStack));
        this.setChanged();
    }

    @Override
    public void setChanged() {
        saveBackpackData();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.getItems().clear();
    }
}
