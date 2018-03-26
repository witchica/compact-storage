package com.tattyseal.compactstorage;

import com.tattyseal.compactstorage.api.IChest;
import com.tattyseal.compactstorage.client.gui.GuiChest;
import com.tattyseal.compactstorage.client.gui.GuiChestBuilder;
import com.tattyseal.compactstorage.inventory.ContainerChest;
import com.tattyseal.compactstorage.inventory.ContainerChestBuilder;
import com.tattyseal.compactstorage.inventory.InventoryBackpack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * Created by Toby on 09/11/2014.
 */
public class GuiHandler implements IGuiHandler
{
    private static final int GUI_CHEST = 0;
    private static final int GUI_CHEST_BUILDER = 1;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);

        switch (ID)
        {
            case GUI_CHEST:
            {
                // chest or backpack
                IChest chest;

                net.minecraft.tileentity.TileEntity entity = world.getTileEntity(pos);

                if (entity instanceof IChest)
                {
                    chest = (IChest)entity;
                }
                else if (player.getHeldItem(EnumHand.MAIN_HAND).getItem().equals(CompactStorage.backpack))
                {
                    chest = new InventoryBackpack(player.getHeldItem(EnumHand.MAIN_HAND));
                }
                // this shouldn't happen
                else return null;

                return new ContainerChest(world, chest, player, pos);
            }
            case GUI_CHEST_BUILDER: return new ContainerChestBuilder(world, player, pos);
            default: return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);

        switch (ID)
        {
            case GUI_CHEST:
            {
                 // chest or backpack
                IChest chest;

                net.minecraft.tileentity.TileEntity entity = world.getTileEntity(pos);

                if (entity instanceof IChest)
                {
                    chest = (IChest)entity;
                }
                else if (player.getHeldItem(EnumHand.MAIN_HAND).getItem().equals(CompactStorage.backpack))
                {
                    chest = new InventoryBackpack(player.getHeldItem(EnumHand.MAIN_HAND));
                }
                // this shouldn't happen
                else return null;

                return new GuiChest((Container) getServerGuiElement(ID, player, world, x, y, z), chest, world, player, pos);
            }
            case GUI_CHEST_BUILDER: return new GuiChestBuilder((Container) getServerGuiElement(ID, player, world, x, y, z), world, player, pos);
            default: return null;
        }
    }
}
