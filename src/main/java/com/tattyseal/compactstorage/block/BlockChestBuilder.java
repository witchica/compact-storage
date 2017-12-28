package com.tattyseal.compactstorage.block;

import com.tattyseal.compactstorage.CompactStorage;
import com.tattyseal.compactstorage.tileentity.TileEntityChestBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockChestBuilder extends Block implements ITileEntityProvider
{
	public BlockChestBuilder()
	{
		super(Material.IRON);
        setUnlocalizedName("chestBuilder");
        setCreativeTab(CompactStorage.tabCS);

		setHardness(2F);
		setResistance(2F);
		setHarvestLevel("pickaxe", 1);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float x, float y, float z)
	{
		if(!player.isSneaking())
		{
			if(!world.isRemote) {
				player.openGui(CompactStorage.instance, 1, world, pos.getX(), pos.getY(), pos.getZ());
			}

			return true;
		}
		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack stack)
	{
		super.onBlockPlacedBy(world, pos, state, player, stack);

		TileEntityChestBuilder cb = (TileEntityChestBuilder) world.getTileEntity(pos);

		if (cb != null) {
			cb.player = player.getName();

			if (stack.hasDisplayName()) {
				cb.setCustomName(stack.getDisplayName());
			}
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(@Nonnull World world, int dim)
	{
		return new TileEntityChestBuilder();
	}

	@Override
	public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state)
	{
		TileEntityChestBuilder chest = (TileEntityChestBuilder) world.getTileEntity(pos);

		if (chest == null) {
			return;
		}

		Random rand = new Random();

		for(int slot = 0; slot < chest.items.length; slot++)
		{
			float randX = rand.nextFloat();
			float randZ = rand.nextFloat();

			if(chest.items != null && chest.items[slot] != ItemStack.EMPTY) world.spawnEntity(new EntityItem(world, pos.getX() + randX, pos.getY() + 0.5f, pos.getZ() + randZ, chest.items[slot]));
		}

		super.breakBlock(world, pos, state);
	}
}
