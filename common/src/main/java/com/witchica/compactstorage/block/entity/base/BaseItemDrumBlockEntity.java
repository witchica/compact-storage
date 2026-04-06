package com.witchica.compactstorage.block.entity.base;

import com.mojang.serialization.Codec;
import com.witchica.compactstorage.api.inventory.RetainingContainer;
import com.witchica.compactstorage.api.inventory.UpgradableContainer;
import com.witchica.compactstorage.block.base.BaseItemDrumBlock;
import com.witchica.compactstorage.data.UpgradeType;
import com.witchica.compactstorage.inventory.DrumInventory;
import com.witchica.compactstorage.mod.CompactStorageBlockEntities;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class BaseItemDrumBlockEntity extends BlockEntity implements UpgradableContainer, RetainingContainer {
    private DrumInventory drumInventory;
    public Optional<ItemStack> clientItem = Optional.empty();
    public int clientStackSize;
    public int clientStoredItems;
    private boolean retaining;

    public BaseItemDrumBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CompactStorageBlockEntities.DRUM_BLOCK_ENTITY.value(), blockPos, blockState);
        drumInventory = new DrumInventory(64, this);
    }

    public void tick() {

    }

    public static void ticker(Level level, BlockPos blockPos, BlockState blockState, BaseItemDrumBlockEntity baseItemDrumBlockEntity) {
        baseItemDrumBlockEntity.tick();
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

        output.putInt("ClientStackSize", drumInventory.getMaxStackSize());
        output.putInt("ClientStoredItems", getTotalItemCount());

        output.store("Retaining", Codec.BOOL, isRetaining());
        output.store("Version", Codec.INT,2);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        int storedVersion = input.getIntOr("Version", 0);

        switch(storedVersion) {
            case 2: {
                ContainerHelper.loadAllItems(input, drumInventory.getItems());
                break;
            } default: {
                for(ItemStackWithSlot itemstackwithslot : input.listOrEmpty("Inventory", ItemStackWithSlot.CODEC)) {
                    if (itemstackwithslot.isValidInContainer(drumInventory.getItems().size())) {
                        drumInventory.setItem(itemstackwithslot.slot(), itemstackwithslot.stack());
                    }
                }
                break;
            }
        }

        this.clientItem = input.read("ClientItem", ItemStack.CODEC);
        this.clientStackSize = input.getIntOr("ClientStackSize", 0);
        this.clientStoredItems = input.getIntOr("ClientStoredItems", 0);
        this.retaining = input.getBooleanOr("Retaining", false);

        checkAndApplyRetainingState();
    }

    protected void checkAndApplyRetainingState() {
        if(retaining != getBlockState().getValue(BaseItemDrumBlock.RETAINING)) {
            getLevel().setBlock(getBlockPos(), getBlockState().setValue(BaseItemDrumBlock.RETAINING, isRetaining()), 2);
        }
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
    public boolean canApplyUpgrade(UpgradeType upgradeType) {
        return upgradeType == UpgradeType.RETAINING_UPGRADE && !isRetaining();
    }

    @Override
    public boolean applyUpgrade(UpgradeType upgradeType) {
        if(upgradeType == UpgradeType.RETAINING_UPGRADE) {
            if(!this.retaining) {
                setRetaining(true);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isRetaining() {
        return this.retaining;
    }

    @Override
    public void setRetaining(boolean retaining) {
        this.retaining = retaining;
        checkAndApplyRetainingState();
    }
}
