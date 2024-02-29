package me.endermenskill.voreplugin.player;

import me.endermenskill.voreplugin.Settings;
import me.endermenskill.voreplugin.belly.Belly;
import me.endermenskill.voreplugin.stats.StatType;
import me.endermenskill.voreplugin.stats.VoreStats;
import me.endermenskill.voreplugin.vore.VoreManager;
import me.endermenskill.voreplugin.vore.VoreType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Class to handle the /vore command
 */
public class VoreCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Settings.msgPrefix + "§cYou cannot run that command from the console.");
            return true;
        }

        Player p = (Player) sender;

        switch (args[0]) {

            case "stats" -> {
                return stats(p, args);
            }

            case "top" -> {
                return top(p, args);
            }

            case "setBelly" -> {
                return setBelly(p, args);
            }

            case "rank" -> {
                return rank(p, args);
            }

            case "preference" -> {
                return preference(p, args);
            }

            default -> {
                return false;
            }
        }
    }

    /**
     * private function to display a player's vore related stats
     * @param p Command sender
     * @param args Command arguments
     * @return success
     */
    private boolean stats(Player p, String[] args) {

        if (Bukkit.getOperators().contains(p) && args.length > 2) {
            p = Bukkit.getPlayer(args[1]);
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

    /**
     * Private function to display the vore stat leaderboard
     * @param p Command sender
     * @param args Command arguments
     * @return success
     */
    private boolean top(Player p, String[] args) {

        if (args.length < 2) {
            return false;
        }

        StatType type = StatType.valueOf(args[1]);

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

        p.sendMessage(Settings.msgPrefix + "§6----- Leaderboard for §r" + args[0] + "§6 -----");
        for (int i = -1; i >= -10; i--) {
            Player player = statData.get(i).getKey();
            Integer stat = statData.get(i).getValue();
            p.sendMessage(Settings.msgPrefix + "§e" + player.getDisplayName() + "§e: §2" + stat);
        }
        return true;
    }

    /**
     * Private function to set a belly
     * @param p Command sender
     * @param args Command arguments
     * @return success
     */
    private boolean setBelly(Player p, String[] args) {

        if (PlayerUtil.getPlayerRank(p) == PlayerRank.PREY) {
            p.sendMessage(Settings.msgPrefix + "§cPreys can't set bellies.");
            return true;
        }

        if (VoreManager.getPrey(p).size() > 0) {
            p.sendMessage(Settings.msgPrefix + "§cCannot set bellies while having swallowed prey.");
            return true;
        }

        if (args[1] == null) {
            return false;
        }

        Belly belly = new Belly(p);
        belly.setName(args[1]);

        Location loc = p.getLocation();
        belly.setLocation(loc);
        belly.setDefaults();


        try {
            VoreType type = VoreType.valueOf(args[2]);
            belly.setType(type);
        } catch (Exception e) {
            p.sendMessage(Settings.msgPrefix + "§cInvalid vore type" + args[1] + "§rReplaced missing optiona argument with default value of " + VoreType.ORAL + ".");
        }

        if (VoreManager.getBellies(p).size() >= 64) {
            p.sendMessage(Settings.msgPrefix + "§cYou can only have up to 64 bellies set at once.");
            return true;
        }

        p.sendMessage(Settings.msgPrefix + "§aSuccessfully set the belly \"" + belly.getName() + "\" to your location. May it be the home of many snacks~");
        return true;
    }

    /**
     * Private function to get/set a player's vore rank
     * @param p Command sender
     * @param args Command arguments
     * @return success
     */
    private boolean rank(Player p, String[] args) {
        PlayerRank rank;

        if (args.length == 1) {
            switch (PlayerUtil.getPlayerRank(p)) {
                case PREDATOR -> p.sendMessage(Settings.msgPrefix + "§aYou are a " + PlayerRank.PREDATOR.getSymbol() + "§a.");
                case PREY -> p.sendMessage(Settings.msgPrefix + "§aYou are a " + PlayerRank.PREY.getSymbol() + "§a.");
                case SWITCH -> p.sendMessage(Settings.msgPrefix + "§aYou are a " + PlayerRank.SWITCH.getSymbol() + "§a.");
            }
            return true;
        }

        try {
            if (args[1].equalsIgnoreCase("PRED")) {
                args[1] = "PREDATOR";
            }

            rank = PlayerRank.valueOf(args[1].toUpperCase());

        } catch (Exception e) {
            p.sendMessage(Settings.msgPrefix + "§c\"" + args[1] + "\" is not a rank. Please use predator, switch, or prey.");
            return true;
        }

        FileConfiguration playerFile = PlayerUtil.getPlayerFile(p);
        playerFile.set("rank", rank.name());
        PlayerUtil.savePlayerFile(p, playerFile);

        switch (rank.name()) {
            case "PREDATOR" -> p.sendMessage(Settings.msgPrefix + "§aYou are now a " + PlayerRank.PREDATOR.getSymbol() + "§a. Happy snacking!");
            case "PREY" -> p.sendMessage(Settings.msgPrefix + "§aYou are now a " + PlayerRank.PREY.getSymbol() + "§a. Beware of hungry predators!");
            case "SWITCH" -> p.sendMessage(Settings.msgPrefix + "§aYou are now a " + PlayerRank.SWITCH.getSymbol() + "§a. The best of both worlds.");
        }

        return true;
    }

    /**
     * Private function to get/set a player's preference settings
     * @param p Command sender
     * @param args Command arguments
     * @return success
     */
    private boolean preference(Player p, String[] args) {

        FileConfiguration file = PlayerUtil.getPlayerFile(p);
        String preferenceData = (String) file.get("preferences");

        assert preferenceData != null;
        preferenceData = preferenceData.replaceAll("\\[", "").replaceAll("]", "");
        String[] rawPreferences = preferenceData.split(",");

        ArrayList<VoreType> preferences = new ArrayList<>();
        for (String entry : rawPreferences) {
            preferences.add(VoreType.valueOf(entry.trim()));
        }

        switch (args[1]) {
            case "add": {

                try {
                    VoreType type = VoreType.valueOf(args[2]);

                    if (preferences.contains(type)) {
                        p.sendMessage(Settings.msgPrefix + "§cYou already blacklisted that vore type.");
                        break;
                    }
                    preferences.add(type);

                    ArrayList<String> finalPreferences = new ArrayList<>();
                    for (VoreType voreType : preferences) {
                        finalPreferences.add(voreType.toString());
                    }

                    file.set("preferences", finalPreferences.toString());
                    PlayerUtil.savePlayerFile(p, file);
                    p.sendMessage(Settings.msgPrefix + "§aAdded vore type \"" + args[2] + "\" to blacklist.");
                    break;
                }
                catch (IllegalArgumentException e) {
                    p.sendMessage(Settings.msgPrefix + "§c" + args[2] + " is not a valid vore type. Available types are " + Arrays.toString(VoreType.values()));
                }
            }

            case "remove": {
                try {
                    VoreType type = VoreType.valueOf(args[2]);
                    preferences.remove(type);

                    ArrayList<String> finalPreferences = new ArrayList<>();
                    for (VoreType voreType : preferences) {
                        finalPreferences.add(voreType.toString());
                    }

                    file.set("preferences", finalPreferences.toString());
                    PlayerUtil.savePlayerFile(p, file);
                    p.sendMessage(Settings.msgPrefix + "§aRemoved vore type \"" + args[2] + "\" from blacklist.");
                    break;
                }
                catch (IllegalArgumentException e) {
                    p.sendMessage(Settings.msgPrefix + "§cYou did not blacklist \"" + args[2] + "\"");
                    break;
                }
            }

            default: p.sendMessage(Settings.msgPrefix + "§aYour blacklisted vore types are " + Arrays.toString(rawPreferences));
        }

        PlayerUtil.savePlayerFile(p, file);
        return true;
    }
}
