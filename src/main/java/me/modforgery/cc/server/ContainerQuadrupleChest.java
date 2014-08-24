package me.modforgery.cc.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.world.World;

/**
 * Created by Toby on 19/08/2014.
 */
public class ContainerQuadrupleChest extends ContainerChest
{
    public ContainerQuadrupleChest(EntityPlayer player, World world, int x, int y, int z)
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

        for(int slot = 0; slot < 9; slot++)
        {
            Slot s = new Slot(inventory, 9 * 9 + 1 + slot, 24, 13 + (slot * 18));

            addSlotToContainer(s);
        }

        for(int slot = 0; slot < 3; slot++)
        {
            Slot s = new Slot(inventory, 9 * 9 + 1 + 9 + slot, 24, 177 + (slot * 18));

            addSlotToContainer(s);
        }

        Slot slot1 = new Slot(inventory, 9 * 9 + 1 + 9 + 3 + 1, 24, 235);
        addSlotToContainer(slot1);

        /***
         * Right
         */

        for(int slot = 0; slot < 9; slot++)
        {
            Slot s = new Slot(inventory, 9 * 9 + 1 + 14 + slot, 217, 13 + (slot * 18));

            addSlotToContainer(s);
        }

        for(int slot = 0; slot < 3; slot++)
        {
            Slot s = new Slot(inventory, 9 * 9 + 1 + 14 + 9 + slot, 217, 177 + (slot * 18));

            addSlotToContainer(s);
        }

        Slot slot2 = new Slot(inventory, 9 * 9 + 1 + 14 + 9 + 3 + 1, 217, 235);
        addSlotToContainer(slot2);

    }
}
