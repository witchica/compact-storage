package com.workshop.compactchests.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.workshop.compactchests.tileentity.TileEntityDoubleChest;

/**
 * Created by Toby on 19/08/2014.
 */
public class BlockDoubleChest extends BlockChest
{
    public BlockDoubleChest()
    {
        super(0);

        setBlockName("doublechest");
        setHardness(1f);
        setResistance(1f);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int dim)
    {
        return new TileEntityDoubleChest();
    }
}
