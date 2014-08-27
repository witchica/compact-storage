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

    public TileEntityChest chest;

    public ArrayList<Slot> playerSlots;
    public ArrayList<Slot> chestSlots;

    public ContainerChest(EntityPlayer player, World world, int x, int y, int z)
    {
        this.player = player;
        this.world = world;

        this.x = x;
        this.y = y;
        this.z = z;

        playerSlots = new ArrayList<Slot>();
        chestSlots = new ArrayList<Slot>();

        ((TileEntityChest) world.getTileEntity(x, y, z)).openInventory();

        chest = (TileEntityChest) world.getTileEntity(x, y, z);

        initializeContainer(player, (IInventory) world.getTileEntity(x, y, z));
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer p, int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot) inventorySlots.get(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i < chest.getSizeInventory()) {
                if (!mergeItemStack(itemstack1, chest.getSizeInventory(), inventorySlots.size(), true)) {
                    return null;
                }
            } else if (!mergeItemStack(itemstack1, 0, chest.getSizeInventory(), false)) {
                return null;
            }
            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
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
}
