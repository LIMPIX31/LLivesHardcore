package com.lmpx.lliveshardcore.Handlers;

import com.lmpx.lliveshardcore.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    public Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        FileConfiguration config = plugin.getConfig();
        if(Main.llhManager.isNew(player)){
            Main.llhManager.setLives(player, config.getInt("startLivesCount"));
            Main.llhManager.setPoints(player, config.getInt("startPoints"));
            Main.llhManager.advscUp(player);
        }
        player.setMaxHealth(config.getInt("healthLimit"));

    }

}
