package me.endermenskill.voreplugin.listeners;

import me.endermenskill.voreplugin.belly.Belly;
import me.endermenskill.voreplugin.player.PlayerUtil;
import me.endermenskill.voreplugin.stats.VoreStats;
import me.endermenskill.voreplugin.vore.VoreManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class DigestListener implements Listener {

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

        prey.sendMessage(belly.getDigestMessage());

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta)skull.getItemMeta();
        assert meta != null;
        meta.setOwningPlayer(prey);
        skull.setItemMeta(meta);
        pred.getInventory().addItem(skull);

        prey.teleport(pred.getLocation());
        prey.setGameMode(GameMode.SPECTATOR);
        prey.setSpectatorTarget(pred);

        VoreStats.incrementPreyDigested(pred);
        VoreStats.incrementTimesDigested(prey);
        VoreManager.digestedPlayers.put(prey.getUniqueId(), belly);

        if (PlayerUtil.getAutoReform(prey)) {
            prey.performCommand("reform");
        }
    }
}
