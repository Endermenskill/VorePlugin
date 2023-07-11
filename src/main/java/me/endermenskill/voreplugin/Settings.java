package me.endermenskill.voreplugin;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Class to contain all the configurable settings
 */
public class Settings {
    public static int maxBellies;
    public static int turduckenLimit = 0;

    public static String bellyModelPrefix = "675682";


    /**
     * Setup function to initialise the settings
     */
    static void setup() {
        FileConfiguration config = VorePlugin.getPlugin().getConfig();

        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            Bukkit.getLogger().severe("Missing dependency: Vault\nDisabling VorePlugin...");
            Bukkit.getPluginManager().disablePlugin(VorePlugin.getPlugin());
        }

        maxBellies = config.getInt("bellyMax");
        if (maxBellies == 0) {
            Bukkit.getLogger().warning("Invalid number for config \"bellyMax\", replacing invalid information with defaults...");
            maxBellies = 10;
        }
        if (maxBellies > 64) {
            Bukkit.getLogger().warning("Config \"bellyMax\" exceeds hard cap of 64. Replacing custom amount with maximum allowed value...");
        }

        turduckenLimit = config.getInt("turduckenLimit");
        if (turduckenLimit > 10) {
            Bukkit.getLogger().warning("Config \"turduckenLimit\" exceeds hard cap of 10. Replacing custom amount with maximum allowed value...");
        }
    }
}
