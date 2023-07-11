package me.endermenskill.voreplugin.belly;

import me.endermenskill.voreplugin.Settings;
import me.endermenskill.voreplugin.vore.VoreManager;
import me.endermenskill.voreplugin.vore.VoreType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class handling the /belly TabCompleter
 */
public class BellyTabCompleter implements TabCompleter {

    /**
     * main function
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

        switch (args.length) {
            case 1 -> list.addAll(getBellies(p));

            case 2 -> {
                list.add("swallow_message");
                list.add("digest_start_message");
                list.add("digest_message");
                list.add("release_message");
                list.add("vore_type");
                list.add("acid_strength");
                list.add("ambient_effect");
                list.add("delete");
            }

            case 3 -> {
                switch (args[1]) {
                    case "vore_type" -> {
                        for (VoreType type : VoreType.values()) {
                            list.add(type.toString());
                        }
                    }

                    case "ambient_effect" -> {
                        for (PotionEffectType effect : PotionEffectType.values()) {
                            list.add(effect.getName().toLowerCase());
                        }
                    }

                    case "acid_strength" -> {
                        list.add("1");
                        list.add("2");
                        list.add("3");
                    }

                    case "delete" -> list.addAll(getBellies(p));
                }
            }
        }
        return list;
    }

    /**
     * Private function to get a player's belly names
     * @param p Player whose belly names to get
     * @return ArrayList of their bellies' names
     */
    private ArrayList<String> getBellies(Player p) {
        ArrayList<Belly> bellies = VoreManager.getBellies(p);

        ArrayList<String> bellyNames = new ArrayList<>();
        for (Belly belly : bellies) {
            bellyNames.add(belly.name);
        }

        return bellyNames;
    }
}
