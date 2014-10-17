package com.workshop.compactchests.block;

import com.workshop.compactchests.CompactChests;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Toby on 19/08/2014.
 */
public abstract class BlockChest extends Block implements ITileEntityProvider
{
    public int guiID;

    public BlockChest(int guiID)
    {
        super(Material.wood);
        setBlockTextureName("planks_oak");
        this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);

        this.guiID = guiID;
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
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int i, float j, float k, float l)
    {
        if(!player.isSneaking())
        {
            player.openGui(CompactChests.instance(), guiID, world, x, y, z);
            return true;
        }

        return false;
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World world, int x, int y, int z)
    {
        super.onBlockAdded(world, x, y, z);
        this.rotateChest(world, x, y, z);
    }

    private void rotateChest(World world, int x, int y, int z)
    {
        if (!world.isRemote)
        {
            Block block = world.getBlock(x, y, z - 1);
            Block block1 = world.getBlock(x, y, z + 1);
            Block block2 = world.getBlock(x - 1, y, z);
            Block block3 = world.getBlock(x + 1, y, z);

            byte metadata = 3;

            if (block.func_149730_j() && !block1.func_149730_j())
            {
                metadata = 3;
            }

            if (block1.func_149730_j() && !block.func_149730_j())
            {
                metadata = 2;
            }

            if (block2.func_149730_j() && !block3.func_149730_j())
            {
                metadata = 5;
            }

            if (block3.func_149730_j() && !block2.func_149730_j())
            {
                metadata = 4;
            }

            world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
        }
    }

    public void rotateChestPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemstack)
    {
        int l = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0)
        {
            world.setBlockMetadataWithNotify(x, y, z, 2, 2);
        }

        if (l == 1)
        {
            world.setBlockMetadataWithNotify(x, y, z, 5, 2);
        }

        if (l == 2)
        {
            world.setBlockMetadataWithNotify(x, y, z, 3, 2);
        }

        if (l == 3)
        {
            world.setBlockMetadataWithNotify(x, y, z, 4, 2);
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
    {
        super.onBlockPlacedBy(world, x, y, z, entity, stack);
        rotateChestPlacedBy(world, x, y, z, entity, stack);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        IInventory inv = (IInventory) world.getTileEntity(x, y, z);

        for(int slot = 0; slot < inv.getSizeInventory(); slot++)
        {
            if(inv.getStackInSlot(slot) != null)
            {
                Random rand = new Random();

                EntityItem item = new EntityItem(world, x + rand.nextFloat(), y + rand.nextFloat(), z + rand.nextFloat(), inv.getStackInSlot(slot));
                world.spawnEntityInWorld(item);
            }
        }

        super.breakBlock(world, x, y, z, block, meta);
    }
}
