package me.tobystrong.compactstorage.block.tile;

import me.tobystrong.compactstorage.CompactStorage;
import me.tobystrong.compactstorage.container.CompactChestContainer;
import me.tobystrong.compactstorage.util.CompactStorageUtilMethods;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.*;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

/*
Only include on the client size
 */
@OnlyIn(
        value = Dist.CLIENT,
        _interface = IChestLid.class
)
public class CompactChestTileEntity extends BaseTileEntity implements IChestLid {
    public float lidAngle = 0f;
    public float prevLidAngle = 0f;

    public CompactChestTileEntity() {
        super(CompactStorage.COMPACT_CHEST_TILE_TYPE);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.compactstorage.compact_chest");
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
        // checks that the size matches before opening the container so we don't throw an exception
        inventory = CompactStorageUtilMethods.validateHandlerSize(inventory, width, height);
        return new CompactChestContainer(windowId, playerInventory, width, height, this, inventory.orElseThrow(NullPointerException::new));
    }

    @Override
    public void tick() {
        super.tick();
        // sets the lid angle for animation
        this.prevLidAngle = lidAngle;

        if(playersUsing > 0 && lidAngle == 0f) {
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 1f, 1f, false);
        }

        if (playersUsing == 0 && this.lidAngle > 0.0F || playersUsing > 0 && this.lidAngle < 1.0F) {
            float f1 = this.lidAngle;
            if (playersUsing > 0) {
                this.lidAngle += 0.1F;
            } else {
                this.lidAngle -= 0.1F;
            }

            if (this.lidAngle > 1.0F) {
                this.lidAngle = 1.0F;
            }

            float f2 = 0.5F;
            if (this.lidAngle < 0.5F && f1 >= 0.5F) {
                world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 1f, 1f, false);
            }

            if (this.lidAngle < 0.0F) {
                this.lidAngle = 0.0F;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getLidAngle(float partialTicks) {
        return MathHelper.lerp(partialTicks, prevLidAngle, lidAngle);
    }
}
