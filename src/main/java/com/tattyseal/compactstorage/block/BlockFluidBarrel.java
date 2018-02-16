package com.tattyseal.compactstorage.block;

import com.tattyseal.compactstorage.tileentity.TileEntityBarrel;
import com.tattyseal.compactstorage.tileentity.TileEntityBarrelFluid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockFluidBarrel extends BlockBarrel
{
    @Override
    public void init()
    {
        setRegistryName("barrel_fluid");
        setUnlocalizedName("barrel_fluid");
    }

    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityBarrelFluid();
    }
}
