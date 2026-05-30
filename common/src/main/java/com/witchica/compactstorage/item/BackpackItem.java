package com.witchica.compactstorage.item;

import com.witchica.compactstorage.api.StorageTypeProvider;
import com.witchica.compactstorage.client.screens.GenericCompactStorageMenuScreen;
import com.witchica.compactstorage.data.StorageType;
import com.witchica.compactstorage.menu.CompactStorageMenuData;
import com.witchica.compactstorage.menu.GenericCompactStorageMenu;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.world.BalmMenuProvider;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
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
    public class BackpackMenuProvider implements BalmMenuProvider<@NotNull CompactStorageMenuData> {

        private final InteractionHand hand;
        private final ItemStack item;

        public BackpackMenuProvider(InteractionHand hand, ItemStack item) {
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
            Balm.networking().openMenu(player, new BackpackMenuProvider(hand, player.getItemInHand(hand)));

            return InteractionResult.CONSUME;
        }

        return super.use(level, player, hand);
    }
}
