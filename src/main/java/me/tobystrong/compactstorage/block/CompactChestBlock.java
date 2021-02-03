package me.tobystrong.compactstorage.block;

import me.tobystrong.compactstorage.CompactStorage;
import me.tobystrong.compactstorage.block.tile.CompactChestTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.audio.Sound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.UUID;

public class CompactChestBlock extends ContainerBlock {
    public static EnumProperty<Direction> PROPERTY_FACING = EnumProperty.create("facing", Direction.class, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);
    public CompactChestBlock() {
        super(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(2f, 5f).harvestLevel(1).harvestTool(ToolType.PICKAXE).notSolid().sound(SoundType.METAL));
        setDefaultState(this.stateContainer.getBaseState().
                with(PROPERTY_FACING, Direction.NORTH)
        );
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        }

        ItemStack heldItem = player.getHeldItem(handIn);

        if(heldItem.getItem() == CompactStorage.upgrade_column || heldItem.getItem() == CompactStorage.upgrade_row) {
            TileEntity entity = worldIn.getTileEntity(pos);

            if(entity != null && worldIn instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) worldIn;
                CompactChestTileEntity chestTile = (CompactChestTileEntity) entity;
                CompactChestTileEntity.UpgradeStatus status = chestTile.handleUpgradeItem(heldItem);

                if(status == CompactChestTileEntity.UpgradeStatus.SUCCESS) {
                    serverWorld.spawnParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 5,0.25,0, 0.25, 0);
                    worldIn.playSound(null, pos, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 0.25f, 1f);
                    worldIn.notifyBlockUpdate(pos, state, state, 2);
                    player.getHeldItem(handIn).setCount(heldItem.getCount() - 1);
                } else {
                    serverWorld.spawnParticle(ParticleTypes.ANGRY_VILLAGER, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 5,0.25,0, 0.25, 0);
                    worldIn.playSound(null, pos, SoundEvents.UI_TOAST_OUT, SoundCategory.BLOCKS, 0.25f, 1f);
                    player.sendMessage(new TranslationTextComponent("message.compactstorage.upgrade." + status.name().toLowerCase()), UUID.randomUUID());
                }

                return ActionResultType.SUCCESS;
            }
        } else {
            INamedContainerProvider namedContainerProvider = this.getContainer(state, worldIn, pos);
            if (namedContainerProvider != null) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
                NetworkHooks.openGui(serverPlayerEntity, namedContainerProvider, (packetBuffer -> {
                    //write the chest size here !
                    packetBuffer.writeBlockPos(pos);
                }));
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(PROPERTY_FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(PROPERTY_FACING);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return createNewTileEntity(world);
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new CompactChestTileEntity();
    }
}
