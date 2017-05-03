package com.tattyseal.compactstorage.event;

import com.tattyseal.compactstorage.ConfigurationHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by tobystrong on 02/05/2017.
 */
public class ConnectionHandler
{
    @SubscribeEvent
    public void connect(PlayerEvent.PlayerLoggedInEvent e)
    {
        if(FMLCommonHandler.instance().getSide().equals(Side.CLIENT) && ConfigurationHandler.newFeatures)
        {
            e.player.sendMessage(new TextComponentString("What's new in CompactStorage 2.2?"));
            e.player.sendMessage(new TextComponentString(" - New retainer chests, regular compact chests can be made to retain items by crouch clicking on them with a diamond."));
            e.player.sendMessage(new TextComponentString(" - New builder interface redesign."));
            e.player.sendMessage(new TextComponentString("This message will only be displayed once and can be re-enabled in the configuration file."));
            ConfigurationHandler.disableMessage();
        }
    }
}
