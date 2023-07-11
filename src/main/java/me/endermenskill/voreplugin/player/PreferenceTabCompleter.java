package me.endermenskill.voreplugin.player;

import me.endermenskill.voreplugin.Settings;
import me.endermenskill.voreplugin.vore.VoreType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Class handling the /preference TabCompleter
 */
public class PreferenceTabCompleter implements TabCompleter {

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
        List<String> list = new ArrayList<>();
        if (args.length == 1){
            list.add("add");
            list.add("remove");
            list.add("list");
        }
        if (args.length == 2 && !args[0].equals("list")) {
            ArrayList<VoreType> preferences = PlayerUtil.getPreferences((Player)sender);
            if (args[1].equals("add")) {
                for (VoreType type : VoreType.values()) {
                    if (!preferences.contains(type)) {
                        list.add(type.toString());
                    }
                }
            }
            if (args[1].equals("remove")) {
                for (VoreType type : preferences) {
                    list.add(type.toString());
                }
            }
        }

        return list;
    }
}
