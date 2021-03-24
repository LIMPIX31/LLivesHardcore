package com.lmpx.lliveshardcore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        File config = new File(getDataFolder() + File.separator + "config.yml");
        if (!config.exists()) {
            getLogger().info(ChatColor.BLUE + "Creating default config file");
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }

        try {
            Functions.createMessagesFile(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {

        } else {
            getLogger().severe(ChatColor.RED + "Required PlaceholderAPI");
            Bukkit.getPluginManager().disablePlugin(this);
        }

    }

    @Override
    public void onDisable() {

    }
}
