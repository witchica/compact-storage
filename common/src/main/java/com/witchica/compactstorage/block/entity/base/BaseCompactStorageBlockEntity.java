package com.witchica.compactstorage.block.entity.base;

import com.mojang.serialization.Codec;
import com.witchica.compactstorage.api.inventory.UpgradeCheckProvider;
import com.witchica.compactstorage.api.inventory.VoidSlotProvider;
import com.witchica.compactstorage.block.base.BaseCompactStorageBlock;
import com.witchica.compactstorage.components.ResizableInventoryComponent;
import com.witchica.compactstorage.menu.CompactStorageMenuData;
import com.witchica.compactstorage.menu.GenericCompactStorageMenu;
import com.witchica.compactstorage.api.inventory.RetainingContainer;
import com.witchica.compactstorage.mod.CompactStorageComponents;
import com.witchica.compactstorage.util.CompactStorageContainerOpenerCounter;
import com.witchica.compactstorage.util.CompactStorageUtil;
import net.blay09.mods.balm.world.BalmMenuProvider;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
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
import org.jspecify.annotations.Nullable;

import java.util.List;

public abstract class BaseCompactStorageBlockEntity extends BaseContainerBlockEntity implements BalmMenuProvider<@NotNull CompactStorageMenuData>, ResizableContainer, RetainingContainer, VoidSlotProvider, UpgradeCheckProvider {
    private NonNullList<ItemStack> items;

    private int inventoryWidth ;
    private int inventoryHeight;
    private boolean needsToBeRetaining;
    private boolean hasVoidSlotUpgrade;
    protected final ContainerOpenersCounter containerOpenersCounter;

    public BaseCompactStorageBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);

        this.inventoryWidth = getDefaultWidth();
        this.inventoryHeight = getDefaultHeight();

        items = NonNullList.withSize(inventoryWidth * inventoryHeight, ItemStack.EMPTY);
        this.containerOpenersCounter = new CompactStorageContainerOpenerCounter(this);
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
        level.playSound((Entity)null, d0, d1, d2, sound, SoundSource.BLOCKS, 0.5F, level.getRandom().nextFloat() * 0.1F + 0.9F);
    }

    public void signalOpenCount(Level level, BlockPos pos, BlockState state, int eventId, int eventParam) {
        Block block = state.getBlock();
        level.blockEvent(this.getBlockPos(), block, 2, eventParam);
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
        writer.store("VoidSlotUpgrade", Codec.BOOL, hasVoidSlot());
        writer.store("Version", Codec.INT,21);
        ContainerHelper.saveAllItems(writer, items);
    }

    @Override
    protected void loadAdditional(ValueInput reader) {
        super.loadAdditional(reader);

        // -1 for anything before 26.1, 21 for 26.1, useful for any data changes
        int version = reader.getIntOr("Version", -1);

        this.inventoryWidth = reader.getIntOr("InventoryWidth", getDefaultWidth());
        this.inventoryHeight = reader.getIntOr("InventoryHeight", getDefaultHeight());
        this.hasVoidSlotUpgrade = reader.getBooleanOr("VoidSlotUpgrade", false);
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
    public void setSize(int width, int height) {
        this.inventoryWidth = width;
        this.inventoryHeight = height;
        resizeInventory();
    }

    @Override
    public void setRetaining(boolean retaining) {
        level.setBlock(getBlockPos(), getBlockState().setValue(BaseCompactStorageBlock.RETAINING, retaining), 2);
    }

    @Override
    public boolean isRetaining() {
        return getBlockState().getValue(BaseCompactStorageBlock.RETAINING);
    }

    @Override
    public void startOpen(ContainerUser user) {
        if(!this.remove && !user.getLivingEntity().isSpectator()) {
            this.containerOpenersCounter.incrementOpeners(user.getLivingEntity(), this.getLevel(), this.getBlockPos(), this.getBlockState(), user.getContainerInteractionRange());
        }
    }

    @Override
    public void stopOpen(ContainerUser user) {
        if(!this.remove && !user.getLivingEntity().isSpectator()) {
            this.containerOpenersCounter.decrementOpeners(user.getLivingEntity(), this.getLevel(), this.getBlockPos(), this.getBlockState());
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

    @Override
    protected void applyImplicitComponents(DataComponentGetter dataComponentGetter) {
        super.applyImplicitComponents(dataComponentGetter);
        setRetaining(dataComponentGetter.getOrDefault(CompactStorageComponents.RETAINING_DATA.value(), false).booleanValue());

        ResizableInventoryComponent resizableInventoryComponent = dataComponentGetter.get(CompactStorageComponents.RESIZABLE_INVENTORY_DATA.value());
        if(resizableInventoryComponent != null) {
            resizableInventoryComponent.apply(this);
        }

        setHasVoidSlot(dataComponentGetter.getOrDefault(CompactStorageComponents.VOID_SLOT.value(), false).booleanValue());
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder dataComponentMap) {
        super.collectImplicitComponents(dataComponentMap);
        dataComponentMap.set(CompactStorageComponents.RETAINING_DATA.value(), this.isRetaining());
        dataComponentMap.set(CompactStorageComponents.RESIZABLE_INVENTORY_DATA.value(), new ResizableInventoryComponent(this.getWidth(), this.getHeight()));
        dataComponentMap.set(CompactStorageComponents.VOID_SLOT.value(), this.hasVoidSlot());
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        if(!state.getValue(BaseCompactStorageBlock.RETAINING)) {
            Containers.dropContents(level, pos, this);
            clearContent();
        }
    }

    @Override
    public void removeComponentsFromTag(ValueOutput tag) {
        super.removeComponentsFromTag(tag);
        tag.discard("InventoryWidth");
        tag.discard("InventoryHeight");
        tag.discard("VoidSlotUpgrade");
    }

    @Override
    public boolean hasVoidSlot() {
        return this.hasVoidSlotUpgrade;
    }

    @Override
    public void setHasVoidSlot(boolean hasVoidSlot) {
        this.hasVoidSlotUpgrade = hasVoidSlot;
        setChanged();
    }
}
