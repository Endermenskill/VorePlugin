package me.endermenskill.voreplugin.player;

import me.endermenskill.voreplugin.Settings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Class handling the /setrank command
 */
public class SetRankCommand implements CommandExecutor {

    /**
     * Main function to handle the /setrank command
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true if hte command has been executed successfully, false otherwise.
     */
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        PlayerRank rank;
        if (!(sender instanceof Player)) {
            sender.sendMessage(Settings.msgPrefix + "§cYou cannot run that command from the console.");
            return true;
        }

        Player p = (Player)sender;

        if (args.length == 0) {
            switch (PlayerUtil.getPlayerRank(p)) {
                case PREDATOR -> p.sendMessage(Settings.msgPrefix + "§aYou are a §r" + PlayerRank.PREDATOR.getSymbol() + "§a.");
                case PREY -> p.sendMessage(Settings.msgPrefix + "§aYou are a §r" + PlayerRank.PREY.getSymbol() + "§a.");
                case SWITCH -> p.sendMessage(Settings.msgPrefix + "§aYou are a §r" + PlayerRank.SWITCH.getSymbol() + "§a.");
            }
            return true;
        }

        try {
            if (args[0].equalsIgnoreCase("PRED")) args[0] = "PREDATOR";
            rank = PlayerRank.valueOf(args[0].toUpperCase());
        } catch (Exception e) {
            sender.sendMessage(Settings.msgPrefix + "§c\"" + args[0] + "\" is not a rank. Please use predator/pred, switch, or prey.");
            return true;
        }
        FileConfiguration playerFile = PlayerUtil.getPlayerFile(p);
        playerFile.set("rank", rank.name());
        PlayerUtil.savePlayerFile(p, playerFile);

        switch (rank.name()) {
            case "PREDATOR" -> p.sendMessage(Settings.msgPrefix + "§aYou are now a " + PlayerRank.PREDATOR.getSymbol() + "§a. Happy snacking!");
            case "PREY" -> p.sendMessage(Settings.msgPrefix + "§aYou are now a " + PlayerRank.PREY.getSymbol() + "§a. Beware of hungry predators!");
            case "SWITCH" -> p.sendMessage(Settings.msgPrefix + "§aYou are now a " + PlayerRank.SWITCH.getSymbol() + "§a. The best of both worlds.");
        }

        return true;
    }
}
