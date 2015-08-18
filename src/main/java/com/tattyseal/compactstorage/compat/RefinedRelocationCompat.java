package com.tattyseal.compactstorage.compat;

import com.dynious.refinedrelocation.api.ModObjects;
import com.tattyseal.compactstorage.block.BlockSortingChest;
import com.tattyseal.compactstorage.item.ItemBlockChest;
import com.tattyseal.compactstorage.tileentity.TileEntityChest;
import com.tattyseal.compactstorage.tileentity.TileEntitySortingChest;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class RefinedRelocationCompat implements ICompat
{
    public static Block sortingChest;

    @Override
    public String modid()
    {
        return "RefinedRelocation";
    }

    @Override
    public void registerCompat() throws Exception
    {
        sortingChest = new BlockSortingChest();
        GameRegistry.registerBlock(sortingChest, ItemBlockChest.class, "sortingCompactChest");
        GameRegistry.registerTileEntity(TileEntitySortingChest.class, "tileSortingChest");
    }

    public static boolean tryToUpgrade(ItemStack stack, World world, int x, int y, int z)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityChest && !(tile instanceof TileEntitySortingChest) && stack != null && stack.getItem() == ModObjects.sortingUpgrade.getItem() && stack.getItemDamage() == 0)
        {
            TileEntityChest chest = (TileEntityChest) tile;
            TileEntitySortingChest sortingChestTile = new TileEntitySortingChest();
            int meta = chest.getBlockMetadata();

            NBTTagCompound compound = new NBTTagCompound();
            chest.writeToNBT(compound);
            sortingChestTile.readFromNBT(compound);

            //Remove the TE first so we don't drop items
            world.removeTileEntity(x, y, z);
            // Clear the old block out
            world.setBlockToAir(x, y, z);
            // Force the Chest TE to reset it's knowledge of neighbouring blocks
            // And put in our block instead
            world.setBlock(x, y, z, sortingChest, 0, 3);

            world.setTileEntity(x, y, z, sortingChestTile);
            world.setBlockMetadataWithNotify(x, y, z, meta, 3);
            stack.stackSize--;
            return true;
        }
        return false;
    }
}
