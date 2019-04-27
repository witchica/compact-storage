package com.tattyseal.compactstorage.block;

import com.tattyseal.compactstorage.CompactStorage;
import com.tattyseal.compactstorage.tileentity.TileEntityChestBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class BlockChestBuilder extends Block {
	public BlockChestBuilder() {
		super(Material.IRON);
		setTranslationKey("chestBuilder");
		setRegistryName("chestBuilder");
		setCreativeTab(CompactStorage.TAB);

		setHardness(2F);
		setResistance(2F);
		setHarvestLevel("pickaxe", 1);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float x, float y, float z) {
		if (!player.isSneaking()) {
			if (!world.isRemote) {
				player.openGui(CompactStorage.instance, 1, world, pos.getX(), pos.getY(), pos.getZ());
			}

			return true;
		}
		return false;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityChestBuilder();
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity chest = world.getTileEntity(pos);
		if (!(chest instanceof TileEntityChestBuilder)) return;

		ItemStackHandler items = ((TileEntityChestBuilder) chest).getItems();

		for (int slot = 0; slot < items.getSlots(); slot++) {
			Block.spawnAsEntity(world, pos, items.getStackInSlot(slot));
		}

		super.breakBlock(world, pos, state);
	}
}
