package com.lmpx.lliveshardcore;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.*;

// SQLIte SQLIte база

public class SQLite {

    public Main plugin = Main.getPlugin(Main.class);

    private String url;

    // ключики
    private final static String TBNAME = "player_data";
    public final static String KEY_NAME = "name";
    public final static String KEY_LIVES = "lives";
    public final static String KEY_POINTS = "points";
    public final static String KEY_ADVSC = "advsc";
    public final static String KEY_BL = "boughtLives";

    // Осторожно много исключений
    // тут начинается магия SQL

    public SQLite() throws Exception {
        url = "jdbc:sqlite:" + plugin.getDataFolder() + File.separator + "database.db";
        Class.forName("org.sqlite.JDBC").newInstance();

        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_data (" +
                "`name` TEXT NOT NULL," +
                "`lives` INTEGER NOT NULL," +
                "`points` INTEGER NOT NULL," +
                "`advsc` INTEGER NOT NULL," +
                "`boughtLives` INTEGER NOT NULL" +
                ");");
        statement.close();
        connection.close();

    }

    public Connection getConnection() throws Exception {
        return DriverManager.getConnection(url);
    }

    public void saveData(Player player, String key, int value) throws Exception {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        statement.executeUpdate(String.format("UPDATE %s SET `%s`= %d WHERE `name` = '%s'", TBNAME, key, value, player.getName()));

        statement.close();
        connection.close();
    }

    public void saveData(Player player, String key, String value) throws Exception {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        statement.executeUpdate(String.format("UPDATE %s SET `%s`= '%s' WHERE `name` = '%s'", TBNAME, key, value, player.getName()));

        statement.close();
        connection.close();
    }

    public void newPlayer(Player player) throws Exception {
        FileConfiguration config = plugin.getConfig();
        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        statement.executeUpdate(String.format("INSERT INTO %s (`name`, `lives`, `points`,`advsc`,`boughtLives`) VALUES ('%s', %d, %d, %d, %d)", TBNAME, player.getName(), config.getInt("startLivesCount"), config.getInt("startPoints"), 0, 0));

        statement.close();
        connection.close();
    }

    public String getDataString(Player player, String key) throws Exception {
        String result;

        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(String.format("SELECT `name`,`lives`,`points`,`advsc`,`boughtLives` FROM %s WHERE `name`='%s'", TBNAME, player.getName()));
        result = resultSet.getString(key);

        statement.close();
        connection.close();

        return result;
    }

    public int getDataInt(Player player, String key) throws Exception {
        int result;

        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(String.format("SELECT `name`,`lives`,`points`,`advsc`,`boughtLives` FROM %s WHERE `name`='%s'", TBNAME, player.getName()));
        result = resultSet.getInt(key);

        statement.close();
        connection.close();

        return result;
    }

    public boolean isNew(Player player) throws Exception {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        ResultSet result = statement.executeQuery(String.format("SELECT `name`,`lives`,`points`,`advsc`,`boughtLives` FROM %s WHERE `name`='%s'", TBNAME, player.getName()));

        statement.close();
        connection.close();

        return !result.next();
    }

    public void addPoints(Player player, int amount) {
        try {
            saveData(player, KEY_POINTS, getDataInt(player, KEY_POINTS) + amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
