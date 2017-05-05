package com.tattyseal.compactstorage.tileentity;

import com.tattyseal.compactstorage.ConfigurationHandler;
import com.tattyseal.compactstorage.api.IChest;
import com.tattyseal.compactstorage.block.BlockChest;
import com.tattyseal.compactstorage.util.StorageInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;

import java.awt.*;

/**
 * Created by Toby on 06/11/2014.
 */
public class TileEntityChest extends TileEntity implements IInventory, IChest, ITickable
{
    public EnumFacing direction;

    public Color color;
    public StorageInfo info;

    public int invX;
    public int invY;

    public boolean init;

    public float lidAngle;
    /** The angle of the lid last tick */
    public float prevLidAngle;
    /** The number of players currently using this chest */
    public int numPlayersUsing;
    /** Server sync counter (once per 20 ticks) */
    private int ticksSinceSync;

    private boolean retaining;
    
    public ItemStack[] items;

    public TileEntityChest()
    {
        super();

        this.retaining = false;
        this.direction = EnumFacing.NORTH;
        this.items = new ItemStack[getSizeInventory()];
        this.info = new StorageInfo(getInvX(), getInvY(), 180, StorageInfo.Type.CHEST);

        for(int i = 0; i < items.length; i++)
        {
            items[i] = ItemStack.EMPTY;
        }
    }

    /* INVENTORY START */
    @Override
    public int getSizeInventory() 
    {
        return invX * invY;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getStackInSlot(int slot) 
    {
    	if(slot < items.length && items[slot] != null && !items[slot].isEmpty())
        {
        	return items[slot];
        }
        
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount)
    {
    	ItemStack stack = getStackInSlot(slot);

        if(stack != ItemStack.EMPTY)
        {
            if(stack.getCount() <= amount)
            {
                setInventorySlotContents(slot, ItemStack.EMPTY);
                markDirty();
                
                return stack.copy();
            }
            else
            {
                ItemStack stack2 = stack.splitStack(amount);
                markDirty();
                
                return stack2.copy();
            }
        }

        return stack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        items[index] = ItemStack.EMPTY;
        return ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) 
    {
    	if(items != null && slot < items.length)
        {
            items[slot] = stack;
            markDirty();
        }
    }

    @Override
    public String getName()
    {
        return "compactChest.inv";
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit() 
    {
        return 64;
    }


    public boolean receiveClientEvent(int id, int type)
    {
        if (id == 1)
        {
            this.numPlayersUsing = type;
            return true;
        }
        else
        {
            return super.receiveClientEvent(id, type);
        }
    }

    @Override
    public void openInventory(EntityPlayer player) {
        if (!player.isSpectator())
        {
            if (this.numPlayersUsing < 0)
            {
                this.numPlayersUsing = 0;
            }

            ++this.numPlayersUsing;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
        }
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        if (!player.isSpectator() && this.getBlockType() instanceof BlockChest)
        {
            --this.numPlayersUsing;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
        }
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) 
    {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public void markDirty()
    {
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        super.markDirty();
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    /* CUSTOM START */
    
    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        if(tag.hasKey("facing")) this.direction = EnumFacing.getFront(tag.getInteger("facing"));

        if(tag.hasKey("retaining"))
        {
            this.retaining = tag.getBoolean("retaining");
        }
        else
        {
            this.retaining = false;
        }

        if(tag.hasKey("hue"))
        {
            info.setHue(tag.getInteger("hue"));
            this.color = getHue() == -1 ? Color.white : Color.getHSBColor(info.getHue() / 360f, 0.5f, 0.5f);
        }
        else
        {
            if(tag.hasKey("color"))
            {
                String color = "";

                if(tag.getTag("color") instanceof NBTTagInt)
                {
                    color = String.format("#%06X", (0xFFFFFF & tag.getInteger("color")));
                }
                else
                {
                    color = tag.getString("color");
                }

                System.out.println("color: " + color);

                if(color.startsWith("0x"))
                {
                    color = "#" + color.substring(2, color.length());
                }

                if(!color.isEmpty())
                {
                    float[] hsbVals = new float[3];

                    this.color = Color.decode(color);

                    hsbVals = Color.RGBtoHSB(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), hsbVals);
                    this.info.setHue((int) (hsbVals[0] * 360));

                    tag.removeTag("color");
                }
                else
                {
                    this.color = Color.white;
                    this.info.setHue(180);
                    tag.removeTag("color");
                }
            }
        }

        this.invX = tag.getInteger("invX");
        this.invY = tag.getInteger("invY");
        
        NBTTagList nbtTagList = tag.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        items = new ItemStack[getSizeInventory()];

        for(int slot = 0; slot < nbtTagList.tagCount(); slot++)
        {
            NBTTagCompound item = nbtTagList.getCompoundTagAt(slot);

            int i = item.getInteger("Slot");

            if(i >= 0 && i < getSizeInventory())
            {
                items[i] = new ItemStack(item);
            }
        }
    }

    @Override
    public NBTTagCompound getTileData()
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag = writeToNBT(tag);

        return tag;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag = writeToNBT(tag);

        return tag;
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag = writeToNBT(tag);

        return tag;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        if(direction != null) tag.setInteger("facing", direction.ordinal());
        tag.setInteger("hue", info.getHue());
        tag.setInteger("invX", invX);
        tag.setInteger("invY", invY);
        tag.setBoolean("retaining", retaining);
        
        NBTTagList nbtTagList = new NBTTagList();
        for(int slot = 0; slot < getSizeInventory(); slot++)
        {
            if(slot < items.length && items[slot] != null && !items[slot].isEmpty())
            {
                NBTTagCompound item = new NBTTagCompound();
                item.setInteger("Slot", slot);
                items[slot].writeToNBT(item);
                nbtTagList.appendTag(item);
            }
        }

        tag.setTag("Items", nbtTagList);

        return tag;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);

        return new SPacketUpdateTileEntity(pos, getBlockMetadata(), tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
    }

    public void setDirection(EnumFacing direction)
    {
        this.direction = direction;
        updateBlock();
    }

    public void setDirection(int direction)
    {
        this.direction = EnumFacing.getFront(direction);
        updateBlock();
    }

    public void updateBlock()
    {
        markDirty();
    }

    @Override
    public void update()
    {
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        ++this.ticksSinceSync;

        if (!this.world.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + i + j + k) % 200 == 0)
        {
            this.numPlayersUsing = 0;
            float f = 5.0F;

            for (EntityPlayer entityplayer : this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB((double)((float)i - 5.0F), (double)((float)j - 5.0F), (double)((float)k - 5.0F), (double)((float)(i + 1) + 5.0F), (double)((float)(j + 1) + 5.0F), (double)((float)(k + 1) + 5.0F))))
            {
                if (entityplayer.openContainer instanceof ContainerChest)
                {
                    IInventory iinventory = ((ContainerChest)entityplayer.openContainer).getLowerChestInventory();

                    if (iinventory == this || iinventory instanceof InventoryLargeChest && ((InventoryLargeChest)iinventory).isPartOfLargeChest(this))
                    {
                        ++this.numPlayersUsing;
                    }
                }
            }
        }

