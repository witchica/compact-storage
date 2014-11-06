package com.workshop.compactchests.legacy.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by Toby on 19/08/2014.
 */
public class ContainerQuadrupleChest extends ContainerChest
{
    public ContainerQuadrupleChest(EntityPlayer player, World world, int x, int y, int z, boolean item)
    {
        super(player, world, x, y, z, item, 12, 9, 53, 5, 80, 177, 80, 235);
    }
}
