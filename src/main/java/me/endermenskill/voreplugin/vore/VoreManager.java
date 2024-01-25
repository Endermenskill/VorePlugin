package me.endermenskill.voreplugin.vore;

import me.endermenskill.voreplugin.Settings;
import me.endermenskill.voreplugin.belly.Belly;
import me.endermenskill.voreplugin.player.PlayerRank;
import me.endermenskill.voreplugin.player.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Class managing vore-related actions
 */
public class VoreManager {
    public static HashMap<UUID, Belly> voredPlayers = new HashMap<>();
    public static HashMap<UUID, Belly> digestedPlayers = new HashMap<>();


    /**
     * Method to get a player's predator.
     * @param p Player whose predator to check
     * @return Predator player or null if the given player wasn't eaten
     */
    public static Player getPredator(Player p) {
        if (voredPlayers.containsKey(p.getUniqueId())) {
            return voredPlayers.get(p.getUniqueId()).getOwner();
        }

        else if (digestedPlayers.containsKey(p.getUniqueId())) {
            return digestedPlayers.get(p.getUniqueId()).getOwner();
        }

        return null;
    }

    /**
     * Get the belly containing the given player.
     * @param p Player to check
     * @return Bely containing the vored player, nul if the player wasn't vored
     */
    public static Belly getPredatorBelly(Player p) {
        if (voredPlayers.containsKey(p.getUniqueId())) {
            return voredPlayers.get(p.getUniqueId());
        }

        else if (digestedPlayers.containsKey(p.getUniqueId())) {
            return digestedPlayers.get(p.getUniqueId());
        }

        return null;
    }

    /**
     * Method to get a predator's prey
     * @param p Player whose prey to check
     * @return ArrayList of the player's prey, will be empty if one are found.
     */
    public static ArrayList<Player> getPrey(Player p) {
        ArrayList<Player> prey = new ArrayList<>();

        for (Map.Entry<UUID, Belly> entry : voredPlayers.entrySet()) {
            if (entry.getValue().getOwner() == p) {
                prey.add(Bukkit.getPlayer(entry.getKey()));
            }
        }

        for (Map.Entry<UUID, Belly> entry : digestedPlayers.entrySet()) {
            if (entry.getValue().getOwner() == p) {
                prey.add(Bukkit.getPlayer(entry.getKey()));
            }
        }

        return prey;
    }

    /**
     * Get all prey in a given belly
     * @param belly Belly to check
     * @return List of all prey in the given belly
     */
    public static ArrayList<Player> getPrey(Belly belly) {
        ArrayList<Player> preyInBelly = new ArrayList<>();

        ArrayList<Player> allPrey = getPrey(belly.getOwner());
        for (Player prey : allPrey) {
            if (getPredatorBelly(prey) == belly) {
                preyInBelly.add(prey);
            }
        }

        return preyInBelly;
    }

    /**
     * Check if a player can vore another player
     * @param pred Predator player to check
     * @param prey Prey player to check
     * @return true if vore is possible, false otherwise
     */
    public static boolean canVore(Player pred, Player prey) {

            boolean isNearby = pred.getNearbyEntities(5,5,5).contains(prey);
            boolean canSee = pred.hasLineOfSight(prey);

            return canBePredator(pred) && canBePrey(prey) && isNearby && canSee;
    }

    /**
     * Method used to determine if a player can be a predator
     * @param p Player to check
     * @return Boolean result
     */
    public static boolean canBePredator(Player p) {

        if (PlayerUtil.getPlayerRank(p) == PlayerRank.PREY || PlayerUtil.getPlayerRank(p) == PlayerRank.UNSET) {
            return false;
        }

        if (getPredator(p) != null) {
            return false;
        }

        return true;
    }

    /**
     * Method used to determine if a player can be a prey
     * @param p Player to check
     * @return Boolean result
     */
    public static boolean canBePrey(Player p) {

        if (PlayerUtil.getPlayerRank(p) == PlayerRank.PREDATOR || PlayerUtil.getPlayerRank(p) == PlayerRank.UNSET) {
            return false;
        }

        if (!getPrey(p).isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     * Gets a player's bellies
     * @param p Payer to get the bellies of
     * @return ArrayList containing that player's bellies
     */
    public static ArrayList<Belly> getBellies(Player p) {
        ArrayList<Belly> bellies = new ArrayList<>();

        FileConfiguration playerFile = PlayerUtil.getPlayerFile(p);
        ConfigurationSection bellySection = playerFile.getConfigurationSection("bellies");
        assert bellySection != null;

        for (String key : bellySection.getKeys(false)) {
            bellies.add(new Belly(bellySection.getConfigurationSection(key)));
        }

        return bellies;
    }

    /**
     * Get a specific belly from a player
     * @param p Player to get the belly from
     * @param name Name of the belly
     * @return Belly object of the belly, null if it can't be found.
     */
    public static Belly getBelly(Player p, String name) {
        for (Belly belly : getBellies(p)) {
            if (belly.getName().equals(name)) {
                return belly;
            }
        }
        return null;
    }

    /**
     * Delete a belly
     * @param belly Belly to delete
     */
    public static void deleteBelly(Belly belly) {
        Player p = belly.getOwner();
        FileConfiguration playerFile = PlayerUtil.getPlayerFile(p);
        ConfigurationSection bellies = playerFile.getConfigurationSection("bellies");
        assert bellies != null;

        for (String key : bellies.getKeys(false)) {
            String bellyName = bellies.getString(key + ".name");
            if (bellyName != null && bellyName.equals(belly.getName())) {
                bellies.set(key, null);
                PlayerUtil.savePlayerFile(p, playerFile);
                return;
            }
        }
    }

    /**
     * Forcefully releases a player from their predator's belly, if they are in one.
     * @param p Player to release
     */
    public static void emergencyRelease(Player p) {

        if (!voredPlayers.containsKey(p.getUniqueId())) {
            return;
        }

        Location releaseLocation = (p.getBedSpawnLocation() == null) ? p.getLocation().getWorld().getSpawnLocation() : p.getBedSpawnLocation();

        voredPlayers.remove(p.getUniqueId());
        p.teleport(releaseLocation);

        p.sendMessage(Settings.msgPrefix + "Something happened to your predator and you have been forcefully released.");
    }

    /**
     * Check if a player is digested
     * @param p Player to check
     * @return true if the Player is digested, false otherwise.
     */
    public static boolean isDigeted(Player p) {
        return digestedPlayers.containsKey(p.getUniqueId());
    }

    /**
     * Check if a player is vored
     * @param p Player to check
     * @return tre if the player is vored, false otherwise
     */
    public static boolean isVored(Player p) {
        return voredPlayers.containsKey(p.getUniqueId());
    }
}
