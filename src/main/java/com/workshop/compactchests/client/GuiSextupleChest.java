package com.workshop.compactchests.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

/**
 * Created by Toby on 19/08/2014.
 */
public class GuiSextupleChest extends GuiChest
{
    public GuiSextupleChest(Container serverGuiElement, EntityPlayer player, World world, int x, int y, int z)
    {
        super("sextuple", 338, 256, serverGuiElement, player, world, x, y, z, true);
    }
}
