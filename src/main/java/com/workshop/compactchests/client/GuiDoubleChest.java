package com.workshop.compactchests.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

/**
 * Created by Toby on 19/08/2014.
 */
public class GuiDoubleChest extends GuiChest
{
    public GuiDoubleChest(Container serverGuiElement, EntityPlayer player, World world, int x, int y, int z)
    {
        super("double", 176, 206, serverGuiElement, player, world, x, y, z, false);
    }
}
