package com.lmpx.lliveshardcore.placeholders;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import com.lmpx.lliveshardcore.Functions;
import com.lmpx.lliveshardcore.Main;
import com.lmpx.lliveshardcore.SQLite;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

// Великие плейсхолдеры и ад из try/catch

public class LLHPlaceholder extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "llh";
    }

    @Override
    public @NotNull String getAuthor() {
        return "LIMPIX31";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.E0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String id) {
        if (id.equalsIgnoreCase("lives-color")) {
            try {
                return IridiumColorAPI.process("<SOLID:" + Functions.getHealthColor(player) + ">" + String.valueOf(Main.sqLite.getDataInt(player, SQLite.KEY_LIVES)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id.equalsIgnoreCase("lives")) {
            try {
                return String.valueOf(Main.sqLite.getDataInt(player, SQLite.KEY_LIVES));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(id.equalsIgnoreCase("points")){
            try {
                return String.valueOf(Main.sqLite.getDataInt(player, SQLite.KEY_POINTS));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(id.equalsIgnoreCase("advsc")){
            try {
                return String.valueOf(Main.sqLite.getDataInt(player, SQLite.KEY_ADVSC));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
