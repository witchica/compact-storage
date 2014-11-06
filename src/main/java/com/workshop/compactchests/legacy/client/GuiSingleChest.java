package com.workshop.compactchests.legacy.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

/**
 * Created by Toby on 19/08/2014.
 */
public class GuiSingleChest extends GuiChest
{
    public GuiSingleChest(Container serverGuiElement, EntityPlayer player, World world, int x, int y, int z)
    {
        super("single", 176, 169, serverGuiElement, player, world, x, y, z, false);
    }
}
