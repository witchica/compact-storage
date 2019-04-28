package com.tattyseal.compactstorage.block;

import javax.annotation.Nonnull;

import com.tattyseal.compactstorage.CompactRegistry;
import com.tattyseal.compactstorage.CompactStorage;
import com.tattyseal.compactstorage.tileentity.TileEntityChest;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by Toby on 06/11/2014.
 */
public class BlockChest extends Block {
	protected static final AxisAlignedBB NOT_CONNECTED_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);

	public BlockChest() {
		super(Material.WOOD);
		setTranslationKey(CompactStorage.MODID + ".chest");
		setRegistryName("chest");
		setCreativeTab(CompactStorage.TAB);
		setHardness(2F);
		setResistance(2F);
		setHarvestLevel("axe", 1);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return NOT_CONNECTED_AABB;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float x, float y, float z) {
		if (!world.isRemote) {
			if (!player.isSneaking()) {
				player.openGui(CompactStorage.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());

				return true;
			} else {
				ItemStack held = player.getHeldItem(EnumHand.MAIN_HAND);
				TileEntityChest chest = (TileEntityChest) world.getTileEntity(pos);
				if (chest != null && !chest.isRetaining() && !held.isEmpty() && held.getItem() == Items.DIAMOND) {
					chest.setRetaining(true);
					held.setCount(held.getCount() - 1);
					player.sendMessage(new TextComponentString(TextFormatting.AQUA + "Chest will now retain items when broken!"));
					world.playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 1, 1);
					chest.updateBlock();
				}
			}
		}

		return !player.isSneaking();
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityChest();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}

	@Override
	public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
		TileEntityChest chest = (TileEntityChest) world.getTileEntity(pos);

		if (!world.isRemote && chest != null) {
			ItemStack stack = new ItemStack(CompactRegistry.CHEST);

			if (chest.isRetaining()) {
				chest.writeToNBT(stack.getOrCreateSubCompound("BlockEntityTag"));
			} else {
				for (int slot = 0; slot < chest.getItems().getSlots(); slot++) {
					Block.spawnAsEntity(world, pos, chest.getItems().getStackInSlot(slot));
				}
				chest.writeToNBT(stack.getOrCreateSubCompound("BlockEntityTag")).removeTag("items");
			}

			world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack));
		}

		super.breakBlock(world, pos, state);
	}

	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {

	}

	@Override
	@Nonnull
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		ItemStack stack = new ItemStack(CompactRegistry.CHEST, 1);
		TileEntityChest chest = (TileEntityChest) world.getTileEntity(pos);
		chest.writeToNBT(stack.getOrCreateSubCompound("BlockEntityTag"));
		return stack;
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}
}
