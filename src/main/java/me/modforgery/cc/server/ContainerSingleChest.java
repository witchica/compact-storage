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
public class ContainerSingleChest extends ContainerChest
{
    public ContainerSingleChest(EntityPlayer player, World world, int x, int y, int z, boolean item)
    {
        super(player, world, x, y, z, item, 9, 3, 8, 18, 8, 87, 8, 145);
    }
}
