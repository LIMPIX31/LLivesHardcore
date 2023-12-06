package com.lmpx.lliveshardcore;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.*;
import java.nio.charset.StandardCharsets;

// Тут ничего комментировать не буду

public abstract class Functions {

  private static boolean actionBarInfoThread;
  private static boolean pointsTimerThread;

  public static double frtr(double value, double From1, double From2, double To1, double To2) {
    return (value - From1) / (From2 - From1) * (To2 - To1) + To1;
  }

  public static int roundTo(int x, int y) {
    return (int) Math.floor(((x + y) / y)) * y;
  }


  private static String getPluginMessageFinalString(String msg) {
    return ChatColor.BLUE + "[LLH] " + ChatColor.WHITE + msg;
  }

  public static void pluginMessage(CommandSender sender, String msg) {
    sender.sendMessage(getPluginMessageFinalString(msg));
  }

  public static void pluginMessage(Player player, String msg) {
    player.sendMessage(getPluginMessageFinalString(msg));
  }

  public static void invalidSubcommand(CommandSender sender) {
    pluginMessage(sender, getMessage("invalidSubcommand"));
  }

  public static void noPermission(CommandSender sender) {
    pluginMessage(sender, getMessage("noPermission"));
  }

  public static void onlyPlayer(CommandSender sender) {
    pluginMessage(sender, Functions.getMessage("onlyPlayer"));
  }

  public static String permRoot() {
    return "llh.";
  }

  public static String permAll() {
    return "llh.*";
  }

  public static String cmdregistered(String cmd) {
    return ChatColor.LIGHT_PURPLE + "[Command] " + ChatColor.GREEN + "Registered " + cmd + " command";
  }

  public static String subcmdregistered(String cmd) {
    return ChatColor.LIGHT_PURPLE + "[SubCommand] " + ChatColor.GREEN + "Registered " + cmd + " subcommands";
  }

