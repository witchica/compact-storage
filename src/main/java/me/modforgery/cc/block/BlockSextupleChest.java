package me.modforgery.cc.block;

import me.modforgery.cc.creativetabs.CreativeTabChest;
import me.modforgery.cc.tileentity.TileEntityQuintupleChest;
import me.modforgery.cc.tileentity.TileEntitySextupleChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Toby on 19/08/2014.
 */
public class BlockSextupleChest extends BlockChest
{
    public BlockSextupleChest()
    {
        super(4);

        setBlockName("sextuplechest");
        setHardness(1f);
        setResistance(1f);

        setCreativeTab(CreativeTabChest.tab);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int dim)
    {
        return new TileEntitySextupleChest();
    }
}
