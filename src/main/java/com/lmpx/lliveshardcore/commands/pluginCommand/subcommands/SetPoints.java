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

public class SetPoints extends SubCommand implements LCommand {
    @Override
    public String getPermission() {
        return "setPoints";
    }

    public String[] subPermissions() {
        return new String[]{
                "add", // Добавить поинты [0]
                "remove" // Отобрать поинты [1]
        };
    }

    @Override
    public void onCommand(CommandSender sender, @NotNull String[] args) throws Exception {
        // сэтаем очки
        if (!(sender.hasPermission(Functions.permAll()) || sender.hasPermission(Functions.permRoot() + getPermission()))) {
            Functions.noPermission(sender);
            return;
        }

        if (args.length < 2) {
            Functions.pluginMessage(sender, ChatColor.RED + Functions.getMessage("invalidArgument"));
            Functions.pluginMessage(sender, ChatColor.GRAY+""+ChatColor.ITALIC+ "/"+name()+" <player> <amount>");
            return;
        }

        boolean isAdd = false;
        boolean isRemove = false;

        if (args[1].charAt(0) == '+') {
            isAdd = true;
            if (!(sender.hasPermission(Functions.permissionBuilder(Functions.permRoot() + getPermission() + "*")) ||
                    sender.hasPermission(Functions.permissionBuilder(Functions.permRoot() + getPermission() + subPermissions()[0])))) {
                Functions.noPermission(sender);
                return;
            }
        }
        if (args[1].charAt(0) == '-') {
            isRemove = true;
            if (!(sender.hasPermission(Functions.permissionBuilder(Functions.permRoot() + getPermission() + "*")) ||
                    sender.hasPermission(Functions.permissionBuilder(Functions.permRoot() + getPermission() + subPermissions()[1])))) {
                Functions.noPermission(sender);
                return;
            }
        }
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
            Main.sqLite.saveData(player, SQLite.KEY_POINTS, arg);
        } else {
            if (isAdd) {
                Main.sqLite.saveData(player, SQLite.KEY_POINTS, Main.sqLite.getDataInt(player, SQLite.KEY_POINTS) + arg);
            }
            if (isRemove) {
                Main.sqLite.saveData(player, SQLite.KEY_POINTS, Main.sqLite.getDataInt(player, SQLite.KEY_POINTS) - arg);
            }
        }

        Functions.pluginMessage(sender, Functions.getMessage("pointsUpdated").replaceAll("\\{PLAYER}", player.getName()));
        Functions.infoActionbar(player);


    }

    @Override
    public String name() {
        return "setPoints";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
