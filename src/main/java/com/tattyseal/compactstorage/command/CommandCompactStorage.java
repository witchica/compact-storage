package com.tattyseal.compactstorage.command;

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

import com.tattyseal.compactstorage.ConfigurationHandler;

import cpw.mods.fml.common.Loader;

/**
 * Created by Toby on 11/02/2015.
 */
public class CommandCompactStorage implements ICommand
{
    @Override
    public String getCommandName()
    {
        return "cs";
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "/cs {version:reload}";
    }

    @Override
    public List getCommandAliases()
    {
        return Arrays.asList("compactstorage");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        if(args.length > 0)
        {
            String cmd = args[0];

            if(cmd.equalsIgnoreCase("version"))
            {
                sender.addChatMessage(new ChatComponentText("CompactStorage version " + Loader.instance().getIndexedModList().get("compactstorage").getDisplayVersion() + "."));
            }
            else if(cmd.equalsIgnoreCase("reload"))
            {
                ConfigurationHandler.init();
                sender.addChatMessage(new ChatComponentText("Reloading configuration..."));
            }
            else
            {
                sender.addChatMessage(new ChatComponentTranslation(getCommandUsage(sender)));
            }
        }
        else
        {
            sender.addChatMessage(new ChatComponentTranslation(getCommandUsage(sender)));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_)
    {
        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
        return p_71516_2_.length == 1 ? Arrays.asList("reload", "version") : null;
    }

    @Override
    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
        return false;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
