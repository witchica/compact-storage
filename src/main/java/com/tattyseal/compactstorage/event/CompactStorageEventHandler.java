package com.tattyseal.compactstorage.event;

import com.tattyseal.compactstorage.block.BlockBarrel;
import com.tattyseal.compactstorage.block.BlockChest;

import net.minecraft.block.Block;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CompactStorageEventHandler 
{
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event)
	{
		Block block = event.getEntity().world.getBlockState(event.getPos()).getBlock();

		if(block instanceof BlockChest || block instanceof BlockBarrel)
		{
			event.setUseBlock(Result.ALLOW);
		}
	}
}
