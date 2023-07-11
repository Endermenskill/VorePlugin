package me.endermenskill.voreplugin.player;

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
            sender.sendMessage("§8[§b§lVorePlugin§8] §cYou cannot run that command from the console.");
            return true;
        }
        if (args.length != 1) {
            return false;
        }

        Player p = (Player)sender;

        try {
            if (args[0].equalsIgnoreCase("PRED")) args[0] = "PREDATOR";
            rank = PlayerRank.valueOf(args[0].toUpperCase());
        } catch (Exception e) {
            sender.sendMessage("§8[§b§lVorePlugin§8] §c\"" + args[0] + "\" is not a rank. Please use predator/pred, switch, or prey.");
            return true;
        }
        FileConfiguration playerFile = PlayerUtil.getPlayerFile(p);
        playerFile.set("rank", rank.name());
        PlayerUtil.savePlayerFile(p, playerFile);

        switch (rank.name()) {
            case "PREDATOR" -> p.sendMessage("§8[§b§lVorePlugin§8] §aYou are now a §cPREDATOR§a. Happy snacking!");
            case "PREY" -> p.sendMessage("§8[§b§lVorePlugin§8] §aYou are now a PREY. Beware of hungry predators!");
            case "SWITCH" -> p.sendMessage("§8[§b§lVorePlugin§8] §aYou are now a §9SWITCH§a. The best of both worlds.");
        }

        return true;
    }
}
