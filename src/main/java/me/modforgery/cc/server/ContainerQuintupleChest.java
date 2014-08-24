package me.modforgery.cc.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.world.World;

/**
 * Created by Toby on 19/08/2014.
 */
public class ContainerQuintupleChest extends ContainerChest
{
    public ContainerQuintupleChest(EntityPlayer player, World world, int x, int y, int z)
    {
        super(player, world, x, y, z);
    }

    @Override
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

        /***
         * Left
         */

        for(int x = 0; x < 2; x++)
        {
            for(int y = 0; y < 9; y++)
            {
                Slot slot = new Slot(inventory, 9 * 9 + (x + y * 2), 6 + (x * 18), 13 + (y * 18));
                addSlotToContainer(slot);
            }
        }

        for(int x = 0; x < 2; x++)
        {
            for(int y = 0; y < 3; y++)
            {
                Slot slot = new Slot(inventory, 9 * 9 + 18 + (x + y * 2), 6 + (x * 18), 177 + (y * 18));
                addSlotToContainer(slot);
            }
        }

        for(int x = 0; x < 2; x++)
        {
            Slot slot = new Slot(inventory, 9 * 9 + 18 + 6 + (x * 2), 6 + (x * 18), 235);
            addSlotToContainer(slot);
        }

        /***
         * Right
         */

        for(int x = 0; x < 2; x++)
        {
            for(int y = 0; y < 9; y++)
            {
                Slot slot = new Slot(inventory, 9 * 9 + 18 + 6 + 2 + (x + y * 2), 217 + (x * 18), 13 + (y * 18));
                addSlotToContainer(slot);
            }
        }

        for(int x = 0; x < 2; x++)
        {
            for(int y = 0; y < 3; y++)
            {
                Slot slot = new Slot(inventory, 9 * 9 + 18 + 6 + 2 + 18 + (x + y * 2), 217 + (x * 18), 177 + (y * 18));
                addSlotToContainer(slot);
            }
        }

        for(int x = 0; x < 2; x++)
        {
            Slot slot = new Slot(inventory, 9 * 9 + 18 + 6 + 2 + 18 + 6 + (x * 2), 217 + (x * 18), 235);
            addSlotToContainer(slot);
        }
    }
}
