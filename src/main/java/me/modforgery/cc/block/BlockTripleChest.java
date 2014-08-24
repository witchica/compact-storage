package me.modforgery.cc.block;

import me.modforgery.cc.CompactChests;
import me.modforgery.cc.tileentity.TileEntityTripleChest;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * Created by Toby on 19/08/2014.
 */
public class BlockTripleChest extends BlockChest
{
    public BlockTripleChest()
    {
        super(1);

        setBlockName("triplechest");
        setHardness(1f);
        setResistance(1f);

        setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int dim)
    {
        return new TileEntityTripleChest();
    }
}
