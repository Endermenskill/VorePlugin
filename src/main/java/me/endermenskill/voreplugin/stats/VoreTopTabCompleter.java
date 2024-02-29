package me.endermenskill.voreplugin.stats;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Class handling the /voretop TabCompleter
 */
public class VoreTopTabCompleter implements TabCompleter {

    /**
     * Main function handling the TabCompleter
     * @param commandSender Source of the command.  For players tab-completing a
     *     command inside a command block, this will be the player, not
     *     the command block.
     * @param command Command which was executed
     * @param s Alias of the command which was used
     * @param strings The arguments passed to the command, including final
     *     partial argument to be completed
     * @return List of possible values for the current argument
     */
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        List<String> list = new ArrayList<>();

        for (StatType type : StatType.values()) {
            list.add(type.name());
        }

        return list;
    }
}
