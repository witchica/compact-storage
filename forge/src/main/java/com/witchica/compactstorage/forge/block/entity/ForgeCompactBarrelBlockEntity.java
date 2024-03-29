package com.witchica.compactstorage.forge.block.entity;

import com.witchica.compactstorage.common.block.CompactBarrelBlock;
import com.witchica.compactstorage.common.block.entity.CompactBarrelBlockEntity;
import com.witchica.compactstorage.common.block.entity.CompactChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;

public class ForgeCompactBarrelBlockEntity extends CompactBarrelBlockEntity {
    private final LazyOptional<IItemHandler> inventoryWrapper;
    public ForgeCompactBarrelBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
        this.inventoryWrapper = LazyOptional.of(() -> new InvWrapper(this.getInventory()));
    }

    @Override
    public void resizeInventory(boolean copy_contents) {
        super.resizeInventory(copy_contents);
        this.invalidateCaps();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return inventoryWrapper.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.inventoryWrapper.invalidate();
    }
}
