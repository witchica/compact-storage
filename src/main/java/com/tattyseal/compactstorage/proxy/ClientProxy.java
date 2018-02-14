package com.tattyseal.compactstorage.proxy;

import com.tattyseal.compactstorage.CompactStorage;
import com.tattyseal.compactstorage.block.BlockBarrel;
import com.tattyseal.compactstorage.block.BlockChest;
import com.tattyseal.compactstorage.client.render.TileEntityBarrelFluidRenderer;
import com.tattyseal.compactstorage.client.render.TileEntityBarrelRenderer;
import com.tattyseal.compactstorage.client.render.TileEntityChestRenderer;
import com.tattyseal.compactstorage.event.ConnectionHandler;
import com.tattyseal.compactstorage.item.ItemBackpack;
import com.tattyseal.compactstorage.item.ItemBlockChest;
import com.tattyseal.compactstorage.tileentity.IBarrel;
import com.tattyseal.compactstorage.tileentity.TileEntityBarrel;
import com.tattyseal.compactstorage.tileentity.TileEntityBarrelFluid;
import com.tattyseal.compactstorage.tileentity.TileEntityChest;
import com.tattyseal.compactstorage.util.ModelUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import javax.annotation.Nullable;
import java.awt.Color;

/**
 * Created by Toby on 06/11/2014.
 */
public class ClientProxy implements IProxy
{
    public void registerRenderers()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChest.class, new TileEntityChestRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBarrel.class, new TileEntityBarrelRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBarrelFluid.class, new TileEntityBarrelFluidRenderer());

        ModelUtil.registerChest();
        ModelUtil.registerBlock(CompactStorage.chestBuilder, 0, "compactstorage:chestBuilder");

        ModelUtil.registerBlock(CompactStorage.barrel, 0, "compactstorage:barrel");
        ModelUtil.registerBlock(CompactStorage.barrel_fluid, 0, "compactstorage:barrel_fluid");

        ModelUtil.registerBlock(CompactStorage.chest, 0, "compactstorage:compactchest");
        ModelUtil.registerItem(CompactStorage.backpack, 0, "compactstorage:backpack");

        ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
        BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();

        blockColors.registerBlockColorHandler(new IBlockColor() {
            @Override
            public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex)
            {
                return (worldIn != null && pos != null && worldIn.getTileEntity(pos) != null) ? getColorFromHue(((IBarrel) worldIn.getTileEntity(pos)).color()) : 0xFFFFFF;
            }
        }, CompactStorage.barrel, CompactStorage.barrel_fluid);


        itemColors.registerItemColorHandler(new IItemColor() {
            @Override
            public int colorMultiplier(ItemStack stack, int color)
            {
                return getColorFromNBT(stack);
            }
        }, CompactStorage.backpack, CompactStorage.ibChest, CompactStorage.itemBlockBarrel, CompactStorage.itemBlockBarrel_fluid);

        MinecraftForge.EVENT_BUS.register(new ConnectionHandler());
    }

    private int getColorFromHue(int hue)
    {
        Color color = (hue == -1 ? Color.white : Color.getHSBColor(hue / 360f, 0.5f, 0.5f).brighter());
        return color.getRGB();
    }

    private int getColorFromNBT(ItemStack stack)
    {
        NBTTagCompound tag = stack.getTagCompound();

        if(stack.hasTagCompound() && stack.getTagCompound().hasKey("hue"))
        {
            int hue = stack.getTagCompound().getInteger("hue");
            return getColorFromHue(hue);
        }

        if(stack.hasTagCompound() && !stack.getTagCompound().hasKey("hue") && stack.getTagCompound().hasKey("color"))
        {
            String color = "";

            if(tag.getTag("color") instanceof NBTTagInt)
            {
                color = String.format("#%06X", (0xFFFFFF & tag.getInteger("color")));
            }
            else
            {
                color = tag.getString("color");

                if(color.startsWith("0x"))
                {
                    color = "#" + color.substring(2);
                }
            }

            if(!color.isEmpty())
            {
                Color c = Color.decode(color);
                float[] hsbVals = new float[3];

                hsbVals = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsbVals);
                tag.setInteger("hue", (int) (hsbVals[0] * 360));
            }
        }

        return 0xFFFFFF;
    }
}
