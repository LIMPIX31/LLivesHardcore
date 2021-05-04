package com.lmpx.lliveshardcore.commands.payPoints;

import com.lmpx.lliveshardcore.Functions;
import com.lmpx.lliveshardcore.Main;
import com.lmpx.lliveshardcore.SQLite;
import com.lmpx.lliveshardcore.commands.LCommand;
import com.lmpx.lliveshardcore.commands.LTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class PayPoints implements CommandExecutor, LCommand {

    private Main plugin = Main.getPlugin(Main.class);

    @Override
    public String getPermission() {
        return "payPoints";
    }

    @Override
    public String name() {
        return "paypoints";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        Player senderPlayer = (Player) sender;
        if (!(sender.hasPermission(Functions.permAll()) || sender.hasPermission(Functions.permRoot() + getPermission()))) {
            Functions.noPermission(sender);
            return true;
        }
        if (args.length < 2) {
            Functions.pluginMessage(sender, ChatColor.RED + Functions.getMessage("invalidArgument"));
            Functions.pluginMessage(sender, ChatColor.GRAY + "" + ChatColor.ITALIC + "/" + name() + " <player> <amount>");
        }

        FileConfiguration config = plugin.getConfig();
        int minPayment = config.getInt("minPayment");
        if(!(minPayment >= 1)){
            Functions.pluginMessage(sender,Functions.getMessage("errorOccurred"));
            plugin.getLogger().severe("Invalid config value \"minPayment\"");
            return true;
        }

        if (!Functions.isNumber(args[1]) || Integer.parseInt(args[1]) < 1) {
            Functions.pluginMessage(sender, ChatColor.RED + Functions.getMessage("invalidNumber") + " (min: "+minPayment+")");
            return true;
        }

        int value = Integer.parseInt(args[1]);
        String playerarg = args[0];
        Player player = Bukkit.getPlayerExact(playerarg);
        if (player == null) {
            Functions.pluginMessage(sender, Functions.getMessage("playerNotFound").replaceAll("\\{PLAYER}", playerarg));
            return true;
        }

        int playerPoints = 0;
        try {
            playerPoints = Main.sqLite.getDataInt(senderPlayer, SQLite.KEY_POINTS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (playerPoints < value) {
            Functions.pluginMessage(sender, Functions.getMessage("notEnoughPoints"));
            return true;
        }
        if (senderPlayer.equals(player)) {
            Functions.pluginMessage(sender, Functions.getMessage("playerToPlayer"));
            return true;
        }

        int targetPlayerPoints = 0;
        try {
            targetPlayerPoints = Main.sqLite.getDataInt(player, SQLite.KEY_POINTS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        targetPlayerPoints += value;
        playerPoints -= value;

        try {
            Main.sqLite.saveData(player,SQLite.KEY_POINTS, targetPlayerPoints);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Main.sqLite.saveData(senderPlayer,SQLite.KEY_POINTS, playerPoints);
        } catch (Exception e) {
            e.printStackTrace();
        }

        player.sendMessage(Functions.getMessage("pointsSuccessfullyReceived").replaceAll("\\{AMOUNT}",String.valueOf(value)).replaceAll("\\{PLAYER}",senderPlayer.getName()));
        senderPlayer.sendMessage(Functions.getMessage("pointsSuccessfullyTransferred").replaceAll("\\{AMOUNT}",String.valueOf(value)).replaceAll("\\{PLAYER}",player.getName()));

        Functions.infoActionbar(player);
        Functions.infoActionbar(senderPlayer);
        return true;
    }

    public void register() {
        plugin.getCommand(name()).setExecutor(this);
        plugin.getLogger().info(Functions.cmdregistered(name()));
    }
}
