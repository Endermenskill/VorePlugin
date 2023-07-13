package me.endermenskill.voreplugin.stats;

import me.endermenskill.voreplugin.Settings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Class handling the /voretop command
 */
public class VoreTopCommand implements CommandExecutor {

    /**
     * Main function handling the /voretop command
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param s Alias of the command which was used
     * @param args Passed command arguments
     * @return true if the command has been executed successfully, false otherwise.
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
        Player p = (Player) sender;

        if (args.length < 1) {
            return false;
        }

        StatTypes type = StatTypes.valueOf(args[0]);

        HashMap<Player, Integer> data;
        switch (type) {
            case timesEaten -> data = VoreStats.getAllTimesEaten();
            case timesDigested -> data = VoreStats.getAllTimesDigested();
            case preyEaten -> data = VoreStats.getAllPreyEaten();
            case preyDigested -> data = VoreStats.getAllPreyDigested();
            default -> {return false;}
        }

        assert data != null;

        ArrayList<Map.Entry<Player, Integer>> statData = new ArrayList<>(data.entrySet());

        statData.sort(new Comparator<>() {
            @Override
            public int compare(Map.Entry<Player, Integer> o1, Map.Entry<Player, Integer> o2) {
                if (o1.getValue().equals(o2.getValue())) {
                    return o1.getKey().getName().compareTo(o2.getKey().getName());
                }
                return Integer.compare(o1.getValue(), o2.getValue());
            }
        });

        p.sendMessage(Settings.msgPrefix + " §6----- Leaderboard for §r" + args[0] + "§6 -----");
        for (int i = -1; i >= -10; i--) {
            Player player = statData.get(i).getKey();
            Integer stat = statData.get(i).getValue();
            p.sendMessage(Settings.msgPrefix + " §e" + player.getDisplayName() + "§e: §2" + stat);
        }
        return true;
    }
}
