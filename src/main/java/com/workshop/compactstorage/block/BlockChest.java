package com.workshop.compactstorage.block;

import com.workshop.compactstorage.essential.CompactStorage;
import com.workshop.compactstorage.essential.init.StorageBlocks;
import com.workshop.compactstorage.exception.InvalidSizeException;
import com.workshop.compactstorage.tileentity.TileEntityChest;
import com.workshop.compactstorage.util.EntityUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
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

        TileEntityChest chest = ((TileEntityChest) world.getTileEntity(x, y, z));

        chest.direction = EntityUtil.get2dOrientation(entity);
        
        if(stack.hasTagCompound())
        {
        	if(stack.getTagCompound().getTag("size") instanceof NBTTagIntArray)
        	{
                chest.invX = stack.getTagCompound().getIntArray("size")[0];
                chest.invY = stack.getTagCompound().getIntArray("size")[1];

                if(stack.getTagCompound().hasKey("color"))
                {
                    chest.color = Integer.decode(stack.getTagCompound().getString("color"));
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

        			InvalidSizeException exception = new InvalidSizeException("You tried to pass off a " + stack.getTagCompound().getTag("size").getClass().getName() + " as a Integer Array. Do not report this or you will be ignored. This is a user based error.");
        			exception.printStackTrace();
        		}
        	}
        }
        else
        {
        	((TileEntityChest) world.getTileEntity(x, y, z)).invX = 0;
            ((TileEntityChest) world.getTileEntity(x, y, z)).invY = 0;
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int i, float j, float k, float l)
    {
        if(CompactStorage.deobf)
        {
        	if(!player.isSneaking())
            {
        		if(player.getHeldItem() != null)
        		{
        			String name = player.getHeldItem().getUnlocalizedName();
        			
        			if(name.startsWith("item.dolly.normal.empty") || name.startsWith("item.dolly.diamond.empty"))
        			{
        				return true;
        			}
        		}
        		
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

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        TileEntityChest chest = (TileEntityChest) world.getTileEntity(x, y, z);

        if(chest != null)
        {
            ItemStack stack = new ItemStack(StorageBlocks.chest, 1);
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

        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player)
    {
        return getPickBlock(target, world, x, y, z);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        ItemStack stack = new ItemStack(StorageBlocks.chest, 1);
        TileEntityChest chest = (TileEntityChest) world.getTileEntity(x, y, z);

        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setIntArray("size", new int[] {chest.invX, chest.invY});

        return stack;
    }

    @Override
    public Item getItemDropped(int meta, Random rand, int fortune)
    {
        return null;
    }
}
