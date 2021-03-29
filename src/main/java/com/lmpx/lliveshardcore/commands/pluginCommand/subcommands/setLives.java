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

public class setLives extends SubCommand implements LCommand {
    @Override
    public String getPermission() {
        return "setLives";
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
            Main.llhManager.setLives(player, arg);
        }else{
            if(isAdd){
                Main.llhManager.setLives(player, Main.llhManager.getLives(player) + arg);
            }
            if(isRemove){
                Main.llhManager.setLives(player, Main.llhManager.getLives(player) - arg);
            }
        }

        Functions.pluginMessage(sender, ChatColor.translateAlternateColorCodes('&', Functions.getMessage("livesUpdated").replaceAll("\\{PLAYER}", player.getName())));
        Main.llhManager.infoActionbar(player);


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
