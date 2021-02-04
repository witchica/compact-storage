package me.tobystrong.compactstorage.block.tile;

import me.tobystrong.compactstorage.CompactStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

public class CompactBarrelTileEntity extends TileEntity implements INamedContainerProvider, ICapabilitySerializable<CompoundNBT>, ITickableTileEntity {
    private LazyOptional<ItemStackHandler> inventory;
    public int width = 9;
    public int height = 3;

    public int playersUsing = 0;

    public CompactBarrelTileEntity() {
        super(CompactStorage.COMPACT_BARREL_TILE_TYPE);
        inventory = LazyOptional.of(() -> new ItemStackHandler(9 * 3));
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.compactstorage.barrel");
    }

    @Override
    public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return null;
    }

    @Override
    public void tick() {

    }

    /*
        Checks if the player is within a 5 block radius of the chest
     */
    public boolean canPlayerAccess(PlayerEntity entity) {
        if(entity.getPosX() > pos.getX() - 5 && entity.getPosY() > pos.getY() - 5 && entity.getPosZ() > pos.getZ() - 5) {
            if(entity.getPosX() < pos.getX() + 5 && entity.getPosY() < pos.getY() + 5 && entity.getPosZ() < pos.getZ() + 5) {
                return true;
            }
        }

        return false;
    }
}
