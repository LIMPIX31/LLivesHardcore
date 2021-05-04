package com.lmpx.lliveshardcore.commands.pluginCommand.subcommands;

import com.lmpx.lliveshardcore.Functions;
import com.lmpx.lliveshardcore.Main;
import com.lmpx.lliveshardcore.SQLite;
import com.lmpx.lliveshardcore.commands.LCommand;
import com.lmpx.lliveshardcore.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetAdvsc extends SubCommand implements LCommand {
    @Override
    public String getPermission() {
        return "setAdvsc";
    }

    @Override
    public void onCommand(CommandSender sender, @NotNull String[] args) {
        if (!(sender.hasPermission(Functions.permAll()) || sender.hasPermission(Functions.permRoot() + getPermission()))) {
            Functions.noPermission(sender);
            return;
        }
        if (args.length < 2) {
            Functions.pluginMessage(sender, ChatColor.RED + Functions.getMessage("invalidArgument"));
            Functions.pluginMessage(sender, ChatColor.GRAY+""+ChatColor.ITALIC+ "/"+name()+" <player> <amount>");
            return;
        }
        if (!Functions.isNumber(args[1]) || Integer.parseInt(args[1]) < 0) {
            Functions.pluginMessage(sender, ChatColor.RED + Functions.getMessage("invalidNumber"));
            return;
        }

        int value = Integer.parseInt(args[1]);
        String playerarg = args[0];
        Player player = Bukkit.getPlayerExact(playerarg);
        if (player == null) {
            Functions.pluginMessage(sender, Functions.getMessage("playerNotFound").replaceAll("\\{PLAYER}", playerarg));
            return;
        }
        try {
            Main.sqLite.saveData(player, SQLite.KEY_ADVSC, value);
        } catch (Exception e) {
            Functions.pluginMessage(sender, Functions.getMessage("errorOccurred"));
            e.printStackTrace();
        }
        Functions.pluginMessage(sender, Functions.getMessage("playerDataUpdated").replaceAll("\\{PLAYER}", player.getName()));
        Functions.infoActionbar(player);

    }

    @Override
    public String name() {
        return "setAdvsc";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
