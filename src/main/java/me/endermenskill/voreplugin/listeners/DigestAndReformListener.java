package me.endermenskill.voreplugin.listeners;

import me.endermenskill.voreplugin.belly.Belly;
import me.endermenskill.voreplugin.stats.VoreStats;
import me.endermenskill.voreplugin.vore.VoreManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DigestAndReformListener implements Listener {
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
        VoreManager.digestedPlayers.put(prey.getUniqueId(), belly);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();

        if (!VoreManager.digestedPlayers.containsKey(p.getUniqueId())) {
            return;
        }

        Location respawn = (p.getBedSpawnLocation() == p.getLocation().getWorld().getSpawnLocation()) ? VoreManager.digestedPlayers.get(p.getUniqueId()).getOwner().getLocation() : p.getBedSpawnLocation();
        assert respawn != null;
        e.setRespawnLocation(respawn);
        p.sendTitle(ChatColor.GREEN + "You have been reformed!", "Your items have been returned from your predator's belly.", 0, 60, 20);
        VoreManager.digestedPlayers.remove(p.getUniqueId());

    }
}
