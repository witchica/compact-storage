package com.witchica.compactstorage.item;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.api.StorageTypeProvider;
import com.witchica.compactstorage.components.ResizableInventoryComponent;
import com.witchica.compactstorage.data.StorageType;
import com.witchica.compactstorage.data.UpgradeType;
import com.witchica.compactstorage.menu.CompactStorageMenuData;
import com.witchica.compactstorage.menu.GenericCompactStorageMenu;
import com.witchica.compactstorage.mod.CompactStorageComponents;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.world.BalmMenuProvider;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class BackpackItem extends Item implements StorageTypeProvider {
    public class HeldBackpackMenuProvider implements BalmMenuProvider<@NotNull CompactStorageMenuData> {

        private final InteractionHand hand;
        private final ItemStack item;

        public HeldBackpackMenuProvider(InteractionHand hand, ItemStack item) {
            this.hand = hand;
            this.item = item;
        }

        @Override
        public @NotNull CompactStorageMenuData getScreenOpeningData(ServerPlayer player) {
            return CompactStorageMenuData.ofHeldBackpack(this.hand.ordinal());
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, @NotNull CompactStorageMenuData> getScreenStreamCodec() {
            return CompactStorageMenuData.STREAM_CODEC;
        }

        @Override
        public Component getDisplayName() {
            return item.getItemName();
        }

        @Override
        public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
            return new GenericCompactStorageMenu(i, inventory, CompactStorageMenuData.ofHeldBackpack(this.hand.ordinal()));
        }
    }

    public static class CuriosBackpackMenuProvider implements BalmMenuProvider<@NotNull CompactStorageMenuData> {
        private final ItemStack item;

        public CuriosBackpackMenuProvider(Player player) {
            this.item = CompactStorage.findCuriosBackpack(player);
        }

        @Override
        public @NotNull CompactStorageMenuData getScreenOpeningData(ServerPlayer player) {
            return CompactStorageMenuData.ofCuriosBackpack();
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, @NotNull CompactStorageMenuData> getScreenStreamCodec() {
            return CompactStorageMenuData.STREAM_CODEC;
        }

        @Override
        public Component getDisplayName() {
            return item.getItemName();
        }

        @Override
        public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
            return new GenericCompactStorageMenu(i, inventory, CompactStorageMenuData.ofCuriosBackpack());
        }
    }

    private final StorageType storageType;

    public BackpackItem(Properties properties, StorageType storageType) {
        super(properties);
        this.storageType = storageType;
    }

    @Override
    public @NonNull StorageType getStorageType() {
        return this.storageType;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if(!level.isClientSide()) {
            if(player.containerMenu instanceof GenericCompactStorageMenu) {
                ((ServerPlayer) player).closeContainer();
                return super.use(level, player, hand);
            }

            ItemStack backpackStack = player.getItemInHand(hand);
            ItemStack oppositeStack = player.getItemInHand(hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);

            if(!oppositeStack.isEmpty() && oppositeStack.getItem() instanceof StorageUpgradeItem storageUpgradeItem) {
                UpgradeType upgradeType = storageUpgradeItem.getUpgradeType();
                ResizableInventoryComponent resizableInventoryComponent = backpackStack.has(CompactStorageComponents.RESIZABLE_INVENTORY_DATA.value()) ? backpackStack.get(CompactStorageComponents.RESIZABLE_INVENTORY_DATA.value()) : new ResizableInventoryComponent(CompactStorage.config().constrainWidth(CompactStorage.config().backpackDefaultWidth), CompactStorage.config().constrainHeight(CompactStorage.config().backpackDefaultHeight));

                if(resizableInventoryComponent == null) {
                    return InteractionResult.FAIL;
                }

                if(upgradeType == UpgradeType.WIDTH_UPGRADE) {
                    if(resizableInventoryComponent.inventoryWidth() < CompactStorage.config().constrainWidth(CompactStorage.config().backpackMaxWidth)) {
                        backpackStack.update(CompactStorageComponents.RESIZABLE_INVENTORY_DATA.value(), resizableInventoryComponent, ResizableInventoryComponent::increaseWidth);
                        oppositeStack.setCount(oppositeStack.getCount() - 1);
                        level.playSound(null, player.getOnPos(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.NEUTRAL);
                        return InteractionResult.CONSUME;
                    } else {
                        level.playSound(null, player.getOnPos(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL);
                        player.displayClientMessage(upgradeType.upgradeFailMessage(), true);
                        return InteractionResult.FAIL;
                    }
                } else if(upgradeType == UpgradeType.HEIGHT_UPGRADE) {
                    if(resizableInventoryComponent.inventoryHeight() < CompactStorage.config().constrainHeight(CompactStorage.config().backpackMaxHeight)) {
                        backpackStack.update(CompactStorageComponents.RESIZABLE_INVENTORY_DATA.value(), resizableInventoryComponent, ResizableInventoryComponent::increaseHeight);
                        oppositeStack.setCount(oppositeStack.getCount() - 1);
                        level.playSound(null, player.getOnPos(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.NEUTRAL);
                        return InteractionResult.CONSUME;
                    } else {
                        level.playSound(null, player.getOnPos(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL);
                        player.displayClientMessage(upgradeType.upgradeFailMessage(), true);
                        return InteractionResult.FAIL;
                    }
                } else {
                    level.playSound(null, player.getOnPos(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL);
                    player.displayClientMessage(upgradeType.upgradeNotCompatibleMessage(), true);
                    return InteractionResult.FAIL;
                }
            }

            Balm.networking().openMenu(player, new HeldBackpackMenuProvider(hand, backpackStack));
            return InteractionResult.CONSUME;
        }

        return super.use(level, player, hand);
    }
}
