package com.workshop.compactchests.legacy.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by Toby on 19/08/2014.
 */
public class ContainerDoubleChest extends ContainerChest
{
    public ContainerDoubleChest(EntityPlayer player, World world, int x, int y, int z, boolean item)
    {
        super(player, world, x, y, z, item, 9, 6, 8, 13, 8, 124, 8, 182);
    }
}
