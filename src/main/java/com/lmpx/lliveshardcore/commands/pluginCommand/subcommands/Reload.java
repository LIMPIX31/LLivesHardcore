package com.lmpx.lliveshardcore.commands.pluginCommand.subcommands;

import com.lmpx.lliveshardcore.Functions;
import com.lmpx.lliveshardcore.Main;
import com.lmpx.lliveshardcore.commands.LCommand;
import com.lmpx.lliveshardcore.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Reload extends SubCommand implements LCommand {

    public Main plugin = Main.getPlugin(Main.class);

    @Override
    public String getPermission() {
        return "reloadConfig";
    }

    @Override
    public void onCommand(CommandSender sender, @NotNull String[] args) {

        if (!(sender.hasPermission(Functions.permAll()) || sender.hasPermission(Functions.permRoot() + getPermission()))) {
            Functions.noPermission(sender);
            return;
        }

        plugin.reloadConfig();
        Functions.pluginMessage(sender,Functions.getMessage("configReloaded"));
    }

    @Override
    public String name() {
        return "reload";
    }

    @Override
    public String[] aliases() {
        return new String[]{"rl"};
    }
}
