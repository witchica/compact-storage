package com.workshop.compactchests.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by Toby on 19/08/2014.
 */
public class ContainerTripleChest extends ContainerChest
{
    public ContainerTripleChest(EntityPlayer player, World world, int x, int y, int z, boolean item)
    {
        super(player, world, x, y, z, item, 9, 9, 8, 7, 8, 174, 8, 232);
    }
}
