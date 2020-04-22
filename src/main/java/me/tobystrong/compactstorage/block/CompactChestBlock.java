package me.tobystrong.compactstorage.block;

import me.tobystrong.compactstorage.CompactStorage;
import me.tobystrong.compactstorage.block.entity.CompactChestBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class CompactChestBlock extends ContainerBlock {
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
    public static final VoxelShape CHEST_SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);
    
    public CompactChestBlock(Properties settings) {
        super(settings);
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        return getDefaultState().with(FACING, ctx.getPlacementHorizontalFacing().getOpposite());
    }


    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new CompactChestBlockEntity();
    }


    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        //if the item has a custom name then name the entity the same name

        if(itemStack.hasTag() && itemStack.getTag().contains("inventory_width") && itemStack.getTag().contains("inventory_height") && !world.isRemote) {
            //get the entity
            TileEntity blockEntity = world.getTileEntity(pos);

            //cast and set the name
            if(blockEntity instanceof  CompactChestBlockEntity) {
                ((CompactChestBlockEntity) blockEntity).inventory_width = itemStack.getTag().getInt("inventory_width");
                ((CompactChestBlockEntity) blockEntity).inventory_height = itemStack.getTag().getInt("inventory_height");
                ((CompactChestBlockEntity) blockEntity).resizeInventory(false);
                blockEntity.markDirty();
            }
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if(!world.isRemote) {
            TileEntity blockEntity = world.getTileEntity(pos);

            if(blockEntity instanceof CompactChestBlockEntity) {
                CompactChestBlockEntity compactChestBlockEntity = (CompactChestBlockEntity) blockEntity;
                Item held_item = player.getHeldItem(hand).getItem();

                if(held_item == CompactStorage.CHEST_UPGRADE_ROW && compactChestBlockEntity.inventory_width < 24) {
                    compactChestBlockEntity.inventory_width += 1;
                    player.getHeldItem(hand).shrink(1);
                    player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1f, 1f);

                    compactChestBlockEntity.resizeInventory(true);
                    compactChestBlockEntity.markDirty();

                    return ActionResultType.SUCCESS;
                } else if (held_item == CompactStorage.CHEST_UPGRADE_ROW && compactChestBlockEntity.inventory_width >= 24) {
                    player.sendMessage(new TranslationTextComponent("compact-storage.text.too_many_rows"));
                }

                if(held_item == CompactStorage.CHEST_UPGRADE_COLUMN && compactChestBlockEntity.inventory_height < 12) {
                    compactChestBlockEntity.inventory_height += 1;
                    player.getHeldItem(hand).shrink(1);
                    player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1f, 1f);

                    compactChestBlockEntity.resizeInventory(true);
                    compactChestBlockEntity.markDirty();

                    return ActionResultType.SUCCESS;
                } else if (held_item == CompactStorage.CHEST_UPGRADE_COLUMN && compactChestBlockEntity.inventory_height >= 12) {
                    player.sendMessage(new TranslationTextComponent("compact-storage.text.too_many_columns"));
                }

                //ContainerProviderRegistry.INSTANCE.openContainer(CompactStorage.COMPACT_CHEST_IDENTIFIER, player, buf -> buf.writeBlockPos(pos))
                NetworkHooks.openGui((ServerPlayerEntity) player, getContainer(state, world, pos), new Consumer<PacketBuffer>() {
                    @Override
                    public void accept(PacketBuffer packetBuffer) {
                        packetBuffer.writeBlockPos(pos);
                    }
                });
            }
        }

        return ActionResultType.SUCCESS;
    }

    @Override
    @Nullable
    public INamedContainerProvider getContainer(BlockState state, World world, BlockPos pos) {
        TileEntity tileentity = world.getTileEntity(pos);
        return tileentity instanceof INamedContainerProvider ? (INamedContainerProvider) tileentity : null;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ctx) {
        return CHEST_SHAPE;
    }


    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        //if the block is actually gone
        if(state.getBlock() != newState.getBlock()) {
            TileEntity entity = world.getTileEntity(pos);

            //and is our block then scatter items and update comparators
            if(entity instanceof  CompactChestBlockEntity) {
                CompactChestBlockEntity chestBlockEntity = (CompactChestBlockEntity) entity;
                InventoryHelper.dropInventoryItems(world, pos, chestBlockEntity.getInventory());
                world.updateComparatorOutputLevel(pos, this);
            }
        }
        super.onReplaced(state, world, pos, newState, moved);
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState p_149740_1_) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
        return Container.calcRedstone(world.getTileEntity(pos));
    }
}
