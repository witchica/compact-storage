package com.witchica.compactstorage.common.inventory;

import com.witchica.compactstorage.CompactStoragePlatform;
import com.witchica.compactstorage.common.item.BackpackItem;
import com.witchica.compactstorage.common.util.CompactStorageInventoryImpl;
import com.witchica.compactstorage.common.util.CompactStorageUtil;
import com.witchica.compactstorage.common.util.InventoryOpenSource;
import com.witchica.compactstorage.common.util.StorageUpgradeType;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class BackpackInventory implements Container, CompactStorageInventoryImpl  {
    private final ItemStack backpackItem;
    private final Optional<InteractionHand> hand;
    private final InventoryOpenSource openSource;
    public NonNullList<ItemStack> items;
    public int inventoryWidth;
    public int inventoryHeight;

    private final Player player;

    // Type: (0 : main hand, 1 : off hand, 2 : backpack key)
    public BackpackInventory(Player player, InventoryOpenSource openSource, ItemStack backpackStack, Optional<InteractionHand> hand) {
        this.player = player;
        this.hand = hand;
        this.openSource = openSource;
        this.backpackItem = backpackStack;

        if(backpackStack.hasTag()) {
            this.fromTag(backpackStack.getTag().contains("Backpack") ? backpackStack.getTag().getCompound("Backpack") : new CompoundTag());
        } else {
            this.fromTag(new CompoundTag());
        }
    }


    public void resizeInventory(boolean copy_contents) {
        NonNullList<ItemStack> newInventory = NonNullList.withSize(inventoryWidth * inventoryHeight, ItemStack.EMPTY);

        if(copy_contents) {
            NonNullList<ItemStack> list = this.items;

            for(int i = 0; i < list.size(); i++) {
                newInventory.set(i, list.get(i));
            }
        }

        this.items = newInventory;
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public int getInventoryWidth() {
        return inventoryWidth;
    }

    @Override
    public int getInventoryHeight() {
        return inventoryHeight;
    }

    @Override
    public boolean getRetaining() {
        return false;
    }

    @Override
    public void setRetaining() {

    }

    @Override
    public CompactStorageUtil.StorageVisualTypes getVisualType() {
        if (backpackItem != null && !backpackItem.isEmpty() && backpackItem.getItem() instanceof BackpackItem backpack) {
            return backpack.getVisualType();
        }

        return CompactStorageUtil.StorageVisualTypes.RED;
    }

    @Override
    public int getContainerSize() {
        return getInventoryWidth() * getInventoryHeight();
    }

    @Override
    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(this.items, slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.items.set(slot, stack);
    }

    @Override
    public void setChanged() {
        
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public void fromTag(CompoundTag tag) {
        this.inventoryWidth = tag.contains("inventory_width") ? tag.getInt("inventory_width") : 9;
        this.inventoryHeight = tag.contains("inventory_height") ? tag.getInt("inventory_height") : 6;
        
        this.items = NonNullList.withSize(inventoryWidth * inventoryHeight, ItemStack.EMPTY);
        readItemsFromTag(this.items, tag);
    }

    public boolean increaseSize(int x, int y) {
        if((inventoryWidth > 20 && x > 0) || (inventoryHeight > 11 && y > 0)) {
            return false;
        }

        inventoryWidth += x;
        inventoryHeight += y;

        resizeInventory(true);
        setChanged();

        return true;
    }

    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("inventory_width", inventoryWidth);
        tag.putInt("inventory_height", inventoryHeight);

        writeItemsToTag(this.items, tag);

        return tag;
    }

    @Override
    public void startOpen(Player player) {
        Container.super.startOpen(player);
        player.playNotifySound(getVisualType().isWooden() ? SoundEvents.WOODEN_TRAPDOOR_OPEN : SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 1f, 1f);
    }

    @Override
    public void stopOpen(Player player) {
        Container.super.stopOpen(player);
        Inventory inventory = player.getInventory();

        if(openSource == InventoryOpenSource.BACKPACK_OPEN_HAND) {
            InteractionHand playerHand = hand.orElse(InteractionHand.MAIN_HAND);
            if(player.getItemInHand(playerHand).getItem() instanceof BackpackItem) {
                if(!player.getItemInHand(playerHand).hasTag()) {
                    player.getItemInHand(playerHand).setTag(new CompoundTag());
                }

                player.getItemInHand(playerHand).getTag().put("Backpack", toTag());
            }
        } else if(openSource == InventoryOpenSource.BACKPACK_OPEN_INVENTORY) {
            ItemStack stack = CompactStoragePlatform.getAdditionalSlotBackpack(player).orElse(ItemStack.EMPTY);
            if(stack.getItem() instanceof BackpackItem) {
                if(!stack.hasTag()) {
                    stack.setTag(new CompoundTag());
                }

                stack.getTag().put("Backpack", toTag());
            }
        }

        player.playNotifySound(getVisualType().isWooden() ? SoundEvents.WOODEN_TRAPDOOR_CLOSE : SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 1f, 1f);
    }

    @Override
    public boolean applyUpgrade(StorageUpgradeType upgradeType) {
        if(!canAcceptUpgrade(upgradeType)) {
            return false;
        }

        switch(upgradeType) {
            case ROW ->  {
                return increaseSize(1, 0);
            }
            case COLUMM -> {
                return increaseSize(0, 1);
            }
            default -> {
                return false;
            }
        }
    }

    @Override
    public boolean hasUpgrade(StorageUpgradeType upgradeType) {
        switch (upgradeType) {
            case ROW -> {
                return inventoryWidth > 9;
            }
            case COLUMM -> {
                return inventoryHeight > 6;
            }
            default -> {
                return false;
            }
        }
    }

    @Override
    public boolean canAcceptUpgrade(StorageUpgradeType upgradeType) {
        switch (upgradeType) {
            case ROW -> {
                return inventoryWidth < 21;
            }
            case COLUMM -> {
                return inventoryHeight < 12;
            }
            default -> {
                return false;
            }
        }
    }
}