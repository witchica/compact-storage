package com.workshop.compactchests.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

import com.workshop.compactchests.server.ContainerDoubleChest;
import com.workshop.compactchests.server.ContainerQuadrupleChest;
import com.workshop.compactchests.server.ContainerQuintupleChest;
import com.workshop.compactchests.server.ContainerSextupleChest;
import com.workshop.compactchests.server.ContainerSingleChest;
import com.workshop.compactchests.server.ContainerTripleChest;

import cpw.mods.fml.common.network.IGuiHandler;

/**
 * Created by Toby on 19/08/2014.
 */
public class GuiHandler implements IGuiHandler
{
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        switch(ID)
        {
            case 0: return new ContainerDoubleChest(player, world, x, y, z, false);
            case 1: return new ContainerTripleChest(player, world, x, y, z, false);
            case 2: return new ContainerQuadrupleChest(player, world, x, y, z, false);
            case 3: return new ContainerQuintupleChest(player, world, x, y, z, false);
            case 4: return new ContainerSextupleChest(player, world, x, y, z, false);
            case 100: return new ContainerSingleChest(player, world, x, y, z, true);
            case 101: return new ContainerDoubleChest(player, world, x, y, z, true);
            case 102: return new ContainerTripleChest(player, world, x, y, z, true);
            case 103: return new ContainerQuadrupleChest(player, world, x, y, z, true);
            case 104: return new ContainerQuintupleChest(player, world, x, y, z, true);
            case 105: return new ContainerSextupleChest(player, world, x, y, z, true);
            default: return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        switch(ID)
        {
            case 0: return new GuiDoubleChest((Container) getServerGuiElement(ID, player, world, x, y, z), player, world, x, y, z);
            case 1: return new GuiTripleChest((Container) getServerGuiElement(ID, player, world, x, y, z), player, world, x, y, z);
            case 2: return new GuiQuadrupleChest((Container) getServerGuiElement(ID, player, world, x, y, z), player, world, x, y, z);
            case 3: return new GuiQuintupleChest((Container) getServerGuiElement(ID, player, world, x, y, z), player, world, x, y, z);
            case 4: return new GuiSextupleChest((Container) getServerGuiElement(ID, player, world, x, y, z), player, world, x, y, z);
            case 100: return new GuiSingleChest((Container) getServerGuiElement(ID, player, world, x, y, z), player, world, x, y, z);
            case 101: return new GuiDoubleChest((Container) getServerGuiElement(ID, player, world, x, y, z), player, world, x, y, z);
            case 102: return new GuiTripleChest((Container) getServerGuiElement(ID, player, world, x, y, z), player, world, x, y, z);
            case 103: return new GuiQuadrupleChest((Container) getServerGuiElement(ID, player, world, x, y, z), player, world, x, y, z);
            case 104: return new GuiQuintupleChest((Container) getServerGuiElement(ID, player, world, x, y, z), player, world, x, y, z);
            case 105: return new GuiSextupleChest((Container) getServerGuiElement(ID, player, world, x, y, z), player, world, x, y, z);
            default: return null;
        }
    }
}
