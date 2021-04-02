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

public class SetLives extends SubCommand implements LCommand {
    @Override
    public String getPermission() {
        return "setLives";
    }

    @Override
    public void onCommand(CommandSender sender, @NotNull String[] args) throws Exception {
        // сэтаем жызу
        if (!(sender.hasPermission(Functions.permAll()) || sender.hasPermission(Functions.permRoot() + getPermission()))) {
            Functions.noPermission(sender);
            return;
        }

        if (args.length < 2) {
            Functions.pluginMessage(sender, ChatColor.RED + Functions.getMessage("invalidArgument"));
            return;
        }

        boolean isAdd = false;
        boolean isRemove = false;

        if (args[1].charAt(0) == '+') isAdd = true;
        if (args[1].charAt(0) == '-') isRemove = true;
        args[1] = args[1].replaceAll("\\+", "").replaceAll("-", "");

        if (!Functions.isNumber(args[1]) || Integer.parseInt(args[1]) < 0) {
            Functions.pluginMessage(sender, ChatColor.RED + Functions.getMessage("invalidNumber"));
            return;
        }
        int arg = Integer.parseInt(args[1]);
        String playerarg = args[0];
        Player player = Bukkit.getPlayerExact(playerarg);
        if (player == null) {
            Functions.pluginMessage(sender, Functions.getMessage("playerNotFound").replaceAll("\\{PLAYER}", playerarg));
            return;
        }


        if (!isAdd && !isRemove) {
            Main.sqLite.saveData(player, SQLite.KEY_LIVES, arg);
        } else {
            if (isAdd) {
                Main.sqLite.saveData(player, SQLite.KEY_LIVES, Main.sqLite.getDataInt(player, SQLite.KEY_LIVES) + arg);
            }
            if (isRemove) {
                Main.sqLite.saveData(player, SQLite.KEY_LIVES, Main.sqLite.getDataInt(player, SQLite.KEY_LIVES) - arg);
            }
        }

        Functions.pluginMessage(sender, Functions.getMessage("livesUpdated").replaceAll("\\{PLAYER}", player.getName()));
        Functions.infoActionbar(player);


    }

    @Override
    public String name() {
        return "setLives";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
