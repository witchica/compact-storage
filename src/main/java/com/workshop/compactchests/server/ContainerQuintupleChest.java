package com.workshop.compactchests.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by Toby on 19/08/2014.
 */
public class ContainerQuintupleChest extends ContainerChest
{
    public ContainerQuintupleChest(EntityPlayer player, World world, int x, int y, int z, boolean item)
    {
        super(player, world, x, y, z, item, 15, 9, 27, 5, 80, 177, 80, 235);
    }
}
