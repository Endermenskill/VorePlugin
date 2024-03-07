package me.endermenskill.voreplugin.player;

import me.endermenskill.voreplugin.stats.StatType;
import me.endermenskill.voreplugin.vore.VoreType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle the /vore TabCompleter
 */
public class VoreTabCompleter implements TabCompleter {

    /**
     * Main function to handle the TabCompleter
     * @param sender Source of the command.  For players tab-completing a
     *     command inside a command block, this will be the player, not
     *     the command block.
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args The arguments passed to the command, including final
     *     partial argument to be completed
     * @return possible values for the current argument
     */
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        List<String> values = new ArrayList<>();

        if (args.length == 1) {
            values.add("stats");
            values.add("top");
            values.add("setbelly");
            values.add("rank");
            values.add("preference");
            return values;
        }

        switch (args[0]) {

            case "top" -> {
                return top();
            }

            case "setBelly" -> {
                return setBelly(args);
            }

            case "rank" -> {
                return rank(args);
            }

            case "preference" -> {
                return preference(args, (Player) sender);
            }

            default -> {
                return null;
            }
        }
    }

    /**
     * Private function to handle the top branch tabCompletion
     * @return List of possible values for the current argument
     */
    private List<String> top() {
        List<String> list = new ArrayList<>();

        for (StatType type : StatType.values()) {
            list.add(type.name());
        }

        return list;
    }

    /**
     * Private funciton to handle the setBelly branch tabCompletion
     * @param args Command arguments
     * @return List of possible values for the current argument
     */
    private List<String> setBelly(String[] args) {
        List<String> list = new ArrayList<>();

        if (args.length == 3) {
            for (VoreType type : VoreType.values()) {
                list.add(type.toString().toLowerCase());
            }
        }

        return list;
    }

    /**
     * Private function to handle the rank branch tabCompletion
     * @param args Command argments
     * @return List of possible values for the current argument
     */
    private List<String> rank(String[] args) {
        List<String> list = new ArrayList<>();

        if (args.length == 2) {
            PlayerRank[] ranks = PlayerRank.values();
            for (PlayerRank rank : ranks) {
                list.add(rank.toString().toLowerCase());
            }
        }

        return list;
    }

    /**
     * Private function to handle the preference branch tabCompletion
     * @param args Command arguments
     * @return List of possible values for the current argument
     */
    private List<String> preference(String[] args, Player p) {
        List<String> list = new ArrayList<>();

        if (args.length == 2){
            list.add("add");
            list.add("remove");
            list.add("list");
        }

        if (args.length == 3 && !args[2].equals("list")) {
            ArrayList<VoreType> preferences = PlayerUtil.getPreferences(p);

            for (VoreType type : VoreType.values()) {
                if (args[2].equals("add") && !preferences.contains(type)) {
                    list.add(type.toString().toLowerCase());
                }
                else if (args[2].equals("remove") && preferences.contains(type)) {
                    list.add(type.toString().toLowerCase());
                }
            }
        }

        return list;
    }
}
