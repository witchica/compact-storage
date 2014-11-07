package com.workshop.compactchests.item;

import com.workshop.compactstorage.essential.CompactStorage;
import com.workshop.compactchests.creativetabs.CreativeTabChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        player.openGui(CompactStorage.instance, 100 + guiID, world, (int) player.posX, (int) player.posY, (int) player.posZ);
        return stack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack p_77626_1_) {
        return 1;
    }
}
