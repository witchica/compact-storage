package com.workshop.compactchests.item;

import com.workshop.compactchests.block.BlockChest;
import com.workshop.compactstorage.essential.init.StorageItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.workshop.compactstorage.essential.CompactStorage;

/**
 * Created by Toby on 27/08/2014.
 */
public class ItemBackpack extends Item
{
    public int guiID;

    public ItemBackpack(int guiID)
    {
        super();

        this.guiID = guiID;
        setUnlocalizedName("backpack_" + guiID);
        setTextureName("compactchests:backpack_" + guiID);
        //setCreativeTab(CompactStorage.tabCS);
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        NBTTagCompound tag = stack.getTagCompound();
        ItemStack newStack = new ItemStack(StorageItems.backpack, 1);

        int color = guiID == 0 ? 0xFFFFFF : BlockChest.colors[guiID - 1];
        int[] size = guiID == 0 ? new int[] {9, 3} : BlockChest.size[guiID - 1];

        if(tag == null)
        {
            NBTTagCompound newTag = new NBTTagCompound();
            newTag.setIntArray("size", size);
            newTag.setInteger("color", color);
            newStack.setTagCompound(newTag);

            return newStack;
        }

        tag.setInteger("color", color);
        tag.setIntArray("size", size);

        newStack.setTagCompound(tag);

        return newStack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack p_77626_1_) {
        return 1;
    }
}
