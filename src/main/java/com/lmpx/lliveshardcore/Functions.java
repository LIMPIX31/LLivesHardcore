package com.lmpx.lliveshardcore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.*;

public abstract class Functions {

    public static double frtr(double value, double From1, double From2, double To1, double To2) {
        return (value - From1) / (From2 - From1) * (To2 - To1) + To1;
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
        pluginMessage(sender, ChatColor.RED + getMessage("invalidSubcommand"));
    }

    public static void noPermission(CommandSender sender) {
        pluginMessage(sender, ChatColor.RED + getMessage("noPermission"));
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
        pluginMessage(sender, ChatColor.RED + getMessage("invalidArgument"));
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
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            StringBuilder defaultmessages = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                defaultmessages.append(line + "\n");
                line = br.readLine();
            }
            FileWriter messagesFile = new FileWriter(messages.getPath(), false);
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
        return messages.getString(path);

    }

}
