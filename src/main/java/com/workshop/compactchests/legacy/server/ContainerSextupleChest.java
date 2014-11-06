package com.workshop.compactchests.legacy.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by Toby on 19/08/2014.
 */
public class ContainerSextupleChest extends ContainerChest
{
    public ContainerSextupleChest(EntityPlayer player, World world, int x, int y, int z, boolean item)
    {
        super(player, world, x, y, z, item, 18, 9, 9, 5, 89, 177, 89, 235);
    }
}
