package com.workshop.compactstorage.inventory;

import com.workshop.compactstorage.util.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

/**
 * Created by Toby on 11/11/2014.
 */
public class ContainerChest extends Container
{
    public World world;
    public EntityPlayer player;
    public BlockPos pos;

    public ContainerChest(World world, EntityPlayer player, BlockPos pos)
    {
        super();

        this.world = world;
        this.player = player;
        this.pos = pos;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }
}
