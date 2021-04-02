package com.lmpx.lliveshardcore.handlers;

import com.lmpx.lliveshardcore.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    public Main plugin = Main.getPlugin(Main.class);


    // Если игрок зашёл
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) throws Exception {
        Player player = e.getPlayer();
        // ловим новичка и сажаем его в базу
        FileConfiguration config = plugin.getConfig();
        if (Main.sqLite.isNew(player)) {
            Main.sqLite.newPlayer(player);
        }
        // Игрок по умолчанию не собираеться покупать жизнь
        Main.buyAccept.put(player, false);
        // ставим хэпэшки игроку
        player.setMaxHealth(config.getInt("healthLimit"));
    }

}
