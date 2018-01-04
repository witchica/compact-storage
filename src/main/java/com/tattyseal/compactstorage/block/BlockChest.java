package com.tattyseal.compactstorage.block;

import com.tattyseal.compactstorage.CompactStorage;
import com.tattyseal.compactstorage.exception.InvalidSizeException;
import com.tattyseal.compactstorage.tileentity.TileEntityChest;
import com.tattyseal.compactstorage.util.EntityUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.awt.*;
import java.util.List;
import java.util.Random;

/**
 * Created by Toby on 06/11/2014.
 */
public class BlockChest extends Block implements ITileEntityProvider
{
    public BlockChest()
    {
        super(Material.WOOD);
        setUnlocalizedName("compactchest");
        setCreativeTab(CompactStorage.tabCS);

        setHardness(2F);
        setResistance(2F);
        setHarvestLevel("axe", 1);
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
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

                if(stack.getTagCompound().hasKey("hue"))
                {
                    chest.setHue(stack.getTagCompound().getInteger("hue"));
                    chest.color = chest.getHue() == -1 ? Color.white : Color.getHSBColor(stack.getTagCompound().getInteger("hue"), 50, 50);
                }
                else if(stack.getTagCompound().hasKey("color") && !stack.getTagCompound().hasKey("hue"))
                {
                    String color = stack.getTagCompound().getString("color");

                    if(color.startsWith("0x"))
                    {
                        color = "#" + color.substring(2);
                    }

                    chest.color = color == "" ? Color.white : Color.decode(color);

                    float[] hsbVals = new float[3];
                    hsbVals = Color.RGBtoHSB(chest.color.getRed(), chest.color.getGreen(), chest.color.getBlue(), hsbVals);
                    chest.setHue((int) hsbVals[0] * 360);
                }
                else
                {
                    chest.color = Color.white;
                }

                chest.items = new ItemStack[chest.invX * chest.invY];
        	}
        	else
        	{
        		if(entity instanceof EntityPlayer)
        		{
        			((EntityPlayer) entity).sendMessage(new TextComponentString(TextFormatting.RED + "You attempted something bad! :("));

                    chest.invX = 9;
                    chest.invY = 3;
                    chest.items = new ItemStack[chest.invX * chest.invY];
                    chest.setHue(180);
                    chest.color = Color.white;

        			InvalidSizeException exception = new InvalidSizeException("You tried to pass off a " + stack.getTagCompound().getTag("size").getClass().getName() + " as a Integer Array. Do not report this or you will be ignored. This is a user based error.");
        			exception.printStackTrace();
        		}
        	}
        }
        else
        {
            if(entity instanceof EntityPlayer)
            {
                ((EntityPlayer) entity).sendMessage(new TextComponentString(TextFormatting.RED + "You attempted something bad! :("));

                chest.invX = 9;
                chest.invY = 3;
                chest.items = new ItemStack[chest.invX * chest.invY];
                chest.setHue(180);
                chest.color = Color.white;
            }
        }

        if(stack.hasTagCompound() && stack.getTagCompound().hasKey("chestData"))
        {
            NBTTagCompound chestData = stack.getTagCompound().getCompoundTag("chestData");
            chestData.removeTag("facing");
            chestData.setInteger("x", pos.getX());
            chestData.setInteger("y", pos.getY());
            chestData.setInteger("z", pos.getZ());

            chest.readFromNBT(chestData);
            chest.markDirty();
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float x, float y, float z)
    {
        if(!world.isRemote)
        {
            if(!player.isSneaking())
            {
                player.openGui(CompactStorage.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());

                return true;
            }
            else
            {
                ItemStack held = player.getHeldItem(EnumHand.MAIN_HAND);
                TileEntityChest chest = (TileEntityChest) world.getTileEntity(pos);
                
                if(!chest.getRetaining() && !held.isEmpty() && held.getItem() == Items.DIAMOND)
                {
                    chest.setRetaining(true);
                    held.setCount(held.getCount() - 1);
                    player.sendMessage(new TextComponentString(TextFormatting.AQUA + "Chest will now retain items when broken!"));
                    world.playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 1, 1);
                    chest.updateBlock();
                }
            }
        }

        return player.isSneaking() ? false : true;
    }

    public TileEntity createNewTileEntity(World world, int dim)
    {
        return new TileEntityChest();
    }

    /**
     * The type of render function called. 3 for standard block models, 2 for TESR's, 1 for liquids, -1 is no render
     */
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.INVISIBLE;
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
            int hue = chest.getHue();
            int color = chest.color.getRGB();

            String colorString = String.format("0x%06X", (0xFFFFFF & color));

            stack.getTagCompound().setIntArray("size", new int[]{invX, invY});
            stack.getTagCompound().setString("color", colorString);
            stack.getTagCompound().setInteger("hue", hue);

            if(chest.getRetaining())
            {
                NBTTagCompound chestData = new NBTTagCompound();

                chest.writeToNBT(chestData);
                stack.getTagCompound().setTag("chestData", chestData);
            }
            else
            {
                for(int slot = 0; slot < chest.items.length; slot++)
                {
                    float randX = rand.nextFloat();
                    float randZ = rand.nextFloat();

                    if(chest.items != null && chest.items[slot] != null && chest.items[slot] != ItemStack.EMPTY) world.spawnEntity(new EntityItem(world, pos.getX() + randX, pos.getY() + 0.5f, pos.getZ() + randZ, chest.items[slot]));
                }
            }

            world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack));
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        items.add(new ItemStack(this, 1, 4));
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
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
