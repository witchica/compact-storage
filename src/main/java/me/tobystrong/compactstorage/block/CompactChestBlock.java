package me.tobystrong.compactstorage.block;

import com.sun.org.apache.xpath.internal.operations.String;
import me.tobystrong.compactstorage.CompactStorage;
import me.tobystrong.compactstorage.block.tile.CompactChestTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.*;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.xml.soap.Text;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CompactChestBlock extends ContainerBlock implements IWaterLoggable {
    public static EnumProperty<Direction> PROPERTY_FACING = EnumProperty.create("facing", Direction.class, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);

    public CompactChestBlock() {
        super(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(2f, 5f).harvestLevel(1).harvestTool(ToolType.PICKAXE).notSolid().sound(SoundType.METAL));
        setDefaultState(this.stateContainer.getBaseState().
                with(PROPERTY_FACING, Direction.NORTH)
                .with(WATERLOGGED, false)
        );
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        }

        if(worldIn.getBlockState(pos.add(0, 1, 0)).isSolid()) {
            return ActionResultType.FAIL;
        }

        ItemStack heldItem = player.getHeldItem(handIn);

        /*
            Handles the upgrades by using the TileEntity to check if upgradable, then uses the status to do the following
            SUCESS = spawn the particles and play the sound, then notify of block update as tile entity handles the change itself, decrements the stack size
            MAX_WIDTH, MAX_HEIGHT - displays particles and plays sound as well as displays a message
         */
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

            /*
                This handles the dyeing of chests in world.
                We get the current dye item and the colour from it, retrieve the right chest and change the block state
             */
        } else if(player.getHeldItem(handIn).getItem() instanceof DyeItem) {
            ServerWorld serverWorld = (ServerWorld) worldIn;

            DyeItem item = (DyeItem) player.getHeldItem(handIn).getItem();
            Direction dir = state.get(PROPERTY_FACING);
            boolean waterlogged = state.get(WATERLOGGED);
            Block newBlock = CompactStorage.chest_blocks[item.getDyeColor().getId()];

            //if the dyed block is not the same colour
            if(newBlock != this) {
                //get the new block and set the state with a client update (flag 2)
                BlockState newState = CompactStorage.chest_blocks[item.getDyeColor().getId()].getDefaultState().with(PROPERTY_FACING, dir).with(WATERLOGGED, waterlogged);
                worldIn.setBlockState(pos, newState, 2);

                //play a sound and decrement stack size
                serverWorld.playSound(null, pos, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                player.getHeldItem(handIn).setCount(player.getHeldItem(handIn).getCount() - 1);
            }
        } else {
            //get the container provider and open the GUI and container for this block
            INamedContainerProvider namedContainerProvider = this.getContainer(state, worldIn, pos);
            if (namedContainerProvider != null) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
                NetworkHooks.openGui(serverPlayerEntity, namedContainerProvider, (packetBuffer -> {
                    //write the chest pos here !
                    packetBuffer.writeBlockPos(pos);
                }));
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        FluidState fluidstate = context.getWorld().getFluidState(context.getPos());
        //gets the player direction and flips it for placement facing the player
        return getDefaultState().with(PROPERTY_FACING, context.getPlacementHorizontalFacing().getOpposite()).with(WATERLOGGED, fluidstate.getFluid() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(PROPERTY_FACING);
        builder.add(WATERLOGGED);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
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

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        //if not being replaced by another chest (aka dyed)
        if(!(newState.getBlock() instanceof CompactChestBlock)) {
            if (!state.isIn(newState.getBlock())) {
                //get the tile entity
                TileEntity tile = worldIn.getTileEntity(pos);

                if(tile instanceof CompactChestTileEntity) {
                    CompactChestTileEntity chestTile = (CompactChestTileEntity) tile;
                    ItemStack chestItem = new ItemStack(state.getBlock(), 1);

                    //this creates a block with the right NBT to keep the upgrades that have been applied

                    if(!(chestTile.width == 9 && chestTile.width == 3)) {
                        CompoundNBT tag = chestItem.getOrCreateChildTag("BlockEntityTag");
                        tag.putInt("width", chestTile.width);
                        tag.putInt("height", chestTile.height);
                    }

                    //make a list of all the chest contents using the capability

                    NonNullList<ItemStack> drops = NonNullList.create();
                    IItemHandler itemHandler = chestTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(NullPointerException::new);

                    for(int i = 0; i < itemHandler.getSlots(); i++) {
                        drops.add(itemHandler.getStackInSlot(i));
                    }

                    drops.add(chestItem);

                    //drop all the items in the world
                    InventoryHelper.dropItems(worldIn, pos, drops);
                }
            }

            //remove the tile entity
            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public void addInformation(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        int width = 9;
        int height = 3;
        boolean upgraded = false;
        boolean retaining = false;

        //this is used to store the data for placement eg: width and height
        if(stack.hasTag() && stack.getChildTag("BlockEntityTag") != null) {
            upgraded = true;

            CompoundNBT tag = stack.getChildTag("BlockEntityTag");

            //if we don't have a width default to 9, height to 3
            width = tag.contains("width") ? tag.getInt("width") : 9;
            height = tag.contains("height") ? tag.getInt("height") : 3;

            upgraded = (width != 9) && (height != 3);

            retaining = tag.contains("Inventory");
        }

        StringTextComponent widthComponent = new StringTextComponent("Width: ");
        widthComponent.append(new StringTextComponent(width + "").mergeStyle(TextFormatting.LIGHT_PURPLE));

        StringTextComponent heightComponent = new StringTextComponent("Height: ");
        heightComponent.append(new StringTextComponent(height + "").mergeStyle(TextFormatting.LIGHT_PURPLE));

        tooltip.add(widthComponent);
        tooltip.add(heightComponent);

        if(retaining) {
            tooltip.add(new StringTextComponent("Retaining").mergeStyle(TextFormatting.AQUA));
        }

        if(upgraded) {
            tooltip.add(new StringTextComponent("Upgraded").mergeStyle(TextFormatting.RED));
        }

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
}
