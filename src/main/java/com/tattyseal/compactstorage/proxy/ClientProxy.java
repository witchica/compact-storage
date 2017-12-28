package com.tattyseal.compactstorage.proxy;

import com.tattyseal.compactstorage.CompactStorage;
import com.tattyseal.compactstorage.block.BlockChest;
import com.tattyseal.compactstorage.client.render.TileEntityChestRenderer;
import com.tattyseal.compactstorage.event.ConnectionHandler;
import com.tattyseal.compactstorage.item.ItemBackpack;
import com.tattyseal.compactstorage.item.ItemBlockChest;
import com.tattyseal.compactstorage.tileentity.TileEntityChest;
import com.tattyseal.compactstorage.util.ModelUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.awt.Color;

/**
 * Created by Toby on 06/11/2014.
 */
public class ClientProxy implements IProxy
{
    public void registerRenderers()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChest.class, new TileEntityChestRenderer());
        ModelUtil.registerChest();
        ModelUtil.registerBlock(CompactStorage.chestBuilder, 0, "compactstorage:chestBuilder");
        ModelUtil.registerBlock(CompactStorage.chest, 0, "compactstorage:compactchest");
        ModelUtil.registerItem(CompactStorage.backpack, 0, "compactstorage:backpack");

        ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
        BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();

        ItemBackpack backpack = (ItemBackpack) CompactStorage.backpack;
        ItemBlockChest ibChest = CompactStorage.ibChest;
        BlockChest chest = (BlockChest) CompactStorage.chest;

        itemColors.registerItemColorHandler(new IItemColor() {
            @Override
            public int colorMultiplier(ItemStack stack, int color)
            {
                return getColorFromNBT(stack);
            }
        }, backpack);

        itemColors.registerItemColorHandler(new IItemColor() {
            @Override
            public int colorMultiplier(ItemStack stack, int color)
            {
                return getColorFromNBT(stack);
            }
        }, ibChest);

        itemColors.registerItemColorHandler(new IItemColor() {
            @Override
            public int colorMultiplier(ItemStack stack, int color)
            {
                return getColorFromNBT(stack);
            }
        }, chest);

        MinecraftForge.EVENT_BUS.register(new ConnectionHandler());
    }

    private int getColorFromNBT(ItemStack stack)
    {
        NBTTagCompound tag = stack.getTagCompound();

        if(stack.hasTagCompound() && stack.getTagCompound().hasKey("hue"))
        {
            int hue = stack.getTagCompound().getInteger("hue");
            Color color = hue == -1 ? Color.white : Color.getHSBColor(hue / 360f, 0.5f, 0.5f).brighter();
            return color.getRGB();
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

            //System.out.println("color: " + color);

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
