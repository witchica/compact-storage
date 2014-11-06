package com.workshop.compactchests.legacy.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by Toby on 19/08/2014.
 */
public class ContainerSingleChest extends ContainerChest
{
    public ContainerSingleChest(EntityPlayer player, World world, int x, int y, int z, boolean item)
    {
        super(player, world, x, y, z, item, 9, 3, 8, 18, 8, 87, 8, 145);
    }
}
