package com.lmpx.lliveshardcore;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class LLHManager {

    public Main plugin = Main.getPlugin(Main.class);

    private NamespacedKey lives = new NamespacedKey(plugin, "lives");
    private NamespacedKey points = new NamespacedKey(plugin, "points");
    private NamespacedKey advsc = new NamespacedKey(plugin, "advancementsCompleted");

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

}
