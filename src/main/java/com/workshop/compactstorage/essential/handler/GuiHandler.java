package com.workshop.compactstorage.essential.handler;

import com.workshop.compactstorage.client.gui.GuiChest;
import com.workshop.compactstorage.inventory.ContainerChest;
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
            case 0: /* chest */ return new ContainerChest(world, player, new BlockPos(x, y, z));
            default: return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        switch (ID)
        {
            case 0: /* chest */ return new GuiChest((Container) getServerGuiElement(ID, player, world, x, y, z), world, player, new BlockPos(x, y, z));
            default: return null;
        }
    }
}
