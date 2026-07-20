package com.witchica.compactstorage.util;

import com.witchica.compactstorage.api.RedyeableBlock;
import com.witchica.compactstorage.data.StorageType;
import com.witchica.compactstorage.data.StorageUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CompactStorageUtil {
    public static InteractionResult onUseWithItem(ItemStack stack, Level level, BlockPos pos, BlockState state, Player player, StorageType storageType, RedyeableBlock redyeableBlock) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        InteractionResult upgradeResult = StorageUpgrade.applyToBlockEntity(stack, blockEntity, player, level);

        if(upgradeResult != InteractionResult.PASS) {
            return upgradeResult;
        }

        if (redyeableBlock != null && storageType.canDye() && stack.getItem() instanceof DyeItem) {
            StorageType newType = StorageType.fromDye(stack.get(DataComponents.DYE));

            if(newType != storageType) {
                level.setBlock(pos, redyeableBlock.getBlockStateOnRedye(state, newType.getDyeColor()), 3);
                stack.setCount(stack.getCount() - 1);
                level.playSound(null, pos, SoundEvents.SLIME_SQUISH, SoundSource.BLOCKS, 1f, 1f);
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }
}
