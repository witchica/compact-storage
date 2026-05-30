package com.witchica.compactstorage.inventory;

import com.witchica.compactstorage.api.StorageTypeProvider;
import com.witchica.compactstorage.api.inventory.ResizableContainer;
import com.witchica.compactstorage.api.inventory.UpgradableContainer;
import com.witchica.compactstorage.data.CompactStorageOpeningSource;
import com.witchica.compactstorage.data.StorageType;
import com.witchica.compactstorage.data.UpgradeType;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemContainerContents;
import org.jspecify.annotations.NonNull;

import javax.xml.crypto.Data;
import java.util.Optional;

public class BackpackInventory implements Container, ResizableContainer, UpgradableContainer {
    private final Player player;
    private final CompactStorageOpeningSource openingSource;
    private final ItemStack backpackStack;
    private final Optional<InteractionHand> hand;
    private final @NonNull StorageType storageType;

    private int inventoryWidth;
    private int inventoryHeight;
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
        if(stack.has(DataComponents.CUSTOM_DATA)) {
            CustomData data = stack.get(DataComponents.CUSTOM_DATA);
            CompoundTag tag = data.copyTag();


            switch(tag.getIntOr("Version", 0)) {
                case 2: {
                    this.inventoryWidth = tag.getIntOr("InventoryWidth", 9);
                    this.inventoryHeight = tag.getIntOr("InventoryHeight", 3);
                    break;
                }
                default: {
                    this.inventoryWidth = tag.getIntOr("inventory_width", 9);
                    this.inventoryHeight = tag.getIntOr("inventory_height", 3);
                }
            }
        } else {
            this.inventoryWidth = 9;
            this.inventoryHeight = 3;
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

        if(openingSource == CompactStorageOpeningSource.BACKPACK_IN_HAND) {
            ItemStack handStack = player.getItemInHand(hand.get());
            handStack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(getItems()));
            handStack.set(DataComponents.CUSTOM_DATA, saveToCustomData());
        }
    }

    private CustomData saveToCustomData() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("InventoryWidth", inventoryWidth);
        tag.putInt("InventoryHeight", inventoryHeight);
        tag.putInt("Version", 2);
        return CustomData.of(tag);
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
    public boolean canApplyUpgrade(UpgradeType upgradeType) {
        if(upgradeType == UpgradeType.WIDTH_UPGRADE) {
            return getWidth() < getMaximumWidth();
        } else if(upgradeType == UpgradeType.HEIGHT_UPGRADE) {
            return getHeight() < getMaximumHeight();
        }

        return false;
    }

    @Override
    public boolean applyUpgrade(UpgradeType upgradeType) {
        if(!canApplyUpgrade(upgradeType)) {
            return false;
        }

        if(upgradeType == UpgradeType.WIDTH_UPGRADE) {
            setWidth(Math.min(getWidth() + 1, getMaximumWidth()));
            return true;
        } else if(upgradeType == UpgradeType.HEIGHT_UPGRADE) {
            setHeight(Math.min(getHeight() + 1, getMaximumHeight()));
            return true;
        }

        return false;
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