        this.prevLidAngle = this.lidAngle;
        float f1 = 0.1F;

        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F)
        {
            double d1 = (double)i + 0.5D;
            double d2 = (double)k + 0.5D;

            this.world.playSound((EntityPlayer)null, d1, (double)j + 0.5D, d2, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F)
        {
            float f2 = this.lidAngle;

            if (this.numPlayersUsing > 0)
            {
                this.lidAngle += 0.1F;
            }
            else
            {
                this.lidAngle -= 0.1F;
            }

            if (this.lidAngle > 1.0F)
            {
                this.lidAngle = 1.0F;
            }

            float f3 = 0.5F;

            if (this.lidAngle < 0.5F && f2 >= 0.5F)
            {
                double d3 = (double)i + 0.5D;
                double d0 = (double)k + 0.5D;

                this.world.playSound((EntityPlayer)null, d3, (double)j + 0.5D, d0, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.lidAngle < 0.0F)
            {
                this.lidAngle = 0.0F;
            }
        }
    }

    @Override
    public int getInvX()
    {
        return invX;
    }

    @Override
    public int getInvY()
    {
        return invY;
    }

    @Override
    public StorageInfo getInfo()
    {
        return info;
    }

    @Override
    public int getColor()
    {
        return 0;
    }

    @Override
    public boolean shouldConnectToNetwork()
    {
        return ConfigurationHandler.shouldConnect;
    }

    @Override
    public boolean getRetaining() {
        return retaining;
    }

    @Override
    public void setRetaining(boolean retaining)
    {
        this.retaining = retaining;
    }

    @Override
    public int getHue()
    {
        return info.getHue();
    }

    @Override
    public void setHue(int hue)
    {
        info.setHue(hue);
    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }
}
