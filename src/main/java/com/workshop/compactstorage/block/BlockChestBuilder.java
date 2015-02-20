package com.workshop.compactstorage.block;

import com.workshop.compactstorage.essential.CompactStorage;
import com.workshop.compactstorage.tileentity.TileEntityChestBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockChestBuilder extends Block implements ITileEntityProvider
{
	public IIcon[] icons;
	
	public BlockChestBuilder()
	{
		super(Material.iron);
        setBlockName("chestbuilder");
        setCreativeTab(CompactStorage.tabCS);

		setHardness(2F);
		setResistance(2F);
		setHarvestLevel("pickaxe", 1);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x,int y, int z, EntityPlayer player,int u, float i, float a, float p)
	{
		if(!player.isSneaking() && !world.isRemote)
		{
			player.openGui(CompactStorage.instance, 1, world, x, y, z);
			return true;
		}
		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) 
	{
		super.onBlockPlacedBy(world, x, y, z, player, stack);
		
		((TileEntityChestBuilder) world.getTileEntity(x, y, z)).player = player.getCommandSenderName();
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int dim)
	{
		return new TileEntityChestBuilder();
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register) 
	{
		icons = new IIcon[3];
		icons[0] = register.registerIcon("compactstorage:chestBuilderTop");
		icons[1] = register.registerIcon("compactstorage:chestBuilderSide");
		icons[2] = register.registerIcon("compactstorage:chestBuilderFront");
	}
	
	@Override
	public IIcon getIcon(IBlockAccess access, int x, int y, int z, int side) 
	{
		return getIcon(side, 0);
	}
	
	@Override
	public IIcon getIcon(int side, int meta)
	{
		switch(side)
		{
			case 0: return Blocks.iron_block.getIcon(0, 0);
			case 1: return icons[0];
			case 2: return icons[1];
			case 4: return icons[1];
			case 3: return icons[2];
			case 5: return icons[2];
		}
		
		return null;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta)
	{
		TileEntityChestBuilder chest = (TileEntityChestBuilder) world.getTileEntity(x, y, z);
		Random rand = new Random();

		for(int slot = 0; slot < chest.items.length; slot++)
		{
			float randX = rand.nextFloat();
			float randZ = rand.nextFloat();

			if(chest.items != null && chest.items[slot] != null) world.spawnEntityInWorld(new EntityItem(world, x + randX, y + 0.5f, z + randZ, chest.items[slot]));
		}

		super.breakBlock(world, x, y, z, block, meta);
	}
}