  public static boolean isNumber(String s) throws NumberFormatException {
    try {
      Integer.parseInt(s);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public static void invalidArgument(CommandSender sender) {
    pluginMessage(sender, getMessage("invalidArgument"));
  }

  public static void createMessagesFile(Main plugin) throws IOException {
    String lang = plugin.getConfig().getString("messagesLang");

    if (!(lang.equalsIgnoreCase("ru") ||
      lang.equalsIgnoreCase("en"))) {
      plugin.getLogger().severe(ChatColor.DARK_RED + "Incorrect or unsupported language (" + lang + "). Disabling plugin");
      Bukkit.getPluginManager().disablePlugin(plugin);
      return;
    }

    File messages = new File(plugin.getDataFolder() + File.separator + ("messages_" + lang + ".yml"));
    if (!messages.exists()) {
      try {
        messages.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
      InputStream is = plugin.getClass().getClassLoader()
        .getResourceAsStream("messages_" + lang + ".yml");
      BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

      StringBuilder defaultmessages = new StringBuilder();
      String line = br.readLine();
      while (line != null) {
        defaultmessages.append(line + "\n");
        line = br.readLine();
      }
      Writer messagesFile = new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(messages.getPath()), StandardCharsets.UTF_8));
      messagesFile.write(defaultmessages.toString());
      messagesFile.flush();

      plugin.getLogger().info(ChatColor.GREEN + "Created new \"" + lang + "\" messages file");
    }
  }

  public static String getMessage(String path) {
    Main plugin = Main.getPlugin(Main.class);
    String lang = plugin.getConfig().getString("messagesLang");

    if (!(lang.equalsIgnoreCase("ru") ||
      lang.equalsIgnoreCase("en"))) {
      plugin.getLogger().severe(ChatColor.DARK_RED + "Incorrect or unsupported language (" + lang + "). Disabling plugin");
      Bukkit.getPluginManager().disablePlugin(plugin);
      return "";
    }

    File messagesFile = new File(plugin.getDataFolder() + File.separator + ("messages_" + lang + ".yml"));
    if (!messagesFile.exists()) {
      try {
        createMessagesFile(plugin);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    YamlConfiguration messages = YamlConfiguration.loadConfiguration(messagesFile);
    return IridiumColorAPI.process(ChatColor.translateAlternateColorCodes('&', messages.getString(path)));

  }

  public static String intToHexColor(int x, int min, int max) {
    int maxColor = (int) Math.round(Functions.frtr(x, min, max, 0, 510));

    if (x < min) return "ff0000";
    if (x > max) return "00ff00";

    int red = 255;
    int green = 0;

    for (int i = 0; i < maxColor; i++) {
      if (green < 255) {
        green++;
      } else {
        red--;
      }
    }

    return StringUtils.leftPad(Integer.toHexString(red), 2, "0") + StringUtils.leftPad(Integer.toHexString(green), 2, "0") + "00";

  }

  public static String getHealthColor(Player player) {
    Main plugin = Main.getPlugin(Main.class);
    try {
      return intToHexColor(Main.sqLite.getDataInt(player, SQLite.KEY_LIVES), 1, plugin.getConfig().getInt("startLivesCount"));
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  public static void infoActionbar(Player player) {
    Main plugin = Main.getPlugin(Main.class);
    FileConfiguration config = plugin.getConfig();
    String abar = config.getString("actionBarFormat");
    try {
//            Main.nms.sendActionBar(player, "[{\"text\":\"" + Main.sqLite.getDataInt(player, SQLite.KEY_LIVES) + "\",\"color\":\"#" + Functions.getHealthColor(player) + "\"},{\"text\":\" | \",\"bold\":true,\"color\":\"dark_gray\"},{\"text\":\"" + Main.sqLite.getDataInt(player, SQLite.KEY_POINTS) + "\",\"color\":\"yellow\"},{\"text\":\" | \",\"bold\":true,\"color\":\"dark_gray\"},{\"text\":\"" + Main.sqLite.getDataInt(player, SQLite.KEY_ADVSC) + "\",\"color\":\"aqua\"}]");
      player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
          TextComponent.toLegacyText(
            ComponentSerializer.parse(
              PlaceholderAPI.setPlaceholders(player, abar
                .replaceAll("\\{LIVES}", String.valueOf(Main.sqLite.getDataInt(player, SQLite.KEY_LIVES)))
                .replaceAll("\\{LIVES_COLOR}", String.valueOf(Functions.getHealthColor(player)))
                .replaceAll("\\{POINTS}", String.valueOf(Main.sqLite.getDataInt(player, SQLite.KEY_POINTS)))
                .replaceAll("\\{ADVANCEMENTS}", String.valueOf(Main.sqLite.getDataInt(player, SQLite.KEY_ADVSC))))
            )
          )
        )
      );
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void startActionBarInfoThread() {
    Main plugin = Main.getPlugin(Main.class);
    actionBarInfoThread = true;
    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
      while (actionBarInfoThread) {
        for (Player player : Bukkit.getOnlinePlayers()) {
          infoActionbar(player);
        }
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });
  }

  public static void stopActionBarInfoThread() {
    actionBarInfoThread = false;
  }

  public static int getLifePrice(Player player) {
    Main plugin = Main.getPlugin(Main.class);
    FileConfiguration config = plugin.getConfig();
    try {
      return config.getInt("startLivePrice") + (config.getInt("nextLivePrice") * Main.sqLite.getDataInt(player, SQLite.KEY_BL));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }

  public static String combineArgs(String[] args) {
    StringBuilder result = new StringBuilder();
    for (String arg : args) {
      result.append(arg);
    }
    return result.toString();
  }

  public static String permissionBuilder(String... args) {
    StringBuilder tree = new StringBuilder();
    for (String perm : args) {
      tree.append(perm).append(".");
    }
    return tree.substring(0, tree.length() - 1);
  }

  public static int parseTime(String timeString) {
    String[] timeParts = timeString.split(" ");
    int s = 0;
    for (String timePart : timeParts) {
      if (timePart.contains("s")) {
        s = s + Integer.parseInt(timePart.replaceAll("s", ""));
      } else if (timePart.contains("m")) {
        s = s + Integer.parseInt(timePart.replaceAll("m", "")) * 60;
      } else if (timePart.contains("h")) {
        s = s + Integer.parseInt(timePart.replaceAll("h", "")) * 60 * 60;
      }
    }
    return s;
  }

  public static void startPointsTimerThread() {
    Main plugin = Main.getPlugin(Main.class);
    pointsTimerThread = true;
    FileConfiguration config = plugin.getConfig();
    boolean enabled = config.getBoolean("pointsTimer.enabled");
    int delay = Functions.parseTime(config.getString("pointsTimer.timer"));
    String delayString = config.getString("pointsTimer.timer");
    int reward = config.getInt("pointsTimer.reward");
    if (enabled) {
      Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
        while (pointsTimerThread) {
          for (Player player : Bukkit.getOnlinePlayers()) {
            if (Main.pointsTimer.containsKey(player)) {
              int ps = Main.pointsTimer.get(player);
              Main.pointsTimer.remove(player);
              Main.pointsTimer.put(player, ps + 1);
              if (Main.pointsTimer.get(player) % delay == 0) {
                Main.sqLite.addPoints(player, reward);
                player.sendMessage(
                  Functions.getMessage("pointsTimerMessage")
                    .replaceAll("\\{time}", delayString)
                    .replaceAll("\\{amount}", String.valueOf(reward))
                );
              }
            } else {
              Main.pointsTimer.put(player, 0);
            }
          }
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      });
    }
  }

  public static void stopPointsTimerThread() {
    pointsTimerThread = false;
  }

}
