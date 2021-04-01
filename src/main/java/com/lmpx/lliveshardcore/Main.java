package com.lmpx.lliveshardcore;

import com.lmpx.lliveshardcore.handlers.JoinEvent;
import com.lmpx.lliveshardcore.handlers.MainEvents;
import com.lmpx.lliveshardcore.commands.buyLife.BuyLife;
import com.lmpx.lliveshardcore.commands.pluginCommand.PluginCommand;
import com.lmpx.lliveshardcore.placeholders.LLHPlaceholder;
import com.lmpx.lliveshardcore.versions.NMSUtils;
import com.lmpx.lliveshardcore.versions.NMSUtils_1_16_R1;
import com.lmpx.lliveshardcore.versions.NMSUtils_1_16_R2;
import com.lmpx.lliveshardcore.versions.NMSUtils_1_16_R3;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main extends JavaPlugin {

    public static NMSUtils nms;
    public static SQLite sqLite;

    public static Map<Player, Boolean> buyAccept = new HashMap<>();

    @Override
    public void onEnable() {
        File config = new File(getDataFolder() + File.separator + "config.yml");
        if (!config.exists()) {
            getLogger().info(ChatColor.BLUE + "Creating default config file");
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }

        try {
            sqLite = new SQLite();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Functions.createMessagesFile(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new LLHPlaceholder().register();
        } else {
            getLogger().severe(ChatColor.RED + "Required PlaceholderAPI");
            Bukkit.getPluginManager().disablePlugin(this);
        }


        //loading NMS
        String version = getNMSVersion();
        switch (version) {
            case "v1_16_R1": {
                nms = new NMSUtils_1_16_R1();
                nmsLoaded();
                break;
            }
            case "v1_16_R2": {
                nms = new NMSUtils_1_16_R2();
                nmsLoaded();
                break;
            }
            case "v1_16_R3": {
                nms = new NMSUtils_1_16_R3();
                nmsLoaded();
                break;
            }
            default: {
                getLogger().severe(ChatColor.DARK_RED + "Unsupported Minecraft Server version (" + version + ")");
                Bukkit.getPluginManager().disablePlugin(this);
                break;
            }
        }

        //registering events
        Bukkit.getPluginManager().registerEvents(new JoinEvent(), this);
        Bukkit.getPluginManager().registerEvents(new MainEvents(), this);

        if (getConfig().getBoolean("actionbarStats")) {
            Functions.startActionBarInfoThread();
        }

        PluginCommand pluginCommand = new PluginCommand();
        pluginCommand.register();

        BuyLife buyLife = new BuyLife();
        buyLife.register();


    }

    @Override
    public void onDisable() {
        Functions.stopActionBarInfoThread();
    }

    public static String getNMSVersion() {
        String v = Bukkit.getServer().getClass().getPackage().getName();
        return v.substring(v.lastIndexOf('.') + 1);
    }

    public void nmsLoaded() {
        getLogger().info(ChatColor.GREEN + "NMS (" + getNMSVersion() + ") loaded!");
    }

}
