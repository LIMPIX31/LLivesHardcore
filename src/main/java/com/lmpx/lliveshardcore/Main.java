package com.lmpx.lliveshardcore;
// Импорты

import com.lmpx.lliveshardcore.commands.buyLife.BuyLife;
import com.lmpx.lliveshardcore.commands.getPrice.GetPrice;
import com.lmpx.lliveshardcore.commands.payPoints.PayPoints;
import com.lmpx.lliveshardcore.commands.pluginCommand.PluginCommand;
import com.lmpx.lliveshardcore.handlers.JoinEvent;
import com.lmpx.lliveshardcore.handlers.MainEvents;
import com.lmpx.lliveshardcore.placeholders.LLHPlaceholder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Main extends JavaPlugin {

  // От NMS мне плохо
//    public static NMSUtils nms;
  // база азаза
  public static SQLite sqLite;
  // тут храним запросы на покупку
  public static HashMap<Player, Boolean> buyAccept = new HashMap<>();

  public static HashMap<Player, Integer> pointsTimer = new HashMap<>();

  @Override
  public void onEnable() {

    //херачим конфиг

    File config = new File(getDataFolder() + File.separator + "config.yml");
    if (!config.exists()) {
      getLogger().info(ChatColor.BLUE + "Creating default config file");
      getConfig().options().copyDefaults(true);
      saveDefaultConfig();
    }

    // осторожно try chatch
    try {
      // подрубаем базу
      sqLite = new SQLite();
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      // грузим язык
      Functions.createMessagesFile(this);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Чек плейсхолдер, без него жить нельзя
    if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
      // регаем плейс
      new LLHPlaceholder().register();
    } else {
      getLogger().severe(ChatColor.RED + "Required PlaceholderAPI");
      Bukkit.getPluginManager().disablePlugin(this);
    }

    //Ивентики регаем
    Bukkit.getPluginManager().registerEvents(new JoinEvent(), this);
    Bukkit.getPluginManager().registerEvents(new MainEvents(), this);

    // запускаем обновление статы в экшн баре
    if (getConfig().getBoolean("actionbarStats")) {
      Functions.startActionBarInfoThread();
    }

    Functions.startPointsTimerThread();

    // регаем команды
    PluginCommand pluginCommand = new PluginCommand();
    pluginCommand.register();

    BuyLife buyLife = new BuyLife();
    buyLife.register();

    PayPoints payPoints = new PayPoints();
    payPoints.register();

    GetPrice getPrice = new GetPrice();
    getPrice.register();


  }

  @Override
  public void onDisable() {
    //стопяем экшнбар
    Functions.stopActionBarInfoThread();

    Functions.stopPointsTimerThread();
  }

}
