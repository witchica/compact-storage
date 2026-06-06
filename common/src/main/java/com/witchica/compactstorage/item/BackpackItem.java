package com.witchica.compactstorage.item;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.api.StorageTypeProvider;
import com.witchica.compactstorage.api.StorageUpgrade;
import com.witchica.compactstorage.api.inventory.ItemWithResizableInventory;
import com.witchica.compactstorage.api.inventory.UpgradeCheckProvider;
import com.witchica.compactstorage.data.StorageType;
import com.witchica.compactstorage.menu.CompactStorageMenuData;
import com.witchica.compactstorage.menu.GenericCompactStorageMenu;
import com.witchica.compactstorage.mod.CompactStorageItems;
import com.witchica.compactstorage.mod.CompactStorageUpgrades;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.world.BalmMenuProvider;
import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class BackpackItem extends Item implements StorageTypeProvider, ItemWithResizableInventory, UpgradeCheckProvider {
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

            InteractionResult upgradeResult = StorageUpgrade.applyUpgradeToItem(oppositeStack, backpackStack, player, level);

            if(upgradeResult != InteractionResult.PASS) {
                return upgradeResult;
            } else if(!oppositeStack.isEmpty() && oppositeStack.getItem() instanceof DyeItem && storageType.canDye()) {
                DyeColor dyeColor = oppositeStack.get(DataComponents.DYE);

                player.setItemInHand(hand, backpackStack.transmuteCopy(CompactStorageItems.BACKPACK_ITEMS.get(StorageType.fromDye(dyeColor))));
                oppositeStack.setCount(oppositeStack.getCount() - 1);
                level.playSound(null, player.getOnPos(), SoundEvents.SLIME_SQUISH, SoundSource.BLOCKS, 1f, 1f);
                return InteractionResult.CONSUME;
            }

            Balm.networking().openMenu(player, new HeldBackpackMenuProvider(hand, backpackStack));
            return InteractionResult.CONSUME;
        }

        return super.use(level, player, hand);
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
    public int getDefaultWidth() {
        return CompactStorage.config().constrainWidth(CompactStorage.config().backpackDefaultWidth);
    }

    @Override
    public int getDefaultHeight() {
        return CompactStorage.config().constrainHeight(CompactStorage.config().backpackDefaultHeight);
    }

    @Override
    public boolean isUpgradeAccepted(StorageUpgrade upgrade) {
        return (upgrade == CompactStorageUpgrades.WIDTH_UPGRADE || upgrade == CompactStorageUpgrades.HEIGHT_UPGRADE || upgrade == CompactStorageUpgrades.VOID_SLOT_UPGRADE);
    }
}
