package me.tobystrong.compactstorage.block.tile;

import me.tobystrong.compactstorage.CompactStorage;
import me.tobystrong.compactstorage.block.CompactBarrelBlock;
import me.tobystrong.compactstorage.container.CompactBarrelContainer;
import me.tobystrong.compactstorage.util.CompactStorageUtilMethods;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CompactBarrelTileEntity extends BaseTileEntity {
    public int lastPlayersUsing = 0;

    public CompactBarrelTileEntity() {
        super(CompactStorage.COMPACT_BARREL_TILE_TYPE);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.compactstorage.barrel");
    }

    @Override
    public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        inventory = CompactStorageUtilMethods.validateHandlerSize(inventory, width, height);
        return new CompactBarrelContainer(windowID, playerInventory, width, height, this, inventory.orElseThrow(NullPointerException::new));
    }

    @Override
    public void tick() {
        super.tick();
        boolean flag = world.getBlockState(pos).get(CompactBarrelBlock.PROPERTY_OPEN);

        if(playersUsing > 0 && lastPlayersUsing == 0 && !flag) {
            world.setBlockState(pos, getBlockState().with(CompactBarrelBlock.PROPERTY_OPEN, true));
            world.playSound(null, pos, SoundEvents.BLOCK_BARREL_OPEN, SoundCategory.BLOCKS, 1f, 1f);
        } else if(playersUsing == 0 && lastPlayersUsing != 0 && flag) {
            world.setBlockState(pos, getBlockState().with(CompactBarrelBlock.PROPERTY_OPEN, false));
            world.playSound(null, pos, SoundEvents.BLOCK_BARREL_CLOSE, SoundCategory.BLOCKS, 1f, 1f);
        }

        lastPlayersUsing = playersUsing;
    }
}
