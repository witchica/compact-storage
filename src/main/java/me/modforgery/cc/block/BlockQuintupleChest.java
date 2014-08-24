package me.modforgery.cc.block;

import me.modforgery.cc.tileentity.TileEntityQuadrupleChest;
import me.modforgery.cc.tileentity.TileEntityQuintupleChest;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Toby on 19/08/2014.
 */
public class BlockQuintupleChest extends BlockChest
{
    public BlockQuintupleChest()
    {
        super(3);

        setBlockName("quintuplechest");
        setHardness(1f);
        setResistance(1f);

        setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int dim)
    {
        return new TileEntityQuintupleChest();
    }
}
