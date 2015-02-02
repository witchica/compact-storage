package com.workshop.compactchests.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.workshop.compactchests.tileentity.TileEntityQuintupleChest;

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
    }

    @Override
    public TileEntity createNewTileEntity(World world, int dim)
    {
        return new TileEntityQuintupleChest();
    }
}
