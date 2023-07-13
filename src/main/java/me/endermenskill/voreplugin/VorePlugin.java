package me.endermenskill.voreplugin;

import me.endermenskill.voreplugin.belly.*;
import me.endermenskill.voreplugin.listeners.HitListener;
import me.endermenskill.voreplugin.listeners.InventoryListener;
import me.endermenskill.voreplugin.listeners.JoinLeaveDigestListener;
import me.endermenskill.voreplugin.player.*;
import me.endermenskill.voreplugin.stats.VoreStatsCommand;
import me.endermenskill.voreplugin.stats.VoreTopCommand;
import me.endermenskill.voreplugin.stats.VoreTopTabCompleter;
import me.endermenskill.voreplugin.vore.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class
 */
public class VorePlugin extends JavaPlugin {
    private static Plugin plugin;


    /**
     * Function to execute on activation of the plugin
     */
    @Override
    public void onEnable() {
        plugin = this;
        Bukkit.getLogger().info("This is a rewrite of adamholder01's voreplugin for VelocityGaming5 on fiverr.");
        registerEvents(new HitListener(), new JoinLeaveDigestListener(), new InventoryListener());

        getCommand("setbelly").setExecutor(new SetBellyCommand());
        getCommand("setrank").setExecutor(new SetRankCommand());
        getCommand("digest").setExecutor(new DigestCommand());
        getCommand("release").setExecutor(new ReleaseCommand());
        getCommand("belly").setExecutor(new BellyCommand());
        getCommand("preference").setExecutor(new PreferenceCommand());
        getCommand("vorestats").setExecutor(new VoreStatsCommand());
        getCommand("voretop").setExecutor(new VoreTopCommand());
        getCommand("vore").setExecutor(new VoreCommand());
        //getCommand("test").setExecutor(new TestCommand()); ->Super secret testing command

        getCommand("setbelly").setTabCompleter(new SetBellyTabCompleter());
        getCommand("setrank").setTabCompleter(new SetRankTabCompleter());
        getCommand("digest").setTabCompleter(new DigestTabCompleter());
        getCommand("release").setTabCompleter(new ReleaseTabCompleter());
        getCommand("belly").setTabCompleter(new BellyTabCompleter());
        getCommand("preference").setTabCompleter(new PreferenceTabCompleter());
        getCommand("voretop").setTabCompleter(new VoreTopTabCompleter());
        getCommand("vore").setTabCompleter(new VoreTabCompleter());

        saveDefaultConfig();
        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerUtil.getPlayerFile(p);
            VoreManager.loadPlayerBellies(p);
        }

        Settings.setup();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Bukkit.getLogger().info("[VorePlugin] Hooking into PlaceholderAPI...");
            new PlaceholderHook(this).register();
            Settings.papi = true;
        }
    }

    /**
     * Function to execute on deactivation of the plugin
     */
    @Override
    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            VoreManager.unloadPlayerBellies(p);
        }

        plugin = null;
    }

    /**
     * Method to get the VorePlugin's plugin instance
     * @return Plugin instance of VorePlugin
     */
    public static Plugin getPlugin() {
        return plugin;
    }

    /**
     * Private function to register listeners
     * @param listeners sequence of listeners to register
     */
    private void registerEvents(Listener... listeners) {
        for (Listener l : listeners) {
            Bukkit.getPluginManager().registerEvents(l, getPlugin());
        }
    }
}
