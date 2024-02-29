package me.endermenskill.voreplugin.stats;

import me.endermenskill.voreplugin.VorePlugin;
import me.endermenskill.voreplugin.player.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;

/**
 * Class to handle vore related statistics
 */
public class VoreStats {

    /**
     * Retrieve the amount of times a player has been eaten.
     * @param p Player to check the stats of
     * @return Amount of times the player has been eaten
     */
    public static int getTimesEaten(Player p) {
        FileConfiguration playerFile = PlayerUtil.getPlayerFile(p);
        ConfigurationSection playerStats = playerFile.getConfigurationSection("stats");
        assert playerStats != null;
        int stat = 0;
        try {
            stat = playerStats.getInt("timesEaten");
        } catch (Exception e){
            Bukkit.getLogger().warning("Error getting stat \"timesEaten\" for player " + p.getName() + ", UUID " + p.getUniqueId());
            setStat(p, StatType.timesEaten, 0);
            e.printStackTrace();
        }
        return stat;
    }

    /**
     * Retrieve the amount of times a player has been digested.
     * @param p Player to check the stats of
     * @return Amount of times the player has been digested
     */
    public static int getTimesDigested(Player p) {
        FileConfiguration playerFile = PlayerUtil.getPlayerFile(p);
        ConfigurationSection playerStats = playerFile.getConfigurationSection("stats");
        assert playerStats != null;
        int stat = 0;
        try {
            stat = playerStats.getInt("timesDigested");
        } catch (Exception e){
            Bukkit.getLogger().warning("Error getting stat \"timesDigested\" for player " + p.getName() + ", UUID " + p.getUniqueId());
            setStat(p, StatType.timesDigested, 0);
            e.printStackTrace();
        }
        return stat;
    }

    /**
     * Retrieve the amount of prey a player has eaten
     * @param p Player to check the stats of
     * @return Amount of prey the player has eaten
     */
    public static int getPreyEaten(Player p) {
        FileConfiguration playerFile = PlayerUtil.getPlayerFile(p);
        ConfigurationSection playerStats = playerFile.getConfigurationSection("stats");
        assert playerStats != null;
        int stat = 0;
        try {
            stat = playerStats.getInt("preyEaten");
        } catch (Exception e){
            Bukkit.getLogger().warning("Error getting stat \"preyEaten\" for player " + p.getName() + ", UUID " + p.getUniqueId());
            setStat(p, StatType.preyEaten, 0);
            e.printStackTrace();
        }
        return stat;
    }

    /**
     * Retrieve the amount of prey a player has digested
     * @param p Player to check the stats of
     * @return Amount of prey the player has digested
     */
    public static int getPreyDigested(Player p) {
        FileConfiguration playerFile = PlayerUtil.getPlayerFile(p);
        ConfigurationSection playerStats = playerFile.getConfigurationSection("stats");
        assert playerStats != null;
        int stat = 0;
        try {
            stat = playerStats.getInt("preyDigested");
        } catch (Exception e){
            Bukkit.getLogger().warning("Error getting stat \"preyDigested\" for player " + p.getName() + ", UUID " + p.getUniqueId());
            setStat(p, StatType.preyDigested, 0);
            e.printStackTrace();
        }
        return stat;
    }

    /**
     * Increment the amount of times a player has been eaten by 1
     * @param p Player to increment the stats of
     */
    public static void incrementTimesEaten(Player p) {
        FileConfiguration playerFile = PlayerUtil.getPlayerFile(p);
        ConfigurationSection playerStats = playerFile.getConfigurationSection("stats");
        assert playerStats != null;
        playerStats.set("timesEaten", playerStats.getInt("timesEaten") + 1);
        PlayerUtil.savePlayerFile(p, playerFile);
    }

    /**
     * Increment the amount of times a player has been digested by 1
     * @param p Player to increment the stats of
     */
    public static void incrementTimesDigested(Player p) {
        FileConfiguration playerFile = PlayerUtil.getPlayerFile(p);
        ConfigurationSection playerStats = playerFile.getConfigurationSection("stats");
        assert playerStats != null;
        playerStats.set("timesDigested", playerStats.getInt("timesDigested") + 1);
        PlayerUtil.savePlayerFile(p, playerFile);
    }

    /**
     * Increment the amount of prey a player has eaten by 1
     * @param p Player to increment the stats of
     */
    public static void incrementPreyEaten(Player p) {
        FileConfiguration playerFile = PlayerUtil.getPlayerFile(p);
        ConfigurationSection playerStats = playerFile.getConfigurationSection("stats");
        assert playerStats != null;
        playerStats.set("preyEaten", playerStats.getInt("preyEaten") + 1);
        PlayerUtil.savePlayerFile(p, playerFile);
    }

