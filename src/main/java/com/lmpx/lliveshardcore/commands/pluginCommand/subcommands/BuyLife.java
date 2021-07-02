package com.lmpx.lliveshardcore.commands.pluginCommand.subcommands;

import com.lmpx.lliveshardcore.Functions;
import com.lmpx.lliveshardcore.Main;
import com.lmpx.lliveshardcore.SQLite;
import com.lmpx.lliveshardcore.commands.LCommand;
import com.lmpx.lliveshardcore.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BuyLife extends SubCommand implements LCommand {
    @Override
    public String getPermission() {
        return "buylifefornpc";
    }

    @Override
    public void onCommand(CommandSender sender, @NotNull String[] args) throws Exception {
        if (!(sender.hasPermission(Functions.permAll()) || sender.hasPermission(Functions.permRoot() + getPermission()))) {
            Functions.noPermission(sender);
            return;
        }


        Player player = Bukkit.getPlayerExact(args[0]);


        int pp = 0;
        try {
            pp = Main.sqLite.getDataInt(player, SQLite.KEY_POINTS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (Functions.getLifePrice(player) <= pp) {
                Main.sqLite.saveData(player, SQLite.KEY_POINTS, pp - Functions.getLifePrice(player));
            } else {
                player.sendMessage(Functions.getMessage("notEnoughPoints").replaceAll("\\{REQ}", String.valueOf(Functions.getLifePrice(player) - pp)));
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Main.sqLite.saveData(player, SQLite.KEY_LIVES, Main.sqLite.getDataInt(player, SQLite.KEY_LIVES) + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Main.sqLite.saveData(player, SQLite.KEY_BL, Main.sqLite.getDataInt(player, SQLite.KEY_BL) + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        player.sendMessage(Functions.getMessage("buySuccessful"));




    }

    @Override
    public String name() {
        return "buylife";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
