package com.lmpx.lliveshardcore.commands.getPrice;

import com.lmpx.lliveshardcore.Functions;
import com.lmpx.lliveshardcore.Main;
import com.lmpx.lliveshardcore.commands.LCommand;
import com.lmpx.lliveshardcore.commands.LTabCompleter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GetPrice implements CommandExecutor, LCommand {

    public Main plugin = Main.getPlugin(Main.class);

    public GetPrice(){

    }

    public void register() {
        plugin.getCommand(name()).setExecutor(this);
        plugin.getLogger().info(Functions.cmdregistered(name()));
    }

    @Override
    public String getPermission() {
        return "getprice";
    }

    @Override
    public String name() {
        return "getPrice";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!(sender.hasPermission(Functions.permAll()) || sender.hasPermission(Functions.permRoot() + getPermission()))) {
            Functions.noPermission(sender);
            return true;
        }
        if (!(sender instanceof Player)) {
            Functions.onlyPlayer(sender);
            return true;
        }

        sender.sendMessage(ChatColor.GRAY+ "Стоимось жизни для вас: " + ChatColor.GREEN + Functions.getLifePrice((Player)sender));

        return true;
    }
}
