package com.tattyseal.compactstorage.command;

import com.tattyseal.compactstorage.ConfigurationHandler;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args)
    {
        if(args.length > 0)
        {
            String cmd = args[0];

            if(cmd.equalsIgnoreCase("version"))
            {
                sender.addChatMessage(new TextComponentString("CompactStorage version " + Loader.instance().getIndexedModList().get("compactstorage").getDisplayVersion() + "."));
            }
            else if(cmd.equalsIgnoreCase("reload"))
            {
                ConfigurationHandler.init();
                sender.addChatMessage(new TextComponentString("Reloading configuration..."));
            }
            else
            {
                sender.addChatMessage(new TextComponentTranslation(getCommandUsage(sender)));
            }
        }
        else
        {
            sender.addChatMessage(new TextComponentTranslation(getCommandUsage(sender)));
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender p_71519_1_)
    {
        return true;
    }


    @Override
    public List getTabCompletionOptions(MinecraftServer server, ICommandSender p_71516_1_, String[] p_71516_2_, @Nullable BlockPos pos) {
        return p_71516_2_.length == 1 ? Arrays.asList("reload", "version") : null;
    }

    @Override
    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
