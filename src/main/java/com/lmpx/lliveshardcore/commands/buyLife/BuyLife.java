package com.lmpx.lliveshardcore.commands.buyLife;

import com.lmpx.lliveshardcore.Functions;
import com.lmpx.lliveshardcore.Main;
import com.lmpx.lliveshardcore.SQLite;
import com.lmpx.lliveshardcore.commands.LCommand;
import com.lmpx.lliveshardcore.commands.LTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BuyLife implements CommandExecutor, LCommand {

    public Main plugin = Main.getPlugin(Main.class);

    @Override
    public String getPermission() {
        return "buyLife";
    }

    @Override
    public String name() {
        return "buyLife";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        // Покупаем жызу
        if (!(sender.hasPermission(Functions.permAll()) || sender.hasPermission(Functions.permRoot() + getPermission()))) {
            Functions.noPermission(sender);
            return true;
        }

        if (!(sender instanceof Player)) {
            Functions.onlyPlayer(sender);
            return true;
        }

        Player player = (Player) sender;
        FileConfiguration config = plugin.getConfig();
        boolean accepted = Main.buyAccept.get(player);

        if (args.length == 0) {
            if (!accepted) {
                Main.buyAccept.remove(player);
                Main.buyAccept.put(player, true);
                player.sendMessage(Functions.getMessage("buyAccept").replaceAll("\\{PRICE}", String.valueOf(Functions.getLifePrice(player))));
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    Main.buyAccept.remove(player);
                    Main.buyAccept.put(player, false);
                }, 400);
            } else {
                Functions.pluginMessage(player, Functions.getMessage("alreadyRequestedBuy"));
            }
        } else {

            // Осторожно ад try/catch
            // Осторожно ад try/catch
            // Осторожно ад try/catch

            if ((args[0].equalsIgnoreCase("accept")) && accepted) {


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
                        return true;
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
                Main.buyAccept.remove(player);
                Main.buyAccept.put(player, false);
            } else {
                player.sendMessage(Functions.getMessage("noActivePurchaseRequest"));
            }

        }


        return true;
    }

    public void register() {
        plugin.getCommand(name()).setExecutor(this);
        plugin.getLogger().info(Functions.cmdregistered(name()));
    }

}
