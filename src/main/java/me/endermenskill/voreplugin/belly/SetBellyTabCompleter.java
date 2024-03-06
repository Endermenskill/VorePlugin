package me.endermenskill.voreplugin.belly;

import me.endermenskill.voreplugin.vore.VoreType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle the /setbelly TabCompleter
 */
public class SetBellyTabCompleter implements TabCompleter {

    /**
     * Main class to handle the TabCompleter
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

         if (args.length == 2) {
             for (VoreType type : VoreType.values()) {
                 if (type.toString().startsWith(args[1])) {
                     list.add(type.toString().toLowerCase());
                 }
             }
         }

        return list;
    }
}
