package com.witchica.compactstorage.block.entity;

import com.mojang.serialization.Codec;
import com.witchica.compactstorage.menu.CompactStorageMenuData;
import com.witchica.compactstorage.menu.CompactStorageMenuTypes;
import com.witchica.compactstorage.menu.GenericCompactStorageMenu;
import net.blay09.mods.balm.world.BalmMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import util.CompactStorageOpeningSource;

import java.util.Optional;

public abstract class BaseCompactStorageBlockEntity extends BaseContainerBlockEntity implements BalmMenuProvider<@NotNull CompactStorageMenuData> {
    private NonNullList<ItemStack> items;

    private int inventoryWidth = 9;
    private int inventoryHeight = 3;

    public BaseCompactStorageBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        items = NonNullList.withSize(inventoryWidth * inventoryHeight, ItemStack.EMPTY);
    }
    public static void ticker(Level level, BlockPos blockPos, BlockState blockState, BaseCompactStorageBlockEntity itemStacks) {

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
            newList.addAll(this.items);
            this.items = newList;
        }
    }
}
