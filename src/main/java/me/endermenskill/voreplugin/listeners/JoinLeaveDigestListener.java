package me.endermenskill.voreplugin.listeners;

import me.endermenskill.voreplugin.belly.Belly;
import me.endermenskill.voreplugin.player.PlayerUtil;
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
        PlayerUtil.getPlayerFile(e.getPlayer());
        VoreManager.loadPlayerBellies(e.getPlayer());
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
        Player p = e.getEntity();

        if (!VoreManager.voredPlayers.containsKey(p.getUniqueId())) {
            return;
        }

        Belly belly = VoreManager.voredPlayers.get(p.getUniqueId());
        VoreManager.voredPlayers.remove(p.getUniqueId());
        if (belly == null) {
            return;
        }

        e.setDeathMessage(p.getDisplayName() + " was digested by " + belly.getOwner().getDisplayName() + ".");
        p.sendMessage(belly.getDigestMessage(p));

        GameMode previousGameMode = p.getPreviousGameMode();
        p.setGameMode((previousGameMode != null) ? previousGameMode : GameMode.SURVIVAL);
        e.getDrops().clear();
        Location respawn = (p.getBedSpawnLocation() != null) ? p.getBedSpawnLocation() : belly.getOwner().getLocation();
        p.teleport(respawn);
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
