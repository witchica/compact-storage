package com.tattyseal.compactstorage.client.gui.responder;

import com.tattyseal.compactstorage.CompactStorage;
import com.tattyseal.compactstorage.client.gui.GuiChestBuilder;
import com.tattyseal.compactstorage.packet.MessageUpdateBuilder;
import com.tattyseal.compactstorage.util.StorageInfo;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

/**
 * Created by tobystrong on 02/05/2017.
 */
public class GuiChestBuilderResponder implements GuiPageButtonList.GuiResponder
{
    private  GuiChestBuilder gui;

    public GuiChestBuilderResponder(GuiChestBuilder gui)
    {
        this.gui = gui;
    }

    @Override
    public void setEntryValue(int id, float value)
    {
        StorageInfo info = new StorageInfo(gui.builder.getInfo().getSizeX(), gui.builder.getInfo().getSizeY(), gui.builder.getInfo().getHue(), gui.builder.getInfo().getType());

        switch(id)
        {
            case 0:
            {
                info.setSizeX((int) MathHelper.clamp(value, 1, 24));
                CompactStorage.NETWORK.sendToServer(new MessageUpdateBuilder(gui.pos, info));
                break;
            }
            case 1:
            {
                info.setSizeY((int) MathHelper.clamp(value, 1, 12));
                CompactStorage.NETWORK.sendToServer(new MessageUpdateBuilder(gui.pos, info));
                break;
            }
            case 2:
            {
                info.setHue((int) MathHelper.clamp(value, -1, 360));
                CompactStorage.NETWORK.sendToServer(new MessageUpdateBuilder(gui.pos, info));
                break;
            }
        }
    }

    @Override
    public void setEntryValue(int id, boolean value)
    {
    }

    @Override
    public void setEntryValue(int id, @Nonnull String value) {
    }
}
