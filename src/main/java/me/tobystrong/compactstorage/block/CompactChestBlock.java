package me.tobystrong.compactstorage.block;

import me.tobystrong.compactstorage.CompactStorage;
import me.tobystrong.compactstorage.block.entity.CompactChestBlockEntity;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.Container;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CompactChestBlock extends BlockWithEntity {
    public CompactChestBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return new CompactChestBlockEntity();
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        //if the item has a custom name then name the entity the same name
        if(itemStack.hasCustomName()) {
            //get the entity
            BlockEntity blockEntity = world.getBlockEntity(pos);

            //cast and set the name
            if(blockEntity instanceof  CompactChestBlockEntity) {
                ((CompactChestBlockEntity) blockEntity).setCustomName(itemStack.getName());
            }
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if(blockEntity instanceof  CompactChestBlockEntity) {
                ContainerProviderRegistry.INSTANCE.openContainer(CompactStorage.COMPACT_CHEST_IDENTIFIER, player, buf -> buf.writeBlockPos(pos));
            }
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        //if the block is actually gone
        if(state.getBlock() != newState.getBlock()) {
            BlockEntity entity = world.getBlockEntity(pos);

            //and is our block then scatter items and update comparators
            if(entity instanceof  CompactChestBlockEntity) {
                CompactChestBlockEntity chestBlockEntity = (CompactChestBlockEntity) entity;
                ItemScatterer.spawn(world, pos, (Inventory) chestBlockEntity);
                world.updateHorizontalAdjacent(pos, this);
            }
        }
        super.onBlockRemoved(state, world, pos, newState, moved);
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return Container.calculateComparatorOutput(world.getBlockEntity(pos));
    }
}
