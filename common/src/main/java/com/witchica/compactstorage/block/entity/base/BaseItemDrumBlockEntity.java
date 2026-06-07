package com.witchica.compactstorage.block.entity.base;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.api.inventory.ResizableItemDrum;
import com.witchica.compactstorage.data.StorageUpgrade;
import com.witchica.compactstorage.api.inventory.RetainingContainer;
import com.witchica.compactstorage.api.inventory.UpgradeCheckProvider;
import com.witchica.compactstorage.block.base.BaseCompactStorageBlock;
import com.witchica.compactstorage.inventory.DrumInventory;
import com.witchica.compactstorage.mod.CompactStorageBlockEntities;
import com.witchica.compactstorage.mod.CompactStorageComponents;
import com.witchica.compactstorage.mod.CompactStorageUpgrades;
import com.witchica.compactstorage.util.CompactStorageUtil;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class BaseItemDrumBlockEntity extends BlockEntity implements RetainingContainer, UpgradeCheckProvider, ResizableItemDrum {
    private DrumInventory drumInventory;
    public Optional<ItemStack> clientItem = Optional.empty();
    public int clientStackSize;
    public int clientStoredItems;
    private boolean needsToBeRetaining;

    public BaseItemDrumBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CompactStorageBlockEntities.DRUM_BLOCK_ENTITY.value(), blockPos, blockState);
        drumInventory = new DrumInventory(getDefaultSize(), this);
    }

    public void inventoryChanged() {
        this.setChanged();
    }


    public boolean hasAnyItems() {
        return !drumInventory.getItemType().isEmpty();
    }

    public Item getStoredType() {
        return drumInventory.getItemType().getItem();
    }

    public int getTotalItemCount() {
        return hasAnyItems() ? drumInventory.countItem(getStoredType()) : 0;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        ContainerHelper.saveAllItems(output, drumInventory.getItems());

        if(getStoredType() != Items.AIR) {
            output.store("ClientItem", ItemStack.CODEC, new ItemStack(getStoredType(), 1));
        }

        output.putInt("ItemDrumSize", getSize());
        output.putInt("ClientStackSize", drumInventory.getMaxStackSize());
        output.putInt("ClientStoredItems", getTotalItemCount());
        output.putInt("Version", 21);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        int version = input.getIntOr("Version", -1);

        this.drumInventory = new DrumInventory(input.getIntOr("ItemDrumSize", getDefaultSize()), this);

        if(version < 21) {
            // Backwards Compatibility
            CompactStorageUtil.loadItemsFromOldVersionIfPresent(input, drumInventory.getItems());
        } else {
            // New way
            ContainerHelper.loadAllItems(input, drumInventory.getItems());
        }

        this.clientItem = input.read("ClientItem", ItemStack.CODEC);
        this.clientStackSize = input.getIntOr("ClientStackSize", 0);
        this.clientStoredItems = input.getIntOr("ClientStoredItems", 0);
    }

    @Override
    public void removeComponentsFromTag(ValueOutput output) {
        super.removeComponentsFromTag(output);
        output.discard("ItemDrumSize");
        output.discard("Items");
        output.discard("ClientItem");
        output.discard("ClientStackSize");
        output.discard("ClientStoredItems");
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return BalmBlockEntityUtils.createUpdatePacket(this);
    }

    public DrumInventory getDrumInventory() {
        return drumInventory;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if(level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }

    }

    @Override
    public boolean isRetaining() {
        return getBlockState().getValue(BaseCompactStorageBlock.RETAINING);
    }

    @Override
    public void setRetaining(boolean retaining) {
        level.setBlock(getBlockPos(), getBlockState().setValue(BaseCompactStorageBlock.RETAINING, retaining), 2);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(CompactStorageComponents.RETAINING_DATA.value(), isRetaining());
        components.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(getDrumInventory().getItems()));
        components.set(CompactStorageComponents.ITEM_DRUM_SIZE.value(), getSize());
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter componentGetter) {
        super.applyImplicitComponents(componentGetter);
        setRetaining(componentGetter.getOrDefault(CompactStorageComponents.RETAINING_DATA.value(), false));
        setSize(componentGetter.getOrDefault(CompactStorageComponents.ITEM_DRUM_SIZE.value(), getDefaultSize()));
        componentGetter.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(getDrumInventory().getItems());
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        if(!state.getValue(BaseCompactStorageBlock.RETAINING)) {
            Containers.dropContents(level, pos, drumInventory);
            drumInventory.clearContent();
        }
    }

    @Override
    public boolean isUpgradeAccepted(StorageUpgrade upgrade) {
        return upgrade == CompactStorageUpgrades.RETAINING_UPGRADE || upgrade == CompactStorageUpgrades.ITEM_DRUM_UPGRADE;
    }

    @Override
    public int getDefaultSize() {
        return Math.clamp(CompactStorage.config().itemDrumDefaultSize, 1, CompactStorage.config().itemDrumMaximumSize);
    }

    @Override
    public int getMaximumSize() {
        return Math.clamp(CompactStorage.config().itemDrumMaximumSize, CompactStorage.config().itemDrumDefaultSize, 256);
    }

    @Override
    public int getSize() {
        return drumInventory.getItems().size();
    }

    @Override
    public void setSize(int size) {
        NonNullList<ItemStack> items = drumInventory.getItems();
        this.drumInventory = new DrumInventory(size, this);

        for(int i = 0; i < items.size(); i++) {
            this.drumInventory.setItem(i, items.get(i));
        }

        setChanged();
    }
}
