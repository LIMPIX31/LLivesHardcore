package com.lmpx.lliveshardcore.handlers;

import com.lmpx.lliveshardcore.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    public Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) throws Exception {
        Player player = e.getPlayer();
        FileConfiguration config = plugin.getConfig();
        if (Main.sqLite.isNew(player)) {
            Main.sqLite.newPlayer(player);
        }
        Main.buyAccept.put(player, false);
        player.setMaxHealth(config.getInt("healthLimit"));
    }

}
