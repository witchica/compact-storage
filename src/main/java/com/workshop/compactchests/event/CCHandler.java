package com.workshop.compactchests.event;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import com.workshop.compactchests.block.BlockChest;
import com.workshop.compactchests.init.ChestBlocks;
import com.workshop.compactchests.tileentity.TileEntityChest;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Toby on 18/09/2014.
 */
public class CCHandler
{
    @SubscribeEvent
    public void onEntity(EntityJoinWorldEvent event)
    {
        if(event.entity instanceof EntityTNTPrimed)
        {
            EntityTNTPrimed tnt = (EntityTNTPrimed) event.entity;

            if(event.world.getTileEntity((int) tnt.posX, (int) tnt.posY - 1, (int) tnt.posZ) instanceof TileEntityChest)
            {
                TileEntityChest chest = (TileEntityChest) event.world.getTileEntity((int) tnt.posX, (int) tnt.posY - 1, (int) tnt.posZ);

                ItemStack[] items = chest.items;

                BlockChest block = (BlockChest) event.world.getBlock((int) tnt.posX, (int) tnt.posY - 1, (int) tnt.posZ);

                Block upgradeTo = ChestBlocks.doubleChest;

                if(block == ChestBlocks.doubleChest)
                {
                    upgradeTo = ChestBlocks.tripleChest;
                }
                else if(block == ChestBlocks.tripleChest)
                {
                    upgradeTo = ChestBlocks.quadrupleChest;
                }
                else if(block == ChestBlocks.quadrupleChest)
                {
                    upgradeTo = ChestBlocks.quintupleChest;
                }
                else if(block == ChestBlocks.quintupleChest)
                {
                    return;
                }

                event.world.createExplosion(tnt, tnt.posX, tnt.posY, tnt.posZ, 2f, false);
                tnt.setDead();

                event.world.setBlock((int) tnt.posX, (int) tnt.posY - 1, (int) tnt.posZ, Blocks.air);
                event.world.setTileEntity((int) tnt.posX, (int) tnt.posY - 1, (int) tnt.posZ, null);

                event.world.setBlock((int) tnt.posX, (int) tnt.posY - 1, (int) tnt.posZ, upgradeTo);
                //((TileEntityChest) event.world.getTileEntity((int) tnt.posX, (int) tnt.posY - 1, (int) tnt.posZ)).items = items;
            }
        }
    }
}
