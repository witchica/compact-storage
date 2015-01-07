package com.workshop.compactstorage.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.workshop.compactstorage.essential.CompactStorage;
import com.workshop.compactstorage.tileentity.TileEntityChestBuilder;

public class BlockChestBuilder extends Block implements ITileEntityProvider
{
	public BlockChestBuilder()
	{
		super(Material.iron);
        setBlockName("chestbuilder");
        setBlockTextureName("planks_oak");
        setCreativeTab(CompactStorage.tabCS);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x,int y, int z, EntityPlayer player,int u, float i, float a, float p)
	{
		if(!player.isSneaking())
		{
			player.openGui(CompactStorage.instance, 1, world, x, y, z);
			return true;
		}
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int dim)
	{
		return new TileEntityChestBuilder(dim);
	}
	
}
