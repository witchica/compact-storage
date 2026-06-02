package com.witchica.compactstorage.block.base;

import com.witchica.compactstorage.api.StorageTypeProvider;
import com.witchica.compactstorage.api.inventory.UpgradableContainer;
import com.witchica.compactstorage.block.entity.base.BaseCompactStorageBlockEntity;
import com.witchica.compactstorage.data.StorageType;
import com.witchica.compactstorage.data.UpgradeType;
import com.witchica.compactstorage.item.StorageUpgradeItem;
import net.blay09.mods.balm.Balm;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public abstract class BaseCompactStorageBlock extends BaseEntityBlock implements StorageTypeProvider {
    public static final BooleanProperty RETAINING = BooleanProperty.create("retaining");

    private final StorageType storageType;

    protected BaseCompactStorageBlock(StorageType storageType, Properties properties) {
        super(properties);
        this.storageType = storageType;
    }

    @Override
    public @NotNull StorageType getStorageType() {
        return storageType;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(!level.isClientSide() && canOpenContainer(state, level, pos, player)) {
            Balm.networking().openMenu(player, getMenuProvider(state, level, pos));
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.CONSUME;
    }

    protected boolean canOpenContainer(BlockState state, Level level, BlockPos pos, Player player) {
        return true;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(!level.isClientSide()) {
            if(stack.getItem() instanceof StorageUpgradeItem upgradeItem) {
                UpgradeType upgradeType = upgradeItem.getUpgradeType();
                if(level.getBlockEntity(pos) instanceof UpgradableContainer upgradableContainer) {
                    boolean applied = upgradableContainer.applyUpgrade(upgradeType);

                    if(applied) {
                        stack.setCount(stack.getCount() - 1);
                        level.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 1f, 1f);
                        return InteractionResult.CONSUME;
                    } else {
                        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1f, 1f);
                        player.displayClientMessage(upgradeItem.getUpgradeType().upgradeFailMessage(), true);
                        return InteractionResult.FAIL;
                    }
                }
            } else if (storageType.canDye() && stack.getItem() instanceof DyeItem dyeItem) {
                StorageType newType = StorageType.fromDye(dyeItem.getDyeColor());

                if(newType != storageType) {
                    level.setBlock(pos, getBlockStateOnRedye(state, dyeItem), 3);
                    stack.setCount(stack.getCount() - 1);
                    level.playSound(null, pos, SoundEvents.SLIME_SQUISH, SoundSource.BLOCKS, 1f, 1f);
                    return InteractionResult.CONSUME;
                }
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    protected abstract BlockState getBlockStateOnRedye(BlockState state, DyeItem dyeItem);

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.tick(state, level, pos, random);

        if(level.getBlockEntity(pos) instanceof BaseCompactStorageBlockEntity baseCompactStorageBlockEntity) {
            baseCompactStorageBlockEntity.recheckOpen();
        }
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
    }

    @Override
    protected boolean shouldChangedStateKeepBlockEntity(BlockState state) {
        return state.getBlock() instanceof BaseCompactStorageBlock;
    }
}
