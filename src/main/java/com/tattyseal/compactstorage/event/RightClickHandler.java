package com.tattyseal.compactstorage.event;

import com.tattyseal.compactstorage.CompactStorage;
import com.tattyseal.compactstorage.block.BlockChest;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by tobystrong on 05/05/2017.
 */
public class RightClickHandler
{
    @SubscribeEvent
    public void handleRightClick(PlayerInteractEvent.RightClickBlock event)
    {
        if(!event.getWorld().isRemote)
        {
            IBlockState state = event.getWorld().getBlockState(event.getPos());

            if(state != null)
            {
                Block block = state.getBlock();

                if(block != null && block == CompactStorage.chest)
                {
                    event.setUseBlock(Event.Result.ALLOW);
                }
            }
        }
    }
}
