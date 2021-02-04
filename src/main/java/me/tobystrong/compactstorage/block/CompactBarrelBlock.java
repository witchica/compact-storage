package me.tobystrong.compactstorage.block;

import me.tobystrong.compactstorage.CompactStorage;
import me.tobystrong.compactstorage.block.tile.CompactBarrelTileEntity;
import me.tobystrong.compactstorage.block.tile.CompactChestTileEntity;
import me.tobystrong.compactstorage.util.CompactStorageUtilMethods;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.List;
import java.util.UUID;

public class CompactBarrelBlock extends ContainerBlock {
    public static EnumProperty<Direction> PROPERTY_FACING = EnumProperty.create("facing", Direction.class);
    public static BooleanProperty PROPERTY_OPEN = BooleanProperty.create("open");

    public CompactBarrelBlock() {
        super(Properties.create(Material.IRON).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(1));
        setDefaultState(this.getStateContainer().getBaseState().with(PROPERTY_FACING, Direction.NORTH).with(PROPERTY_OPEN, false));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(worldIn.isRemote) {
            return ActionResultType.SUCCESS;
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
                CompactBarrelTileEntity barrelTile = (CompactBarrelTileEntity) entity;
                CompactStorageUtilMethods.UpgradeStatus status = barrelTile.handleUpgradeItem(heldItem);

                if(status == CompactStorageUtilMethods.UpgradeStatus.SUCCESS) {
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
            Block newBlock = CompactStorage.barrel_blocks[item.getDyeColor().getId()];

            //if the dyed block is not the same colour
            if(newBlock != this) {
                //get the new block and set the state with a client update (flag 2)
                BlockState newState = CompactStorage.barrel_blocks[item.getDyeColor().getId()].getDefaultState().with(PROPERTY_FACING, dir);
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
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if(!(newState.getBlock() instanceof CompactBarrelBlock)) {
            if (!state.isIn(newState.getBlock())) {
                //get the tile entity
                TileEntity tile = worldIn.getTileEntity(pos);

                if(tile instanceof CompactBarrelTileEntity) {
                    CompactBarrelTileEntity barrelTile = (CompactBarrelTileEntity) tile;
                    ItemStack barrelItem = new ItemStack(state.getBlock(), 1);

                    //this creates a block with the right NBT to keep the upgrades that have been applied

                    if(!(barrelTile.width == 9 && barrelTile.width == 3)) {
                        CompoundNBT tag = barrelItem.getOrCreateChildTag("BlockEntityTag");
                        tag.putInt("width", barrelTile.width);
                        tag.putInt("height", barrelTile.height);
                    }

                    //make a list of all the chest contents using the capability

                    NonNullList<ItemStack> drops = NonNullList.create();
                    IItemHandler itemHandler = barrelTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(NullPointerException::new);

                    for(int i = 0; i < itemHandler.getSlots(); i++) {
                        drops.add(itemHandler.getStackInSlot(i));
                    }

                    drops.add(barrelItem);

                    //drop all the items in the world
                    InventoryHelper.dropItems(worldIn, pos, drops);
                }
            }
            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public void addInformation(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompactStorageUtilMethods.addInformationForUpgradableBlocks(stack, worldIn, tooltip, flagIn);
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(PROPERTY_FACING, PROPERTY_OPEN);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context).with(PROPERTY_FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new CompactBarrelTileEntity();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
