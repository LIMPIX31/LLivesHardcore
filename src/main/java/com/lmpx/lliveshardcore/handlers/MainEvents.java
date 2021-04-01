package com.lmpx.lliveshardcore.handlers;

import com.lmpx.lliveshardcore.Functions;
import com.lmpx.lliveshardcore.Main;
import com.lmpx.lliveshardcore.SQLite;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.inventory.ItemStack;

public class MainEvents implements Listener {

    public Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) throws Exception {
        FileConfiguration config = plugin.getConfig();
        Player player = e.getEntity();
        int lives = Main.sqLite.getDataInt(player, SQLite.KEY_LIVES);
//        Main.llhManager.setLives(player, Main.llhManager.getLives(player) - 1);
        Main.sqLite.saveData(player, SQLite.KEY_LIVES, lives - 1);
        if(Main.sqLite.getDataInt(player, SQLite.KEY_LIVES) <= 0){
            player.setGameMode(GameMode.SPECTATOR);
            Functions.infoActionbar(player);
            if (!config.getBoolean("subtitleDeathMessage")) return;
            String subtitleDeathMessage = Functions.getMessage("subtitleLastDeathMessage").replaceAll("\\{PLAYER}", player.getName());
            player.sendTitle(" ", subtitleDeathMessage);
            Bukkit.broadcastMessage(subtitleDeathMessage);
        }else{
            Functions.infoActionbar(player);
            if (!config.getBoolean("subtitleDeathMessage")) return;
            String subtitleDeathMessage = Functions.getMessage("subtitleDeathMessage").replaceAll("\\{PLAYER}", player.getName());
            player.sendTitle(" ", subtitleDeathMessage);
            Bukkit.broadcastMessage(subtitleDeathMessage);
        }

    }

    @EventHandler
    public void onPlayerPreDeath(EntityDamageEvent e) throws Exception {
        FileConfiguration config = plugin.getConfig();
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        if (player.getHealth() > e.getFinalDamage()) return;
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();
        int totems = 0;
        if (mainHand.getType().equals(Material.TOTEM_OF_UNDYING)) totems++;
        if (offHand.getType().equals(Material.TOTEM_OF_UNDYING)) totems++;

        if (totems > 0) {
            int playerPoints = Main.sqLite.getDataInt(player, SQLite.KEY_POINTS);
            if (playerPoints < config.getInt("totemCost")) {
                if (mainHand.getType().equals(Material.TOTEM_OF_UNDYING)) {
                    player.getInventory().setItemInMainHand(null);
                    Bukkit.getWorld(player.getWorld().getName()).dropItemNaturally(player.getLocation(), mainHand);
                }
                if (offHand.getType().equals(Material.TOTEM_OF_UNDYING)) {
                    player.getInventory().setItemInOffHand(null);
                    Bukkit.getWorld(player.getWorld().getName()).dropItemNaturally(player.getLocation(), offHand);
                }
            } else {
//                Main.llhManager.setPoints(player, Main.llhManager.getPoints(player) - config.getInt("totemCost"));
                Main.sqLite.saveData(player, SQLite.KEY_POINTS, Main.sqLite.getDataInt(player, SQLite.KEY_POINTS) - config.getInt("totemCost"));
            }
        }
        Functions.infoActionbar(player);

    }

    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent e) throws Exception {
        Player player = e.getPlayer();
        if (e.getAdvancement().getKey().getKey().split("/")[0].equals("recipes")) return;
        if (e.getAdvancement().getKey().getKey().split("/")[1].equals("a_wizards_breakfast")) return;
        if (e.getAdvancement().getKey().getKey().split("/")[1].equals("a_wizards_breakfast_fail")) return;
        if (e.getAdvancement().getKey().getKey().split("/")[1].equals("just_keeps_going_fail")) return;
        if (e.getAdvancement().getKey().getKey().split("/")[1].equals("just_keeps_going_start")) return;
        if (e.getAdvancement().getKey().getKey().split("/")[1].equals("castaway_fail")) return;
        if (e.getAdvancement().getKey().getKey().split("/")[1].equals("castaway_start")) return;
        if (e.getAdvancement().getKey().getKey().split("/")[1].equals("kill_dragon")) return;
        if (e.getAdvancement().getKey().getKey().split("/")[1].equals("kill_wither")) return;
        Main.sqLite.saveData(player, SQLite.KEY_ADVSC, Main.sqLite.getDataInt(player, SQLite.KEY_ADVSC) + 1);

        FileConfiguration config = plugin.getConfig();
        int reward = config.getInt("reward");
        int rewardMultiplier = config.getInt("rewardMultiplier");
        int threshold = config.getInt("threshold");

//        int advsc = Main.llhManager.getAdvsc(player);
        int advsc = Main.sqLite.getDataInt(player, SQLite.KEY_ADVSC);

        int advscRounded = Functions.roundTo(advsc, threshold);
        int finalPoints = ((advscRounded * rewardMultiplier) / threshold);

//        Main.llhManager.setPoints(player, Main.llhManager.getPoints(player) + finalPoints);
        Main.sqLite.saveData(player, SQLite.KEY_POINTS, Main.sqLite.getDataInt(player, SQLite.KEY_POINTS) + finalPoints);
        Functions.infoActionbar(player);

        if (config.getBoolean("advancementRewardInChat")) {
            player.sendMessage(Functions.getMessage("rewardMessageForAdvancement").replaceAll("\\{REWARD}", String.valueOf(finalPoints)));
        }
    }

}
