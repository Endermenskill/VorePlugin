package me.endermenskill.voreplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class PluginInfoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(Settings.msgPrefix + "Plugin version: " + VorePlugin.getPlugin().getDescription().getVersion());
        sender.sendMessage(Settings.msgPrefix + "Papi integration active: " + Settings.papi);
        sender.sendMessage(Settings.msgPrefix + "Cpm integration active: " + Settings.cpm);
        return true;
    }
}
