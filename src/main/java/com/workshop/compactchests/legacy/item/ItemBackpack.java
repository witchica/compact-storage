package com.workshop.compactchests.legacy.item;

import com.workshop.compactchests.essential.CompactStorage;
import com.workshop.compactchests.legacy.CompactChests;
import com.workshop.compactchests.legacy.creativetabs.CreativeTabChest;
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
        setCreativeTab(CreativeTabChest.tab);
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
