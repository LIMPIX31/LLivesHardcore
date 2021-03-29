package com.lmpx.lliveshardcore.commands.pluginCommand.subcommands;

import com.lmpx.lliveshardcore.Functions;
import com.lmpx.lliveshardcore.Main;
import com.lmpx.lliveshardcore.commands.LCommand;
import com.lmpx.lliveshardcore.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class setPoints extends SubCommand implements LCommand {
    @Override
    public String getPermission() {
        return "setPoints";
    }

    @Override
    public void onCommand(CommandSender sender, @NotNull String[] args) {

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
            Functions.pluginMessage(sender, ChatColor.translateAlternateColorCodes('&', Functions.getMessage("playerNotFound")).replaceAll("\\{PLAYER}", playerarg));
            return;
        }


        if(!isAdd && !isRemove){
            Main.llhManager.setPoints(player, arg);
        }else{
            if(isAdd){
                Main.llhManager.setPoints(player, Main.llhManager.getPoints(player) + arg);
            }
            if(isRemove){
                Main.llhManager.setPoints(player, Main.llhManager.getPoints(player) - arg);
            }
        }

        Functions.pluginMessage(sender, ChatColor.translateAlternateColorCodes('&', Functions.getMessage("pointsUpdated").replaceAll("\\{PLAYER}", player.getName())));
        Main.llhManager.infoActionbar(player);


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
