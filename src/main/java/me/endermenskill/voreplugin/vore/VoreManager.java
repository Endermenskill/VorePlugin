package me.endermenskill.voreplugin.vore;

import me.endermenskill.voreplugin.belly.Belly;
import me.endermenskill.voreplugin.player.PlayerRank;
import me.endermenskill.voreplugin.player.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Class managing vore-related actions
 */
public class VoreManager {
    public static HashMap<UUID, Belly> voredPlayers = new HashMap<>();
    public static HashMap<UUID, ArrayList<Belly>> playerBellies = new HashMap<>();
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

        return prey;
    }

    /**
     * Check if a player can vore another player
     * @param pred Predator player to check
     * @param prey Prey player to check (can be null)
     * @return true if vore is possible, false otherwise
     */
    public static boolean canVore(Player pred, @Nullable Player prey) {
        if (PlayerUtil.getPlayerRank(pred) == PlayerRank.PREY) {
            return false;
        }
        if (prey != null && PlayerUtil.getPlayerRank(prey) == PlayerRank.PREDATOR) {
            return false;
        }

        if (getPredator(pred) != null) {
            return false;
        }

        if (prey != null && !pred.getNearbyEntities(5,5,5).contains(prey)) {
            return false;
        }

        return true;
    }

    /**
     * Loads a player's bellies into playerBellies
     * @param p Player to load the bellies of
     */
    public static void loadPlayerBellies(Player p) {
        FileConfiguration playerFile = PlayerUtil.getPlayerFile(p);
        ConfigurationSection bellySection = playerFile.getConfigurationSection("bellies");

        ArrayList<Belly> bellies = new ArrayList<>();

        assert bellySection != null;
        for (String key : bellySection.getKeys(false)) {

            ConfigurationSection section = bellySection.getConfigurationSection(key);
            assert section != null;

            try {
                Belly belly = new Belly(p);
                belly.load(section);

                bellies.add(belly);
            }
            catch (Exception ignored) {}
        }

        playerBellies.put(p.getUniqueId(),bellies);
    }

    /**
     * Removes a player's bellies from playerBellies and teleports currently eaten prey to the player
     * @param p Player to unload the bellies of
     */
    public static void unloadPlayerBellies(Player p) {

        ArrayList<Player> eatenPrey = getPrey(p);
        if (eatenPrey.size() > 0) {
            for (Player prey : eatenPrey) {
                prey.sendMessage("§8[§b§lVorePlugin§8] §aSomething happened to " + p.getDisplayName() + " so you were automatically released from their belly.");
                prey.teleport(p.getLocation());
            }
        }

        playerBellies.remove(p.getUniqueId());
    }

    /**
     * Unloads then loads a player's bellies. For more info see loadPlayerBellies and unloadPlayerBellies
     * @param p Player to reload the bellies of
     */
    public static void reloadPlayerBellies(Player p) {
        unloadPlayerBellies(p);
        loadPlayerBellies(p);
    }

    /**
     * Gets a player's bellies
     * @param p Payer to get the bellies of
     * @return ArrayList containing that player's bellies
     */
    public static ArrayList<Belly> getBellies(Player p) {
        return playerBellies.get(p.getUniqueId());
    }

    /**
     * Get a specific belly from a player
     * @param p Player to get the belly from
     * @param name Name of the belly
     * @return Belly object of the belly, null if it can't be found.
     */
    public static Belly getBelly(Player p, String name) {
        for (Belly belly : playerBellies.get(p.getUniqueId())) {
            if (belly.name.equals(name)) {
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
            if (bellyName != null && bellyName.equals(belly.name)) {
                bellies.set(key, null);
                PlayerUtil.savePlayerFile(p, playerFile);
                VoreManager.reloadPlayerBellies(p);
                return;
            }
        }
    }
}
