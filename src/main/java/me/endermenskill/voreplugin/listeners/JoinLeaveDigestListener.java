package me.endermenskill.voreplugin.listeners;

import me.endermenskill.voreplugin.Settings;
import me.endermenskill.voreplugin.belly.Belly;
import me.endermenskill.voreplugin.player.PlayerRank;
import me.endermenskill.voreplugin.player.PlayerUtil;
import me.endermenskill.voreplugin.stats.VoreStats;
import me.endermenskill.voreplugin.vore.VoreManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * Class handling Join, leave and Death (by digestion) Listeners
 */
public class JoinLeaveDigestListener implements Listener {

    /**
     * Listener for PlayerJoinEvents
     * @param e PlayerJoinEvent
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        PlayerUtil.getPlayerFile(p);
        VoreManager.loadPlayerBellies(p);
        if (PlayerUtil.getPlayerRank(p) == PlayerRank.UNSET) {
            p.sendMessage(Settings.msgPrefix + " It seems like it's your first time playing. Be sure to set your vore rank with §a/setrank <rank>");
        }
    }

    /**
     * Listener for PlayerQuitEvents
     * @param e PlayerQuitEvent
     */
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        VoreManager.unloadPlayerBellies(e.getPlayer());
        VoreManager.digestedPlayers.remove(e.getPlayer().getUniqueId());
    }

    /**
     * Listener for PlayerDeathEvent - Only reacts if death was caused by digestion.
     * @param e PlayerDeathEvent
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player prey = e.getEntity();
        Player pred = VoreManager.getPredator(prey);

        if (!VoreManager.voredPlayers.containsKey(prey.getUniqueId())) {
            return;
        }

        Belly belly = VoreManager.voredPlayers.get(prey.getUniqueId());
        VoreManager.voredPlayers.remove(prey.getUniqueId());
        if (belly == null) {
            return;
        }

        e.setDeathMessage(prey.getDisplayName() + " was digested by " + belly.getOwner().getDisplayName() + ".");
        prey.sendMessage(belly.getDigestMessage(prey));

        GameMode previousGameMode = prey.getPreviousGameMode();
        prey.setGameMode((previousGameMode != null) ? previousGameMode : GameMode.SURVIVAL);
        e.getDrops().clear();
        Location respawn = (prey.getBedSpawnLocation() != null) ? prey.getBedSpawnLocation() : belly.getOwner().getLocation();
        prey.teleport(respawn);

        VoreStats.incrementPreyDigested(pred);
        VoreStats.incrementTimesDigested(prey);
        //VoreManager.digestedPlayers.put(p.getUniqueId(), belly);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();

        if (VoreManager.digestedPlayers.containsKey(p.getUniqueId())) {
            p.sendTitle(ChatColor.GREEN + "You have been reformed!", "Your items have been returned from your predator's belly.", 0, 60, 20);
            VoreManager.digestedPlayers.remove(p.getUniqueId());
        }
    }
}
