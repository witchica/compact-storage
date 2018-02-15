package com.tattyseal.compactstorage.block;

import com.tattyseal.compactstorage.CompactStorage;
import com.tattyseal.compactstorage.tileentity.IBarrel;
import com.tattyseal.compactstorage.tileentity.TileEntityBarrel;
import com.tattyseal.compactstorage.util.EntityUtil;
import com.tattyseal.compactstorage.util.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
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

import javax.annotation.Nullable;

public class BlockBarrel extends Block implements ITileEntityProvider
{
    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class, new EnumFacing[] {EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.WEST});

    public BlockBarrel()
    {
        super(Material.IRON);
        setHardness(3f);
        init();
        setCreativeTab(CompactStorage.tabCS);
    }

    public void init()
    {
        setRegistryName("barrel");
        setUnlocalizedName("barrel");
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing) state.getProperties().get(FACING)).ordinal();
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing facing = EnumFacing.values()[meta];

        if(FACING.getAllowedValues().contains(facing))
        {
            return getDefaultState().withProperty(FACING, EnumFacing.values()[meta]);
        }

        return getDefaultState();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        worldIn.setBlockState(pos, state.withProperty(FACING, EntityUtil.get2dOrientation(placer)));
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
    {
        super.onBlockClicked(worldIn, pos, playerIn);

        if(!worldIn.isRemote)
        {
            IBarrel barrel = (IBarrel) worldIn.getTileEntity(pos);

            if(barrel != null)
            {
                ItemStack stack = barrel.dropItems(playerIn);

                if(!stack.isEmpty())
                {
                    EntityItem item = new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ);
                    item.setItem(stack);

                    worldIn.spawnEntity(item);
                }
            }
        }
    }

    @Override
    public boolean isFullBlock(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(playerIn.isSneaking())
            return false;

        if(!worldIn.isRemote)
        {
            IBarrel barrel = (IBarrel) worldIn.getTileEntity(pos);

            if(playerIn.getHeldItem(EnumHand.MAIN_HAND).isEmpty())
            {
                if(barrel != null)
                {
                    ItemStack stack = barrel.dropItems(playerIn);

                    if(!stack.isEmpty())
                    {
                        EntityItem item = new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ);
                        item.setItem(stack);

                        worldIn.spawnEntity(item);
                    }
                }
            }
            else
            {
                if(barrel != null)
                {
                    playerIn.setHeldItem(EnumHand.MAIN_HAND, barrel.insertItems(playerIn.getHeldItem(EnumHand.MAIN_HAND), playerIn));
                    return true;
                }
            }
        }

        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityBarrel();
    }
}
