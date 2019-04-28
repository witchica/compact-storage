package com.tattyseal.compactstorage.block;

import com.tattyseal.compactstorage.tileentity.TileEntityBarrelFluid;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.World;

public class BlockFluidBarrel extends BlockBarrel {
	@Override
	public void init() {
		setRegistryName("fluid_barrel");
		setTranslationKey("barrel_fluid");
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityBarrelFluid();
	}
}
