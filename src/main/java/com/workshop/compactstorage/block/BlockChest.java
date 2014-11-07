package com.workshop.compactstorage.block;

import com.workshop.compactstorage.essential.CompactStorage;
import com.workshop.compactstorage.tileentity.TileEntityChest;
import com.workshop.compactstorage.util.EntityUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Toby on 06/11/2014.
 */
public class BlockChest extends Block implements ITileEntityProvider
{
    public BlockChest()
    {
        super(Material.wood);
        setBlockName("compactchest");
        setBlockTextureName("planks_oak");
        setCreativeTab(CompactStorage.tabCS);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
        for(ChestType type : ChestType.values())
        {
            list.add(new ItemStack(item, 1, type.id));
        }
    }

    @Override
    public int colorMultiplier(IBlockAccess world, int x, int y, int z)
    {
        return ChestType.values()[world.getBlockMetadata(x, y, z)].defaultColor;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
    {
        super.onBlockPlacedBy(world, x, y, z, entity, stack);

        ((TileEntityChest) world.getTileEntity(x, y, z)).direction = EntityUtil.get2dOrientation(entity);
    }

    public TileEntity createNewTileEntity(World world, int dim)
    {
        return new TileEntityChest();
    }
}
