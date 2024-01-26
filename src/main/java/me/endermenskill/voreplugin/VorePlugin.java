package me.endermenskill.voreplugin;

import me.endermenskill.voreplugin.belly.*;
import me.endermenskill.voreplugin.listeners.*;
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
     * Method to execute on activation of the plugin
     */
    @Override
    public void onEnable() {
        plugin = this;

        getLogger().info("[VorePlugin] registering listeners...");
        registerListeners();

        getLogger().info("[VorePlugin] registering commands...");
        registerCommands();

        getLogger().info("[VorePlugin] registering tab completers...");
        registerTabCompleters();

        saveDefaultConfig();
        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerUtil.getPlayerFile(p);
        }

        Settings.setup();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Bukkit.getLogger().info("[VorePlugin] Hooking into PlaceholderAPI...");
            new PlaceholderHook(this).register();
            Settings.papi = true;
        }
    }

    /**
     * Method to execute on deactivation of the plugin
     */
    @Override
    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            VoreManager.emergencyRelease(p);
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
     * Private method to register the VorePlugin's listeners
     */
    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new HitListener(), getPlugin());
        Bukkit.getPluginManager().registerEvents(new JoinLeaveListener(), getPlugin());
        Bukkit.getPluginManager().registerEvents(new DigestAndReformListener(), getPlugin());
        Bukkit.getPluginManager().registerEvents(new SpectatorTargetChangeListener(), getPlugin());
        //Bukkit.getPluginManager().registerEvents(new ChatListener(), getPlugin());
    }

    /**
     * Private method to register the VorePlugin's commands
     */
    private void registerCommands() {
        getCommand("setbelly").setExecutor(new SetBellyCommand());
        getCommand("setrank").setExecutor(new SetRankCommand());
        getCommand("digest").setExecutor(new DigestCommand());
        getCommand("release").setExecutor(new ReleaseCommand());
        getCommand("belly").setExecutor(new BellyCommand());
        getCommand("preference").setExecutor(new PreferenceCommand());
        getCommand("vorestats").setExecutor(new VoreStatsCommand());
        getCommand("voretop").setExecutor(new VoreTopCommand());
        getCommand("vore").setExecutor(new VoreCommand());
        getCommand("reform").setExecutor(new ReformCommand());
        getCommand("disposal").setExecutor(new DisposalCommand());
        getCommand("test").setExecutor(new TestCommand()); //->Super secret testing command
    }

    /**
     * Private method to register the VorePlugin's TabCompleters
     */
    private void registerTabCompleters() {
        getCommand("setbelly").setTabCompleter(new SetBellyTabCompleter());
        getCommand("setrank").setTabCompleter(new SetRankTabCompleter());
        getCommand("digest").setTabCompleter(new DigestTabCompleter());
        getCommand("release").setTabCompleter(new ReleaseTabCompleter());
        getCommand("belly").setTabCompleter(new BellyTabCompleter());
        getCommand("preference").setTabCompleter(new PreferenceTabCompleter());
        getCommand("voretop").setTabCompleter(new VoreTopTabCompleter());
        getCommand("vore").setTabCompleter(new VoreTabCompleter());
        getCommand("reform").setTabCompleter(new ReformTabCompleter());
    }
}
