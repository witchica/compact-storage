package com.workshop.compactstorage.block;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.gson.JsonParseException;
import com.workshop.compactstorage.essential.CompactStorage;
import com.workshop.compactstorage.tileentity.TileEntityChest;
import com.workshop.compactstorage.util.EntityUtil;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

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
    public int colorMultiplier(IBlockAccess world, int x, int y, int z)
    {
        return 0xFFFFFF;
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
        ((TileEntityChest) world.getTileEntity(x, y, z)).invX = 9;
        ((TileEntityChest) world.getTileEntity(x, y, z)).invY = 6;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int i, float j, float k, float l)
    {
        if(CompactStorage.deobf)
        {
        	if(!player.isSneaking())
            {
                player.openGui(CompactStorage.instance, 0, world, x, y, z);
                return true;
            }
            else
            {
            	((TileEntityChest) world.getTileEntity(x, y, z)).color = world.rand.nextInt(0xFFFFFF);
            }
        }
        else
        {
        	player.addChatMessage(new ChatComponentText("Nope! WIP!"));
        	((TileEntityChest) world.getTileEntity(x, y, z)).color = 0xFF0000;
        }

        return false;
    }

    public TileEntity createNewTileEntity(World world, int dim)
    {
        return new TileEntityChest();
    }
}
