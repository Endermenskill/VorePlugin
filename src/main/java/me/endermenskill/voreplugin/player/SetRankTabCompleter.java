package me.endermenskill.voreplugin.player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Class handling the /setrank TabCompleter
 */
public class SetRankTabCompleter implements TabCompleter {

    /**
     * Main function handling the TabCompleter
     * @param sender Source of the command.  For players tab-completing a
     *     command inside a command block, this will be the player, not
     *     the command block.
     * @param cmd Command which was executed
     * @param s Alias of the command which was used
     * @param args The arguments passed to the command, including final
     *     partial argument to be completed
     * @return List of possible values for the current argument
     */
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
        List<String> list = new ArrayList<>();

        if (args.length <= 1) {
            PlayerRank[] ranks = PlayerRank.values();
            for (PlayerRank rank : ranks) {
                list.add(rank.toString().toLowerCase());
            }
        }

        return list;
    }
}
