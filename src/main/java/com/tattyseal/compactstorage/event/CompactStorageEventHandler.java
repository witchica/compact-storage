package com.tattyseal.compactstorage.event;

import com.tattyseal.compactstorage.block.BlockChest;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CompactStorageEventHandler 
{
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event)
	{
		if(event.getEntity().world.getBlockState(event.getPos()).getBlock() instanceof BlockChest)
		{
			event.setUseBlock(Result.ALLOW);
		}
	}
}
