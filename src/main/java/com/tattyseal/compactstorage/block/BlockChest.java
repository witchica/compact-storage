package com.tattyseal.compactstorage.block;

import com.tattyseal.compactstorage.CompactStorage;
import com.tattyseal.compactstorage.exception.InvalidSizeException;
import com.tattyseal.compactstorage.tileentity.TileEntityChest;
import com.tattyseal.compactstorage.util.EntityUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Toby on 06/11/2014.
 */
public class BlockChest extends Block implements ITileEntityProvider
{
    public BlockChest()
    {
        super(Material.wood);
        setUnlocalizedName("compactchest");
        setCreativeTab(CompactStorage.tabCS);

        setHardness(2F);
        setResistance(2F);
        setHarvestLevel("axe", 1);
        setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
    }

    @Override
    public boolean isFullCube()
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
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack)
    {
        super.onBlockPlacedBy(world, pos, state, entity, stack);

        TileEntityChest chest = ((TileEntityChest) world.getTileEntity(pos));

        chest.direction = EntityUtil.get2dOrientation(entity);
        
        if(stack.hasTagCompound() && stack.getTagCompound().hasKey("size"))
        {
        	if(stack.getTagCompound().getTag("size") instanceof NBTTagIntArray)
        	{
                chest.invX = stack.getTagCompound().getIntArray("size")[0];
                chest.invY = stack.getTagCompound().getIntArray("size")[1];

                if(stack.getTagCompound().hasKey("color"))
                {
                    String color = stack.getTagCompound().getString("color");
                    chest.color = color == "" ? 0xffffff : Integer.decode(color);
                }
                else
                {
                    chest.color = 0xffffff;
                }

                chest.items = new ItemStack[chest.invX * chest.invY];
        	}
        	else
        	{
        		if(entity instanceof EntityPlayer)
        		{
        			((EntityPlayer) entity).addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "You attempted something bad! :("));

                    chest.invX = 9;
                    chest.invY = 3;
                    chest.items = new ItemStack[chest.invX * chest.invY];
                    chest.color = 0xFFFFFF;

        			InvalidSizeException exception = new InvalidSizeException("You tried to pass off a " + stack.getTagCompound().getTag("size").getClass().getName() + " as a Integer Array. Do not report this or you will be ignored. This is a user based error.");
        			exception.printStackTrace();
        		}
        	}
        }
        else
        {
            if(entity instanceof EntityPlayer)
            {
                ((EntityPlayer) entity).addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "You attempted something bad! :("));

                chest.invX = 9;
                chest.invY = 3;
                chest.items = new ItemStack[chest.invX * chest.invY];
                chest.color = 0xFFFFFF;
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing facing, float x, float y, float z)
    {
        if(!world.isRemote)
        {
            if(!player.isSneaking())
            {
                world.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), "random.chestopen", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
                player.openGui(CompactStorage.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());

                return true;
            }
        }

        return player.isSneaking() ? false : true;
    }

    public TileEntity createNewTileEntity(World world, int dim)
    {
        return new TileEntityChest();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        TileEntityChest chest = (TileEntityChest) world.getTileEntity(pos);

        if(chest != null)
        {
            ItemStack stack = new ItemStack(CompactStorage.chest, 1);
            Random rand = new Random();

            stack.setTagCompound(new NBTTagCompound());

            int invX = chest.invX;
            int invY = chest.invY;
            int color = chest.color;

            stack.getTagCompound().setIntArray("size", new int[]{invX, invY});
            stack.getTagCompound().setInteger("color", color);

            world.spawnEntityInWorld(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack));

            for(int slot = 0; slot < chest.items.length; slot++)
            {
                float randX = rand.nextFloat();
                float randZ = rand.nextFloat();

                if(chest.items != null && chest.items[slot] != null) world.spawnEntityInWorld(new EntityItem(world, pos.getX() + randX, pos.getY() + 0.5f, pos.getZ() + randZ, chest.items[slot]));
            }
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player)
    {
        return getPickBlock(target, world, pos);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos)
    {
        ItemStack stack = new ItemStack(CompactStorage.chest, 1);
        TileEntityChest chest = (TileEntityChest) world.getTileEntity(pos);

        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setIntArray("size", new int[] {chest.invX, chest.invY});

        return stack;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }
}
