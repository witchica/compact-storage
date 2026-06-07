package com.witchica.compactstorage.util;

import com.witchica.compactstorage.api.RedyeableBlock;
import com.witchica.compactstorage.data.StorageType;
import com.witchica.compactstorage.data.StorageUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;

public class CompactStorageUtil {
    public static void loadItemsFromOldVersionIfPresent(ValueInput input, NonNullList<ItemStack> items) {
        ValueInput.TypedInputList<CompoundTag> targetItemTag = input.listOrEmpty("Items", CompoundTag.CODEC);

        if(targetItemTag.isEmpty()) {
            targetItemTag = input.listOrEmpty("Inventory", CompoundTag.CODEC);
        }

        if(!targetItemTag.isEmpty()) {
            CompoundTag fakeInventory = new CompoundTag();
            ListTag newList = new ListTag();
            for(CompoundTag subtag : targetItemTag) {
                // change of case here, annoying
                subtag.putInt("count", (int) subtag.getByteOr("Count", (byte) 1));
                // remove the old one
                subtag.remove("Count");
                newList.add(subtag);
            }

            // Create new tag that the actual loading system can just use off rip
            fakeInventory.put("Items", newList);

            ValueInput fakeInventoryInput = TagValueInput.create(ProblemReporter.DISCARDING, input.lookup(), fakeInventory);
            ContainerHelper.loadAllItems(fakeInventoryInput, items);
        }
    }

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
