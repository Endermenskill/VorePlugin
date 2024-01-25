package me.endermenskill.voreplugin.listeners;

import me.endermenskill.voreplugin.Settings;
import me.endermenskill.voreplugin.belly.Belly;
import me.endermenskill.voreplugin.stats.VoreStats;
import me.endermenskill.voreplugin.vore.VoreManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DigestAndReformListener implements Listener {
    /**
     * Listener for EntityDamageEvent - Only reacts if the affected entity dies to digestion.
     * @param e PlayerDeathEvent
     */
    @EventHandler
    public void onDeath(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        Player prey = (Player) e.getEntity();

        if (!VoreManager.isVored(prey)) {
            return;
        }

        if (prey.getHealth() - e.getFinalDamage() > 0) {
            return;
        }

        Player pred = VoreManager.getPredator(prey);
        assert pred != null;
        Belly belly = VoreManager.voredPlayers.get(prey.getUniqueId());
        VoreManager.voredPlayers.remove(prey.getUniqueId());
        if (belly == null) {
            return;
        }

        Bukkit.broadcastMessage(prey.getDisplayName() + " was digested by " + belly.getOwner().getDisplayName() + ".");

        if (Settings.papi) {
            //papi parse digestion message
        }
        else {
            prey.sendMessage(belly.getDigestMessage());
        }

        prey.teleport(pred.getLocation());
        prey.setGameMode(GameMode.SPECTATOR);
        prey.setSpectatorTarget(pred);

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
