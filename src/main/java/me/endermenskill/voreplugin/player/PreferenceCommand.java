package me.endermenskill.voreplugin.player;

import me.endermenskill.voreplugin.Settings;
import me.endermenskill.voreplugin.vore.VoreType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class handling the /preference command
 */
public class PreferenceCommand implements CommandExecutor {

    /**
     * Main function to handle the /preference command
     * @param sender Source of the command
     * @param command Command which was executed
     * @param s Alias of the command which was used
     * @param args Passed command arguments
     * @return true if the command has been executed successfuly, false otherwise
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Settings.msgPrefix + " §cYou cannot run that command from the console.");
            return true;
        }
        if (args.length > 2){
            return false;
        }

        Player p = (Player) sender;
        ArrayList<VoreType> preferences = PlayerUtil.getPreferences(p);
        FileConfiguration file = PlayerUtil.getPlayerFile(p);

        switch (args[0]) {
            case "add": {
                try {
                    VoreType type = VoreType.valueOf(args[1]);
                    if (preferences.contains(type)) {
                        p.sendMessage(Settings.msgPrefix + " §cYou already blacklisted that vore type.");
                        break;
                    }
                    preferences.add(type);
                    file.set("preferences", preferences.toString());
                    PlayerUtil.savePlayerFile(p, file);
                    p.sendMessage(Settings.msgPrefix + " §aAdded vore type \"" + args[1] + "\" to blacklist.");
                    break;
                } catch (IllegalArgumentException e) {
                    p.sendMessage(Settings.msgPrefix + " §c" + args[1] + " is not a valid vore type. Available types are " + Arrays.toString(VoreType.values()));
                }
            }

            case "remove": {
                try {
                    VoreType type = VoreType.valueOf(args[1]);
                    if (preferences.contains(type)) {
                        preferences.remove(type);
                        file.set("preferences", preferences.toString());
                        PlayerUtil.savePlayerFile(p, file);
                        p.sendMessage(Settings.msgPrefix + " §aRemoved vore type \"" + args[1] + "\" from blacklist.");
                        break;
                    }
                }
                catch (IllegalArgumentException e) {
                    p.sendMessage(Settings.msgPrefix + " §cYou did not blacklist \"" + args[1] + "\"");
                    break;
                }
            }

            default: {
                ArrayList<String> preferenceMessage= new ArrayList<>();
                for (VoreType preference : preferences) {
                    preferenceMessage.add(preference.toString());
                }
                p.sendMessage(Settings.msgPrefix + " §aYour blacklisted vore types are " + preferenceMessage);
            }
        }

        PlayerUtil.savePlayerFile(p, file);
        return true;
    }
}
