package com.tattyseal.compactstorage.proxy;

import com.tattyseal.compactstorage.CompactStorage;
import com.tattyseal.compactstorage.block.BlockChest;
import com.tattyseal.compactstorage.client.render.TileEntityChestRenderer;
import com.tattyseal.compactstorage.event.GuiOverlayEvent;
import com.tattyseal.compactstorage.item.ItemBackpack;
import com.tattyseal.compactstorage.item.ItemBlockChest;
import com.tattyseal.compactstorage.tileentity.TileEntityChest;
import com.tattyseal.compactstorage.util.ModelUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

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
            public int getColorFromItemstack(ItemStack stack, int color)
            {
                return getColorFromNBT(stack);
            }
        }, backpack);

        itemColors.registerItemColorHandler(new IItemColor() {
            @Override
            public int getColorFromItemstack(ItemStack stack, int color)
            {
                return getColorFromNBT(stack);
            }
        }, ibChest);

        itemColors.registerItemColorHandler(new IItemColor() {
            @Override
            public int getColorFromItemstack(ItemStack stack, int color)
            {
                return getColorFromNBT(stack);
            }
        }, chest);

        //MinecraftForge.EVENT_BUS.register(new GuiOverlayEvent());
    }

    private int getColorFromNBT(ItemStack stack)
    {
        if(stack.hasTagCompound() && stack.getTagCompound().hasKey("color"))
        {
            if(stack.getTagCompound().getTag("color") instanceof NBTTagString)
            {
                return Integer.decode(stack.getTagCompound().getString("color"));
            }
            else if(stack.getTagCompound().getTag("color") instanceof NBTTagInt)
            {
                return stack.getTagCompound().getInteger("color");
            }
        }

        return 0xFFFFFF;
    }
}
