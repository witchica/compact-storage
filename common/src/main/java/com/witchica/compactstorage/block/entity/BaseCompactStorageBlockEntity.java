package com.witchica.compactstorage.block.entity;

import com.mojang.serialization.Codec;
import com.witchica.compactstorage.menu.CompactStorageMenuData;
import com.witchica.compactstorage.menu.GenericCompactStorageMenu;
import com.witchica.compactstorage.api.inventory.RetainingContainer;
import net.blay09.mods.balm.world.BalmMenuProvider;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import com.witchica.compactstorage.api.inventory.ResizableContainer;
import com.witchica.compactstorage.data.UpgradeType;
import org.jspecify.annotations.Nullable;

import java.util.List;

public abstract class BaseCompactStorageBlockEntity extends BaseContainerBlockEntity implements BalmMenuProvider<@NotNull CompactStorageMenuData>, ResizableContainer, RetainingContainer {
    private NonNullList<ItemStack> items;

    private int inventoryWidth = 9;
    private int inventoryHeight = 3;
    private boolean retaining;
    protected ContainerOpenersCounter containerOpenersCounter;

    public BaseCompactStorageBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        items = NonNullList.withSize(inventoryWidth * inventoryHeight, ItemStack.EMPTY);

        this.containerOpenersCounter = new ContainerOpenersCounter() {
            @Override
            protected void onOpen(Level level, BlockPos blockPos, BlockState blockState) {
                onContainerOpened(level, blockPos, blockState);
            }

            @Override
            protected void onClose(Level level, BlockPos blockPos, BlockState blockState) {
                onContainerClosed(level, blockPos, blockState);
            }

            @Override
            protected void openerCountChanged(Level level, BlockPos blockPos, BlockState blockState, int i, int i1) {
                signalOpenCount(level, pos, blockState, i1);
            }

            @Override
            public boolean isOwnContainer(Player player) {
                if (!(player.containerMenu instanceof GenericCompactStorageMenu)) {
                    return false;
                } else {
                    Container container = ((GenericCompactStorageMenu)player.containerMenu).container;
                    return container == BaseCompactStorageBlockEntity.this || container instanceof CompoundContainer && ((CompoundContainer)container).contains(BaseCompactStorageBlockEntity.this);
                }
            }
        };
    }
    public static void ticker(Level level, BlockPos blockPos, BlockState blockState, BaseCompactStorageBlockEntity entity) {
        entity.tick();
    }

    public abstract void tick();

    public void onContainerOpened(Level level, BlockPos blockPos, BlockState blockState) {

    }

    public void onContainerClosed(Level level, BlockPos blockPos, BlockState blockState) {

    }

    protected void playSound(Level level, BlockPos pos, SoundEvent sound) {
        double d0 = (double)pos.getX() + (double)0.5F;
        double d1 = (double)pos.getY() + (double)0.5F;
        double d2 = (double)pos.getZ() + (double)0.5F;
        level.playSound((Entity)null, d0, d1, d2, sound, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
    }

    protected void signalOpenCount(Level level, BlockPos pos, BlockState state, int eventParam) {
        Block block = state.getBlock();
        level.blockEvent(pos, block, 1, eventParam);
    }

    @Override
    public @NotNull CompactStorageMenuData getScreenOpeningData(ServerPlayer player) {
        return CompactStorageMenuData.ofBlock(getBlockPos());
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, @NotNull CompactStorageMenuData> getScreenStreamCodec() {
        return CompactStorageMenuData.STREAM_CODEC;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullList) {
        this.items = nonNullList;
        this.setChanged();
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new GenericCompactStorageMenu(i, inventory, CompactStorageMenuData.ofBlock(getBlockPos()));
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    protected void saveAdditional(ValueOutput writer) {
        super.saveAdditional(writer);
        writer.store("InventoryWidth", Codec.INT, inventoryWidth);
        writer.store("InventoryHeight", Codec.INT, inventoryHeight);
        ContainerHelper.saveAllItems(writer, items);
    }

    @Override
    protected void loadAdditional(ValueInput reader) {
        super.loadAdditional(reader);
        this.inventoryWidth = reader.getIntOr("InventoryWidth", 9);
        this.inventoryHeight = reader.getIntOr("InventoryHeight", 3);
        this.items = NonNullList.withSize(inventoryWidth * inventoryHeight, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(reader, items);
    }

    public void resizeInventory() {
        if(this.items.size() != (inventoryWidth * inventoryHeight)) {
            NonNullList<ItemStack> newList = NonNullList.withSize(inventoryWidth * inventoryHeight, ItemStack.EMPTY);
            for(int i = 0; i < items.size(); i++) {
                newList.set(i, items.get(i));
            }
            this.items = newList;
        }

        this.setChanged();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        BalmBlockEntityUtils.sync(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return BalmBlockEntityUtils.createUpdateTag(registries, this::saveAdditional);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return BalmBlockEntityUtils.createUpdatePacket(this);
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
        resizeInventory();
    }

    @Override
    public boolean canApplyUpgrade(UpgradeType upgradeType) {
        if(upgradeType == UpgradeType.WIDTH_UPGRADE) {
            return getWidth() < getMaximumWidth();
        } else if(upgradeType == UpgradeType.HEIGHT_UPGRADE) {
            return getHeight() < getMaximumHeight();
        } else if (upgradeType == UpgradeType.RETAINING_UPGRADE) {
            return !isRetaining();
        } else {
            return false;
        }
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
        } else if(upgradeType == UpgradeType.RETAINING_UPGRADE) {
            setRetaining(true);
            return true;
        }

        return false;
    }

    @Override
    public void setRetaining(boolean retaining) {
        this.retaining = retaining;
    }

    @Override
    public boolean isRetaining() {
        return retaining;
    }

    @Override
    public void startOpen(ContainerUser user) {
        if(!this.remove && !user.getLivingEntity().isSpectator()) {
            this.containerOpenersCounter.incrementOpeners(user.getLivingEntity(), user.getLivingEntity().level(), getBlockPos(), getBlockState(), user.getContainerInteractionRange());
        }
    }

    @Override
    public void stopOpen(ContainerUser user) {
        if(!this.remove && !user.getLivingEntity().isSpectator()) {
            this.containerOpenersCounter.decrementOpeners(user.getLivingEntity(), user.getLivingEntity().level(), getBlockPos(), getBlockState());
        }
    }

    @Override
    public List<ContainerUser> getEntitiesWithContainerOpen() {
        return this.containerOpenersCounter.getEntitiesWithContainerOpen(getLevel(), getBlockPos());
    }

    public void recheckOpen() {
        if (!this.remove) {
            this.containerOpenersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }
}
