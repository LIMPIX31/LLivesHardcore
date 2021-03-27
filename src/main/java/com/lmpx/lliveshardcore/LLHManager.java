package com.lmpx.lliveshardcore;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class LLHManager {

    public Main plugin = Main.getPlugin(Main.class);

    private NamespacedKey lives = new NamespacedKey(plugin, "lives");
    private NamespacedKey points = new NamespacedKey(plugin, "points");
    private NamespacedKey advsc = new NamespacedKey(plugin, "advancementsCompleted");

    private boolean actionBarInfoThread = true;

    public int getLives(Player player) {
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        if (dataContainer.has(lives, PersistentDataType.INTEGER)) {
            return dataContainer.get(lives, PersistentDataType.INTEGER);
        } else {
            return 0;
        }
    }

    public void setLives(Player player, int i) {
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        dataContainer.set(lives, PersistentDataType.INTEGER, i);
    }


    public int getPoints(Player player) {
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        if (dataContainer.has(points, PersistentDataType.INTEGER)) {
            return dataContainer.get(points, PersistentDataType.INTEGER);
        } else {
            return 0;
        }
    }

    public void setPoints(Player player, int i) {
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        dataContainer.set(points, PersistentDataType.INTEGER, i);
    }


    public int getAdvsc(Player player) {
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        if (dataContainer.has(advsc, PersistentDataType.INTEGER)) {
            return dataContainer.get(advsc, PersistentDataType.INTEGER);
        } else {
            return 0;
        }
    }

    public void advscUp(Player player) {
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        if (dataContainer.has(advsc, PersistentDataType.INTEGER)) {
            dataContainer.set(advsc, PersistentDataType.INTEGER, dataContainer.get(advsc, PersistentDataType.INTEGER) + 1);
        } else {
            dataContainer.set(advsc, PersistentDataType.INTEGER, 0);
        }
    }

    public boolean isNew(Player player) {
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        boolean isDataLives = dataContainer.has(lives, PersistentDataType.INTEGER);
        boolean isDataPoints = dataContainer.has(points, PersistentDataType.INTEGER);
        boolean isDataAdvcs = dataContainer.has(advsc, PersistentDataType.INTEGER);
        return !isDataLives || !isDataPoints || !isDataAdvcs;
    }

    public void startActionBarInfoThread() {
        actionBarInfoThread = true;
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            while (actionBarInfoThread) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Main.nms.sendActionBar(player, "[{\"text\":\"" + Main.llhManager.getLives(player) + "\",\"color\":\"#" + Main.llhManager.intToHexColor(Main.llhManager.getLives(player), 0, plugin.getConfig().getInt("startLivesCount")) + "\"},{\"text\":\" | \",\"bold\":true,\"color\":\"dark_gray\"},{\"text\":\"" + Main.llhManager.getPoints(player) + "\",\"color\":\"yellow\"},{\"text\":\" | \",\"bold\":true,\"color\":\"dark_gray\"},{\"text\":\"" + Main.llhManager.getAdvsc(player) + "\",\"color\":\"aqua\"}]");
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void stopActionBarInfoThread() {
        actionBarInfoThread = false;
    }


    public String intToHexColor(int x, int min, int max) {
        int maxColor = (int) Math.round(Functions.frtr(x, min, max, 0, 510));

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

}
