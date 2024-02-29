package me.endermenskill.voreplugin;

import me.endermenskill.voreplugin.belly.Belly;
import me.endermenskill.voreplugin.gui.BellyInfoGui;
import me.endermenskill.voreplugin.vore.VoreManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Super secret testing command so shush!
 */
public class TestCommand implements CommandExecutor {

    /**
     * Definitely not the super secret testing command main function
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player p = (Player) sender;

        if (!Bukkit.getOperators().contains(p)) {
            p.sendMessage(Settings.msgPrefix + "Oops! You need to put the CD up your ass!");
            return false;
        }

        p.sendMessage(Settings.msgPrefix + "Testing: GUI stuff");

        ArrayList<Belly> bellies = VoreManager.getBellies(p);

        BellyInfoGui.create(p, bellies.get(0).getName());
        return true;
    }
}