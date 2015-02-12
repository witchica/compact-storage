package com.workshop.compactstorage.essential.handler;

import com.workshop.compactstorage.api.IChest;
import com.workshop.compactstorage.client.gui.GuiChest;
import com.workshop.compactstorage.client.gui.GuiChestBuilder;
import com.workshop.compactstorage.essential.init.StorageItems;
import com.workshop.compactstorage.inventory.ContainerChest;
import com.workshop.compactstorage.inventory.ContainerChestBuilder;
import com.workshop.compactstorage.inventory.InventoryBackpack;
import com.workshop.compactstorage.util.BlockPos;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

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

                if(player.getHeldItem() != null && player.getHeldItem().getItem().equals(StorageItems.backpack))
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

                if(player.getHeldItem() != null && player.getHeldItem().getItem() == StorageItems.backpack)
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
