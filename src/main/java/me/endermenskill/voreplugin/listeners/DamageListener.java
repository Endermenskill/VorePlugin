package me.endermenskill.voreplugin.listeners;

import me.endermenskill.voreplugin.vore.VoreManager;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntityType() != EntityType.PLAYER) {
            return;
        }

        Player p = (Player) e.getEntity();

        if (VoreManager.getPredator(p) != null) {
            e.setCancelled(true);
        }
    }
}
