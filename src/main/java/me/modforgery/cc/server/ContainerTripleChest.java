package me.modforgery.cc.server;

import me.modforgery.cc.CompactChests;
import me.modforgery.cc.network.ChestPacket;
import me.modforgery.cc.tileentity.TileEntityChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Created by Toby on 19/08/2014.
 */
public class ContainerTripleChest extends ContainerChest
{
    public ContainerTripleChest(EntityPlayer player, World world, int x, int y, int z)
    {
        super(player, world, x, y, z);
    }

    public void initializeContainer(EntityPlayer player, IInventory inventory)
    {
        for(int slot = 0; slot < 9; slot++)
        {
            Slot s = new Slot(player.inventory, slot, 48 + (slot * 18), 235);
            addSlotToContainer(s);
        }

        for(int x = 0; x < 9; x++)
        {
            for(int y = 0; y < 3; y++)
            {
                Slot s = new Slot(player.inventory, x + y * 9 + 9, 48 + (x * 18), 177 + (y * 18));
                addSlotToContainer(s);
            }
        }

        for(int x = 0; x < 9; x++)
        {
            for(int y = 0; y < 9; y++)
            {
                Slot slot = new Slot(inventory, x + y * 9, 48 + (x * 18), 13 + (y * 18));

                addSlotToContainer(slot);
            }
        }
    }
}
