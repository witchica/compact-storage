package me.modforgery.cc.client;

import cpw.mods.fml.common.network.IGuiHandler;
import me.modforgery.cc.server.ContainerDoubleChest;
import me.modforgery.cc.server.ContainerQuadrupleChest;
import me.modforgery.cc.server.ContainerQuintupleChest;
import me.modforgery.cc.server.ContainerTripleChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

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
            case 0: return new ContainerDoubleChest(player, world, x, y, z);
            case 1: return new ContainerTripleChest(player, world, x, y, z);
            case 2: return new ContainerQuadrupleChest(player, world, x, y, z);
            case 3: return new ContainerQuintupleChest(player, world, x, y, z);
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
            default: return null;
        }
    }
}
