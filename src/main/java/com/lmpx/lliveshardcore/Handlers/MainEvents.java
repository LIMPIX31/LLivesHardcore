package com.lmpx.lliveshardcore.Handlers;

import com.lmpx.lliveshardcore.Functions;
import com.lmpx.lliveshardcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
    public void onPlayerDeath(PlayerDeathEvent e) {
        FileConfiguration config = plugin.getConfig();
        Player player = e.getEntity();
        Main.llhManager.setLives(player, Main.llhManager.getLives(player) - 1);
        Main.llhManager.infoActionbar(player);
        if (!config.getBoolean("subtitleDeathMessage")) return;
        String subtitleDeathMessage = ChatColor.translateAlternateColorCodes('&', Functions.getMessage("subtitleDeathMessage").replaceAll("\\{PLAYER}", player.getName()));
        player.sendTitle(" ", subtitleDeathMessage);
        Bukkit.broadcastMessage(subtitleDeathMessage);
    }

    @EventHandler
    public void onPlayerPreDeath(EntityDamageEvent e) {
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
            int playerPoints = Main.llhManager.getPoints(player);
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
                Main.llhManager.setPoints(player, Main.llhManager.getPoints(player) - config.getInt("totemCost"));
            }
        }
        Main.llhManager.infoActionbar(player);

    }

    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent e) {
        Player player = e.getPlayer();
        if (e.getAdvancement().getKey().getKey().split("/")[0].equals("recipes")) return;
        if (e.getAdvancement().getKey().getKey().split("/")[1].equals("a_wizards_breakfast")) return;
        if (e.getAdvancement().getKey().getKey().split("/")[1].equals("a_wizards_breakfast_fail")) return;
        if (e.getAdvancement().getKey().getKey().split("/")[1].equals("just_keeps_going_fail")) return;
        if (e.getAdvancement().getKey().getKey().split("/")[1].equals("just_keeps_going_start")) return;
        if (e.getAdvancement().getKey().getKey().split("/")[1].equals("castaway_fail")) return;
        if (e.getAdvancement().getKey().getKey().split("/")[1].equals("castaway_start")) return;
        Main.llhManager.advscUp(player);

        FileConfiguration config = plugin.getConfig();
        int reward = config.getInt("reward");
        int rewardMultiplier = config.getInt("rewardMultiplier");
        int threshold = config.getInt("threshold");

        int advsc = Main.llhManager.getAdvsc(player);

        int advscRounded = Functions.roundTo(advsc, threshold);
        int finalPoints = ((advscRounded * rewardMultiplier) / threshold);

        Main.llhManager.setPoints(player, Main.llhManager.getPoints(player) + finalPoints);
        Main.llhManager.infoActionbar(player);

        if (config.getBoolean("advancementRewardInChat")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',Functions.getMessage("rewardMessageForAdvancement").replaceAll("\\{REWARD}", String.valueOf(finalPoints))));
        }
    }

}
