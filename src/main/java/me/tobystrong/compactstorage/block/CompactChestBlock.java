package me.tobystrong.compactstorage.block;

import me.tobystrong.compactstorage.block.tile.CompactChestTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
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

        INamedContainerProvider namedContainerProvider = this.getContainer(state, worldIn, pos);
        if(namedContainerProvider != null) {
            player.sendMessage(new StringTextComponent("container not null"), UUID.randomUUID());
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
            NetworkHooks.openGui(serverPlayerEntity, namedContainerProvider, (packetBuffer -> {
                //write the chest size here !
                packetBuffer.writeInt(9);
                packetBuffer.writeInt(3);
            }));
        } else {
            player.sendMessage(new StringTextComponent("container  null"), UUID.randomUUID());
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
