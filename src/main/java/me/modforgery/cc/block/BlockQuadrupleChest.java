package me.modforgery.cc.block;

import me.modforgery.cc.creativetabs.CreativeTabChest;
import me.modforgery.cc.tileentity.TileEntityQuadrupleChest;
import me.modforgery.cc.tileentity.TileEntityTripleChest;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Toby on 19/08/2014.
 */
public class BlockQuadrupleChest extends BlockChest
{
    public BlockQuadrupleChest()
    {
        super(2);

        setBlockName("quadruplechest");
        setHardness(1f);
        setResistance(1f);

        setCreativeTab(CreativeTabChest.tab);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int dim)
    {
        return new TileEntityQuadrupleChest();
    }
}
