package me.modforgery.cc.server;

import me.modforgery.cc.tileentity.TileEntityChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Created by Toby on 19/08/2014.
 */
public abstract class ContainerChest extends Container
{
    public EntityPlayer player;
    public World world;

    public int x;
    public int y;
    public int z;

    public ContainerChest(EntityPlayer player, World world, int x, int y, int z)
    {
        this.player = player;
        this.world = world;

        this.x = x;
        this.y = y;
        this.z = z;

        ((TileEntityChest) world.getTileEntity(x, y, z)).openInventory();

        initializeContainer(player, (IInventory) world.getTileEntity(x, y, z));
    }

    @Override
    public void onContainerClosed(EntityPlayer player)
    {
        super.onContainerClosed(player);
        ((TileEntityChest) world.getTileEntity(x, y, z)).closeInventory();
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }

    public abstract void initializeContainer(EntityPlayer player, IInventory inventory);

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot)
    {
        return null;
    }
}