    /**
     * Increment the amount of prey a player has digested by 1
     * @param p Player to increment the stats of
     */
    public static void incrementPreyDigested(Player p) {
        FileConfiguration playerFile = PlayerUtil.getPlayerFile(p);
        ConfigurationSection playerStats = playerFile.getConfigurationSection("stats");
        assert playerStats != null;
        playerStats.set("preyDigested", playerStats.getInt("preyDigested") + 1);
        PlayerUtil.savePlayerFile(p, playerFile);
    }

    /**
     * Set a player's stat to a given amount
     * @param p Player to modify the stat of
     * @param stat Stat to modify. Can be "timesEaten", "timesDigested", "preyEaten", "preyDigested"
     * @param amount The amount to set the stat to
     */
    static void setStat(Player p, StatType stat, int amount) {
        FileConfiguration playerFile = PlayerUtil.getPlayerFile(p);
        ConfigurationSection playerStats = playerFile.getConfigurationSection("stats");
        if (playerStats == null) {
            Bukkit.getLogger().warning("Error getting stats section from " + p.getName() + "'s file. Creating section...");
            playerFile.createSection("stats");
            playerStats = playerFile.getConfigurationSection("stats");
        }

        assert playerStats != null;
        playerStats.set(stat.name(), amount);
        PlayerUtil.savePlayerFile(p, playerFile);
    }

    /**
     * Get every player's amount of times they have been eaten
     * @return HashMap(Player, Integer) with times eaten for every player
     */
    public static HashMap<Player, Integer> getAllTimesEaten() {
        HashMap<Player, Integer> allTimesEaten = new HashMap<>();
        File dataFolder = VorePlugin.getPlugin().getDataFolder();
        File playersFolder = new File(dataFolder, "players");

        File[] playerFiles = playersFolder.listFiles();
        if (playerFiles == null) {
            return null;
        }
        for (File playerFile : playerFiles) {
            YamlConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
            allTimesEaten.put(Bukkit.getPlayer(playerFile.getName()), playerConfig.getInt("stats.timesEaten"));
        }

        return allTimesEaten;
    }

    /**
     * Get every player#s amount of times they have been digested
     * @return HashMap(Player, Integer) with times digested for every player
     */
    public static HashMap<Player, Integer> getAllTimesDigested() {
        HashMap<Player, Integer> allTimesDigested = new HashMap<>();
        File dataFolder = VorePlugin.getPlugin().getDataFolder();
        File playersFolder = new File(dataFolder, "players");

        File[] playerFiles = playersFolder.listFiles();
        if (playerFiles == null) {
            return null;
        }
        for (File playerFile : playerFiles) {
            YamlConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
            allTimesDigested.put(Bukkit.getPlayer(playerFile.getName()), playerConfig.getInt("stats.timesDigested"));
        }

        return allTimesDigested;
    }

    /**
     * Get every player's amount of prey they have eaten
     * @return HashMap(Player, Integer) with every player's prey eaten
     */
    public static HashMap<Player, Integer> getAllPreyEaten() {
        HashMap<Player, Integer> allPreyEaten = new HashMap<>();
        File dataFolder = VorePlugin.getPlugin().getDataFolder();
        File playersFolder = new File(dataFolder, "players");

        File[] playerFiles = playersFolder.listFiles();
        if (playerFiles == null) {
            return null;
        }
        for (File playerFile : playerFiles) {
            YamlConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
            allPreyEaten.put(Bukkit.getPlayer(playerFile.getName()), playerConfig.getInt("stats.preyEaten"));
        }

        return allPreyEaten;
    }

    /**
     * Get every player's amount of prey they have digested
     * @return HashMap(Player, Integer) with every player's prey digested
     */
    public static HashMap<Player, Integer> getAllPreyDigested() {
        HashMap<Player, Integer> allPreyDigested = new HashMap<>();
        File dataFolder = VorePlugin.getPlugin().getDataFolder();
        File playersFolder = new File(dataFolder, "players");

        File[] playerFiles = playersFolder.listFiles();
        if (playerFiles == null) {
            return null;
        }
        for (File playerFile : playerFiles) {
            YamlConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
            allPreyDigested.put(Bukkit.getPlayer(playerFile.getName()), playerConfig.getInt("stats.preyDigested"));
        }

        return allPreyDigested;
    }
}
