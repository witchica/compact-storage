package com.witchica.compactstorage.inventory;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.api.StorageTypeProvider;
import com.witchica.compactstorage.api.inventory.ResizableContainer;
import com.witchica.compactstorage.api.inventory.VoidSlotProvider;
import com.witchica.compactstorage.components.ResizableInventoryComponent;
import com.witchica.compactstorage.data.CompactStorageOpeningSource;
import com.witchica.compactstorage.data.StorageType;
import com.witchica.compactstorage.mod.CompactStorageComponents;
import com.witchica.compactstorage.util.CompactStorageUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;
import org.jspecify.annotations.NonNull;
import java.util.Optional;

public class BackpackInventory implements Container, ResizableContainer, VoidSlotProvider {
    private final Player player;
    private final CompactStorageOpeningSource openingSource;
    private final ItemStack backpackStack;
    private final Optional<InteractionHand> hand;
    private final @NonNull StorageType storageType;

    private int inventoryWidth;
    private int inventoryHeight;
    private NonNullList<ItemStack> items;
    private boolean hasVoidSlot;


    public BackpackInventory(Player player, CompactStorageOpeningSource openingSource, ItemStack backpackStack, Optional<InteractionHand> hand) {
        this.player = player;
        this.openingSource = openingSource;
        this.backpackStack = backpackStack;
        this.hand = hand;
        this.storageType = ((StorageTypeProvider) backpackStack.getItem()).getStorageType();

        this.inventoryWidth = getDefaultWidth();
        this.inventoryHeight = getDefaultHeight();

        fromItemStack(backpackStack, player.registryAccess());
    }

    public void fromItemStack(ItemStack stack, RegistryAccess registryAccess) {
        if(stack.has(CompactStorageComponents.RESIZABLE_INVENTORY_DATA.value())) {
            ResizableInventoryComponent resizableInventoryComponent = stack.get(CompactStorageComponents.RESIZABLE_INVENTORY_DATA.value());

            if(resizableInventoryComponent != null) {
                resizableInventoryComponent.apply(this);
            }
        }

        this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);

        if(stack.has(DataComponents.CONTAINER)) {
            ItemContainerContents contents = stack.get(DataComponents.CONTAINER);

            if(contents != null) {
                contents.copyInto(getItems());
            }
        }
        
        if(stack.has(CompactStorageComponents.VOID_SLOT.value())) {
            this.hasVoidSlot = backpackStack.get(CompactStorageComponents.VOID_SLOT.value()).booleanValue();
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
        Optional<ItemStack> stackToSave = Optional.empty();

        if(openingSource == CompactStorageOpeningSource.BACKPACK_IN_HAND && hand.isPresent()) {
            stackToSave = Optional.of(player.getItemInHand(hand.get()));
        } else if(openingSource == CompactStorageOpeningSource.BACKPACK_HOT_KEY) {
            stackToSave = Optional.of(CompactStorage.findCuriosBackpack(this.player));
        }

        if(stackToSave.isPresent()) {
            ItemStack stack = stackToSave.get();

            stack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(getItems()));
            stack.set(CompactStorageComponents.RESIZABLE_INVENTORY_DATA.value(), new ResizableInventoryComponent(inventoryWidth, inventoryHeight));
            stack.set(CompactStorageComponents.VOID_SLOT.value(), this.hasVoidSlot());
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
        return CompactStorage.config().constrainWidth(CompactStorage.config().backpackMaxWidth);
    }

    @Override
    public int getMaximumHeight() {
        return CompactStorage.config().constrainHeight(CompactStorage.config().backpackMaxHeight);
    }

    @Override
    public void setSize(int width, int height) {
        this.inventoryWidth = width;
        this.inventoryHeight = height;
    }

    @Override
    public int getDefaultWidth() {
        return CompactStorage.config().constrainWidth(CompactStorage.config().backpackDefaultWidth);
    }

    @Override
    public int getDefaultHeight() {
        return CompactStorage.config().constrainHeight(CompactStorage.config().backpackDefaultHeight);
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

    @Override
    public boolean hasVoidSlot() {
        return hasVoidSlot;
    }

    @Override
    public void setHasVoidSlot(boolean hasVoidSlot) {
        // Not implemented
    }
}
