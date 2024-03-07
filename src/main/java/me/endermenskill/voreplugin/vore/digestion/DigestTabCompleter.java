package me.endermenskill.voreplugin.vore.digestion;

import me.endermenskill.voreplugin.vore.VoreManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Class handling the /digest TabCompleter
 */
public class DigestTabCompleter implements TabCompleter {

    /**
     * Main function to handle the TabCompleter
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
        Player p = (Player) sender;
        List<String> list = new ArrayList<>();

        if (args.length == 1) {
            list.add("all");

            ArrayList<Player> prey = VoreManager.getPrey(p);
            for (Player player : prey) {
                list.add(player.getName());
            }
        }
        return list;
    }
}
