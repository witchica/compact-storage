package com.tattyseal.compactstorage.inventory;

import com.tattyseal.compactstorage.inventory.slot.SlotChestBuilder;
import com.tattyseal.compactstorage.inventory.slot.SlotUnplaceable;
import com.tattyseal.compactstorage.tileentity.TileEntityChestBuilder;
import com.tattyseal.compactstorage.util.StorageInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * Created by Toby on 11/11/2014.
 */
public class ContainerChestBuilder extends Container
{
    public World world;
    public EntityPlayer player;
    public BlockPos pos;

    public TileEntityChestBuilder chest;
    
    /***
     * This is carried over from the GUI for slot placement issues
     */
    private int xSize;
    private int ySize;
    
    public ContainerChestBuilder(World world, EntityPlayer player, BlockPos pos)
    {
        super();

        this.world = world;
        this.player = player;
        this.pos = pos;
        this.chest = ((TileEntityChestBuilder) world.getTileEntity(pos));
        
        this.xSize = 7 + 162 + 7;
        this.ySize = 7 + 108 + 13 + 54 + 4 + 18 + 7;
        
        setupSlots();
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer player)
    {
        return true;
    }
    
    private void setupSlots()
    {
        int slotY =  50 + 12;
        int slotX = ((xSize / 2) - 36);

        for(int x = 0; x < 4; x++)
        {
        	SlotChestBuilder slot = new SlotChestBuilder(chest, x, slotX + (x * 18) + 1, slotY + 21);
            addSlotToContainer(slot);
        }

        SlotUnplaceable chestSlot = new SlotUnplaceable(chest, 4, 5 + xSize - 29, 8 + 108 - 12);
        addSlotToContainer(chestSlot);

        slotX = (xSize / 2) - ((9 * 18) / 2) + 1;
        slotY = 8 + 108 + 10;

        for(int x = 0; x < 9; x++)
        {
            for(int y = 0; y < 3; y++)
            {
            	Slot slot = new Slot(player.inventory, x + y * 9 + 9, slotX + (x * 18), slotY + (y * 18));
                addSlotToContainer(slot);
            }
        }

        slotY = slotY + (3 * 18) + 4;

        for(int x = 0; x < 9; x++)
        {
        	Slot slot = new Slot(player.inventory, x, slotX + (x * 18), slotY);
            addSlotToContainer(slot);
        }
    }
    
    @Override
    @Nonnull
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
    {
        try
        {
            Slot slot = inventorySlots.get(slotIndex);

            if (slot != null && slot.getHasStack())
            {
                ItemStack itemStack1 = slot.getStack();
                ItemStack itemStack = itemStack1.copy();

                if (slotIndex < 5)
                {
                    if (!this.mergeItemStack(itemStack1, 5, 5 + 36, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (!this.mergeItemStack(itemStack1, 0, 5, false))
                {
                    return ItemStack.EMPTY;
                }

                if (itemStack1.getCount() == 0)
                {
                    slot.putStack(ItemStack.EMPTY);
                }
                else
                {
                    slot.onSlotChanged();
                }
                return itemStack;
            }

            return ItemStack.EMPTY;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendWindowProperty(this, 0, 9);
        listener.sendWindowProperty(this, 1, 3);
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (IContainerListener player : this.listeners) {
            if (chest != null && chest.info != null) player.sendWindowProperty(this, 0, chest.info.getSizeX());
            if (chest != null && chest.info != null) player.sendWindowProperty(this, 1, chest.info.getSizeY());
            if (chest != null && chest.info != null) player.sendWindowProperty(this, 2, chest.info.getHue());
            if (chest != null && chest.info != null) player.sendWindowProperty(this, 3, chest.info.getType().ordinal());
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int id, int value)
    {
    	if(chest.info == null) chest.info = new StorageInfo(9, 3, 180, StorageInfo.Type.CHEST);

        switch(id)
        {
        	case 0: chest.info.setSizeX(value); break;
        	case 1: chest.info.setSizeY(value); break;
            case 2: chest.info.setHue(value); break;
            case 3: chest.info.setType(StorageInfo.Type.values()[value]); break;
        }
    }
}
