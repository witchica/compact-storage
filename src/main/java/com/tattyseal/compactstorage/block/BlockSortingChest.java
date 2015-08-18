package com.tattyseal.compactstorage.block;

import com.dynious.refinedrelocation.api.APIUtils;
import com.tattyseal.compactstorage.compat.RefinedRelocationCompat;
import com.tattyseal.compactstorage.tileentity.TileEntityChest;
import com.tattyseal.compactstorage.tileentity.TileEntitySortingChest;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.Random;

public class BlockSortingChest extends BlockChest
{
    public BlockSortingChest()
    {
        setBlockName("sortingcompactchest");
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int i1, float f1, float f2, float f3)
    {
        if (world.isRemote)
        {
            return true;
        }

        if (player.isSneaking())
        {
            APIUtils.openFilteringGUI(player, world, x, y, z);
            return true;
        }

        return super.onBlockActivated(world, x, y, z, player, i1, f1, f2, f3);
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        return new TileEntitySortingChest();
    }

    //TODO: Maybe do this differently (this is copied from BlockChest and only changed the ItemStack Block input)
    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        TileEntityChest chest = (TileEntityChest) world.getTileEntity(x, y, z);

        if(chest != null)
        {
            ItemStack stack = new ItemStack(RefinedRelocationCompat.sortingChest, 1);
            Random rand = new Random();

            stack.setTagCompound(new NBTTagCompound());

            int invX = chest.invX;
            int invY = chest.invY;
            int color = chest.color;

            stack.getTagCompound().setIntArray("size", new int[]{invX, invY});
            stack.getTagCompound().setInteger("color", color);

            world.spawnEntityInWorld(new EntityItem(world, x, y + 0.5f, z, stack));

            for(int slot = 0; slot < chest.items.length; slot++)
            {
                float randX = rand.nextFloat();
                float randZ = rand.nextFloat();

                if(chest.items != null && chest.items[slot] != null) world.spawnEntityInWorld(new EntityItem(world, x + randX, y + 0.5f, z + randZ, chest.items[slot]));
            }
        }
    }

    //TODO: Maybe do this differently (this is copied from BlockChest and only changed the ItemStack Block input)
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        ItemStack stack = new ItemStack(RefinedRelocationCompat.sortingChest, 1);
        TileEntityChest chest = (TileEntityChest) world.getTileEntity(x, y, z);

        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setIntArray("size", new int[] {chest.invX, chest.invY});

        return stack;
    }
}
