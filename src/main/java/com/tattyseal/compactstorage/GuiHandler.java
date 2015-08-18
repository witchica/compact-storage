package com.tattyseal.compactstorage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

import com.tattyseal.compactstorage.api.IChest;
import com.tattyseal.compactstorage.client.gui.GuiChest;
import com.tattyseal.compactstorage.client.gui.GuiChestBuilder;
import com.tattyseal.compactstorage.inventory.ContainerChest;
import com.tattyseal.compactstorage.inventory.ContainerChestBuilder;
import com.tattyseal.compactstorage.inventory.InventoryBackpack;
import com.tattyseal.compactstorage.util.BlockPos;

import cpw.mods.fml.common.network.IGuiHandler;

/**
 * Created by Toby on 09/11/2014.
 */
public class GuiHandler implements IGuiHandler
{
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {

        switch (ID)
        {
            case 0:
            {
                /* chest or backpack*/
                IChest chest;

                if(player.getHeldItem() != null && player.getHeldItem().getItem().equals(CompactStorage.backpack))
                {
                    chest = new InventoryBackpack(player.getHeldItem());
                }
                else
                {
                    chest = (IChest) world.getTileEntity(x, y, z);
                }

                return new ContainerChest(world, chest, player, new BlockPos(x, y, z));
            }
            case 1: /* chest builder */ return new ContainerChestBuilder(world, player, new BlockPos(x, y, z));
            default: return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        switch (ID)
        {
            case 0:
            {
                 /* chest or backpack*/
                IChest chest;

                if(player.getHeldItem() != null && player.getHeldItem().getItem() == CompactStorage.backpack)
                {
                    chest = new InventoryBackpack(player.getHeldItem());
                }
                else
                {
                    chest = (IChest) world.getTileEntity(x, y, z);
                }

                return new GuiChest((Container) getServerGuiElement(ID, player, world, x, y, z), chest, world, player, new BlockPos(x, y, z));
            }
            case 1: /* chest builder */ return new GuiChestBuilder((Container) getServerGuiElement(ID, player, world, x, y, z), world, player, new BlockPos(x, y, z));
            default: return null;
        }
    }
}
