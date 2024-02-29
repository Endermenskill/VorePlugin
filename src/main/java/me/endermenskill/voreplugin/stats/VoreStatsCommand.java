package me.endermenskill.voreplugin.stats;

import me.endermenskill.voreplugin.Settings;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Class handling the /vorestats command
 */
public class VoreStatsCommand implements CommandExecutor {

    /**
     * Main function handling the /vorestats command
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param s Alias of the command which was used
     * @param args Passed command arguments
     * @return true if the command has been executed successfully, false otherwise.
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
        Player p = (Player) sender;
        if (Bukkit.getOperators().contains(p) && args.length > 0) {
            p = Bukkit.getPlayer(args[0]);
        }

        assert p != null;
        p.sendMessage(Settings.msgPrefix + "§6----- Vore stats of §r" + p.getDisplayName() + " §6-----");
        p.sendMessage(Settings.msgPrefix + "§eTimes eaten: §2" + VoreStats.getTimesEaten(p));
        p.sendMessage(Settings.msgPrefix + "§eTimes digested: §2" + VoreStats.getTimesDigested(p));
        p.sendMessage(Settings.msgPrefix + "§ePrey eaten: §2" + VoreStats.getPreyEaten(p));
        p.sendMessage(Settings.msgPrefix + "§ePrey digested: §2" + VoreStats.getPreyDigested(p));
        p.sendMessage(Settings.msgPrefix);

        if (VoreStats.getTimesDigested(p) == 0) {
            p.sendMessage(Settings.msgPrefix + "§eTimes eaten/digested ratio: §2" + VoreStats.getTimesEaten(p));
        } else p.sendMessage(Settings.msgPrefix + "§eTimes eaten/digested ratio: §2" + VoreStats.getTimesEaten(p) / VoreStats.getTimesDigested(p));

        if (VoreStats.getPreyDigested(p) == 0) {
            p.sendMessage(Settings.msgPrefix + "§ePrey eaten/digested ratio: §2" + VoreStats.getPreyEaten(p));
        } else p.sendMessage(Settings.msgPrefix + "§ePrey eaten/digested ratio: §2" + VoreStats.getPreyEaten(p) / VoreStats.getPreyDigested(p));

        return true;
    }
}
