package me.endermenskill.voreplugin.player;

import me.endermenskill.voreplugin.Settings;
import me.endermenskill.voreplugin.VorePlugin;
import me.endermenskill.voreplugin.stats.StatTypes;
import me.endermenskill.voreplugin.vore.VoreType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class containing useful methods concerning players
 */
public class PlayerUtil {

    /**
     * Get a player's config file
     * @param p Player to get the file of
     * @return FileConfiguration of the player's file
     */
    public static FileConfiguration getPlayerFile(Player p) {
        File dataFolder = new File(VorePlugin.getPlugin().getDataFolder(), "players");
        File playerFile = new File(dataFolder, p.getUniqueId() + ".yml");
        if (!playerFile.exists()) {
            createPlayerFile(p);
            playerFile = new File(dataFolder, p.getUniqueId() + ".yml");
        }
        return YamlConfiguration.loadConfiguration(playerFile);
    }

    /**
     * Create a fresh player config file
     * @param p Player for whom to create the file
     */
    private static void createPlayerFile(Player p) {
        File dataFolder = VorePlugin.getPlugin().getDataFolder();
        if (!dataFolder.exists()) {
            Bukkit.getLogger().info("Data folder not found, creating!");
            dataFolder.mkdir();
        }

        File playersFolder = new File(dataFolder, "players");
        if (!playersFolder.exists()) {
            Bukkit.getLogger().info("Players folder not found, creating!");
            playersFolder.mkdir();
        }

        File playerFile = new File(playersFolder, p.getUniqueId() + ".yml");
        if (!playerFile.exists()) {
            try {
                playerFile.createNewFile();

                YamlConfiguration ymlConfig = YamlConfiguration.loadConfiguration(playerFile);
                ymlConfig.set("rank", PlayerRank.UNSET.toString());

                ymlConfig.createSection("bellies");
                for (int i = 1; i <= 64; i++) {
                    ymlConfig.createSection("bellies.belly" + i);
                }

                ymlConfig.createSection("stats");
                for (StatTypes stat : StatTypes.values()) {
                    ymlConfig.set("stats." + stat.toString(), 0);
                }

                ymlConfig.set("preferences", "[" + VoreType.OTHER + "]");
                ymlConfig.set("autoReform", true);

                ymlConfig.save(playerFile);

                p.sendMessage(Settings.msgPrefix + " It seems like it's your first time playing. Be sure to set your vore rank with §a/setrank <rank>");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Save a player's config file
     * @param p Player whose file to save
     */
    public static void savePlayerFile(Player p, FileConfiguration playerFile) {
        File playerData = new File(VorePlugin.getPlugin().getDataFolder(), "players/" + p.getUniqueId() + ".yml");
        try {
            playerFile.save(playerData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a player's vore rank
     * @param p Player to get the rank of
     * @return PlayerRank of the player
     */
    public static PlayerRank getPlayerRank(Player p) {
        FileConfiguration playerFile = getPlayerFile(p);
        String rank = playerFile.getString("rank");
        assert rank != null;
        rank = rank.trim().toUpperCase();
        return PlayerRank.valueOf(rank);
    }

    /**
     * Get a belly's configuration section key via it's owner and name.
     * @param p Player that owns the belly
     * @param bellyName Name of the belly
     * @return Key of the belly in the player's config file. null if no belly matching the name is found.
     */
    public static String getBellyConfigurationSection(Player p, String bellyName) {
        FileConfiguration playerFile = getPlayerFile(p);
        ConfigurationSection bellies = playerFile.getConfigurationSection("bellies");

        assert bellies != null;
        for (String key : bellies.getKeys(false)) {
            String bellySection = bellies.getString(key + ".name");
            try {
                assert bellySection != null;
                if (bellySection.equals(bellyName)) {
                    return key;
                }
            }
            catch (NullPointerException ignored) {}
        }

        return null;
    }

    /**
     * Get an empty configurationSection for a belly
     * @param p Player whose file to scan
     * @return ID of the Configuration section if one was found, null otherwise.
     */
    public static String getEmptyBellySection(Player p) {
        FileConfiguration playerFile = getPlayerFile(p);
        ConfigurationSection bellies = playerFile.getConfigurationSection("bellies");
        assert bellies != null;

        for (String key : bellies.getKeys(false)) {
            ConfigurationSection section = bellies.getConfigurationSection(key);
            if (section != null) {
                if (section.getString("name") == null) {
                    return key;
                }
            }
        }

        return null;
    }

    /**
     * Get a player's preferences
     * @param p Player to get the preferences of
     * @return ArrayList containing all the player's preferences, will be empty if none are set.
     */
    public static ArrayList<VoreType> getPreferences(Player p) {
        ArrayList<VoreType> preferences = new ArrayList<>();

        FileConfiguration file = PlayerUtil.getPlayerFile(p);
        String preferenceData = (String) file.get("preferences");

        if (preferenceData == null) {
            return preferences;
        }

        preferenceData = preferenceData.replaceAll("\\[", "").replaceAll("]", "");
        String[] rawPreferences = preferenceData.split(",");

        for (String entry : rawPreferences) {
            if (!entry.isBlank()) {
                preferences.add(VoreType.valueOf(entry.trim()));
            }
        }

        return preferences;
    }
}
