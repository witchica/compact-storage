package com.workshop.compactchests.legacy.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

/**
 * Created by Toby on 19/08/2014.
 */
public class GuiQuintupleChest extends GuiChest
{
    public GuiQuintupleChest(Container serverGuiElement, EntityPlayer player, World world, int x, int y, int z)
    {
        super("quintuple", 320, 256, serverGuiElement, player, world, x, y, z, true);
    }
}
