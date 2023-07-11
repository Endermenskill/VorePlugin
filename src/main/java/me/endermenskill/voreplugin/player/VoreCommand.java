package me.endermenskill.voreplugin.player;

import me.endermenskill.voreplugin.belly.Belly;
import me.endermenskill.voreplugin.stats.StatTypes;
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
            sender.sendMessage("§8[§b§lVorePlugin§8] §cYou cannot run that command from the console.");
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
        p.sendMessage("§8[§b§lVorePlugin§8] §6----- Vore stats of §r" + p.getDisplayName() + " §6-----");
        p.sendMessage("§8[§b§lVorePlugin§8] §eTimes eaten: §2" + VoreStats.getTimesEaten(p));
        p.sendMessage("§8[§b§lVorePlugin§8] §eTimes digested: §2" + VoreStats.getTimesDigested(p));
        p.sendMessage("§8[§b§lVorePlugin§8] §ePrey eaten: §2" + VoreStats.getPreyEaten(p));
        p.sendMessage("§8[§b§lVorePlugin§8] §ePrey digested: §2" + VoreStats.getPreyDigested(p));
        p.sendMessage("§8[§b§lVorePlugin§8] ");

        if (VoreStats.getTimesDigested(p) == 0) {
            p.sendMessage("§8[§b§lVorePlugin§8] §eTimes eaten/digested ratio: §2" + VoreStats.getTimesEaten(p));
        } else p.sendMessage("§8[§b§lVorePlugin§8] §eTimes eaten/digested ratio: §2" + VoreStats.getTimesEaten(p) / VoreStats.getTimesDigested(p));

        if (VoreStats.getPreyDigested(p) == 0) {
            p.sendMessage("§8[§b§lVorePlugin§8] §ePrey eaten/digested ratio: §2" + VoreStats.getPreyEaten(p));
        } else p.sendMessage("§8[§b§lVorePlugin§8] §ePrey eaten/digested ratio: §2" + VoreStats.getPreyEaten(p) / VoreStats.getPreyDigested(p));

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

        StatTypes type = StatTypes.valueOf(args[1]);

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

        //noinspection Convert2Lambda
        statData.sort(new Comparator<>() {
            @Override
            public int compare(Map.Entry<Player, Integer> o1, Map.Entry<Player, Integer> o2) {
                if (o1.getValue().equals(o2.getValue())) {
                    return o1.getKey().getName().compareTo(o2.getKey().getName());
                }
                return Integer.compare(o1.getValue(), o2.getValue());
            }
        });

        p.sendMessage("§8[§b§lVorePlugin§8] §6----- Leaderboard for §r" + args[0] + "§6 -----");
        for (int i = -1; i >= -10; i--) {
            Player player = statData.get(i).getKey();
            Integer stat = statData.get(i).getValue();
            p.sendMessage("§8[§b§lVorePlugin§8] §e" + player.getDisplayName() + "§e: §2" + stat);
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
            p.sendMessage("§8[§b§lVorePlugin§8] §cPreys can't set bellies.");
            return true;
        }

        if (VoreManager.getPrey(p).size() > 0) {
            p.sendMessage("§8[§b§lVorePlugin§8] §cCannot set bellies while having swallowed prey.");
            return true;
        }

        if (args[1] == null) {
            return false;
        }

        Belly belly = new Belly(p);
        belly.setName(args[1]);

        Location loc = p.getLocation();
        belly.setLocation(loc);
        belly.setType(VoreType.ORAL);


        try {
            VoreType type = VoreType.valueOf(args[2]);
            belly.setType(type);
        }
        catch (IllegalArgumentException e) {
            p.sendMessage("§8[§b§lVorePlugin§8] §c\"" + args[2] + "\" is not a valid vore type. §rAvailable types are §a" + Arrays.toString(VoreType.values()));
            return true;
        }

        if (VoreManager.getBellies(p).size() >= 64) {
            p.sendMessage("§8[§b§lVorePlugin§8] §cYou can only have up to 64 bellies set at once.");
            return true;
        }

        belly.save();

        p.sendMessage("§8[§b§lVorePlugin§8] §aSuccessfully set the belly \"" + belly.name + "\" to your location. May it be the home of many snacks~");
        VoreManager.reloadPlayerBellies(p);
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
                case PREDATOR -> p.sendMessage("§8[§b§lVorePlugin§8] §aYou are a §cPREDATOR§a.");
                case PREY -> p.sendMessage("§8[§b§lVorePlugin§8] §aYou are a PREY.");
                case SWITCH -> p.sendMessage("§8[§b§lVorePlugin§8] §aYou are a §9SWITCH§a.");
            }
            return true;
        }

        try {
            if (args[1].equalsIgnoreCase("PRED")) {
                args[1] = "PREDATOR";
            }

            rank = PlayerRank.valueOf(args[1].toUpperCase());

        } catch (Exception e) {
            p.sendMessage("§8[§b§lVorePlugin§8] §c\"" + args[1] + "\" is not a rank. Please use predator, switch, or prey.");
            return true;
        }

        FileConfiguration playerFile = PlayerUtil.getPlayerFile(p);
        playerFile.set("rank", rank.name());
        PlayerUtil.savePlayerFile(p, playerFile);

        switch (rank.name()) {
            case "PREDATOR" -> p.sendMessage("§8[§b§lVorePlugin§8] §aYou are now a §cPREDATOR§a. Happy snacking!");
            case "PREY" -> p.sendMessage("§8[§b§lVorePlugin§8] §aYou are now a PREY. Beware of hungry predators!");
            case "SWITCH" -> p.sendMessage("§8[§b§lVorePlugin§8] §aYou are now a §9SWITCH§a. The best of both worlds.");
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
                        p.sendMessage("§8[§b§lVorePlugin§8] §cYou already blacklisted that vore type.");
                        break;
                    }
                    preferences.add(type);

                    ArrayList<String> finalPreferences = new ArrayList<>();
                    for (VoreType voreType : preferences) {
                        finalPreferences.add(voreType.toString());
                    }

                    file.set("preferences", finalPreferences.toString());
                    PlayerUtil.savePlayerFile(p, file);
                    p.sendMessage("§8[§b§lVorePlugin§8] §aAdded vore type \"" + args[2] + "\" to blacklist.");
                    break;
                }
                catch (IllegalArgumentException e) {
                    p.sendMessage("§8[§b§lVorePlugin§8] §c" + args[2] + " is not a valid vore type. Available types are " + Arrays.toString(VoreType.values()));
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
                    p.sendMessage("§8[§b§lVorePlugin§8] §aRemoved vore type \"" + args[2] + "\" from blacklist.");
                    break;
                }
                catch (IllegalArgumentException e) {
                    p.sendMessage("§8[§b§lVorePlugin§8] §cYou did not blacklist \"" + args[2] + "\"");
                    break;
                }
            }

            default: p.sendMessage("§8[§b§lVorePlugin§8] §aYour blacklisted vore types are " + Arrays.toString(rawPreferences));
        }

        PlayerUtil.savePlayerFile(p, file);
        return true;
    }
}
