package com.lmpx.lliveshardcore;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class LLHManager {

    public Main plugin = Main.getPlugin(Main.class);

    public NamespacedKey lives = new NamespacedKey(plugin, "lives");
    public NamespacedKey points = new NamespacedKey(plugin, "points");
    public NamespacedKey advsc = new NamespacedKey(plugin, "advancementsCompleted");

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

    public void AdvscUp(Player player) {
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        if (dataContainer.has(advsc, PersistentDataType.INTEGER)) {
            dataContainer.set(advsc, PersistentDataType.INTEGER, dataContainer.get(advsc, PersistentDataType.INTEGER) + 1);
        }
    }

}
