package me.endermenskill.voreplugin;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Class to contain all the configurable settings
 */
public class Settings {
    public static int maxBellies;
    public static int turduckenLimit = 0;


    public static final String msgPrefix = "§8[§b§lVorePlugin§8]§r";
    public static final String bellyModelPrefix = "675682";
    public static boolean papi = false;

    /**
     * Setup function to initialise the settings
     */
    static void setup() {
        FileConfiguration config = VorePlugin.getPlugin().getConfig();

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
