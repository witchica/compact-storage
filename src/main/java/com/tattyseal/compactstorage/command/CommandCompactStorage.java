package com.tattyseal.compactstorage.command;

import com.tattyseal.compactstorage.ConfigurationHandler;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Toby on 11/02/2015.
 */
public class CommandCompactStorage implements ICommand
{
    @Override
    @Nonnull
    public String getName()
    {
        return "cs";
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender)
    {
        return "/cs {version:reload}";
    }

    @Override
    @Nonnull
    public List<String> getAliases()
    {
        return Collections.singletonList("compactstorage");
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
    {
        if(args.length > 0)
        {
            String cmd = args[0];

            if(cmd.equalsIgnoreCase("version"))
            {
                sender.sendMessage(new TextComponentString("CompactStorage version " + Loader.instance().getIndexedModList().get("compactstorage").getDisplayVersion() + "."));
            }
            else if(cmd.equalsIgnoreCase("reload"))
            {
                ConfigurationHandler.init();
                sender.sendMessage(new TextComponentString("Reloading configuration..."));
            }
            else
            {
                sender.sendMessage(new TextComponentTranslation(getUsage(sender)));
            }
        }
        else
        {
            sender.sendMessage(new TextComponentTranslation(getUsage(sender)));
        }
    }

    @Override
    public boolean checkPermission(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender)
    {
        return true;
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, BlockPos pos) {
        return args.length == 1 ? Arrays.asList("reload", "version") : Collections.<String>emptyList();
    }

    @Override
    public boolean isUsernameIndex(@Nonnull String[] args, int n) {
        return false;
    }

    @Override
    public int compareTo(@Nonnull ICommand o) {
        return 0;
    }
}
