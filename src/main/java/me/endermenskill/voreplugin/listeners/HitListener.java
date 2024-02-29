package me.endermenskill.voreplugin.listeners;

import me.endermenskill.voreplugin.Settings;
import me.endermenskill.voreplugin.gui.BellySelectGui;
import me.endermenskill.voreplugin.vore.VoreManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

/**
 * Class to handle damage related listeners
 */
public class HitListener implements Listener {

    public static HashMap<UUID, UUID> vorePlayers = new HashMap<>();

    /**
     * Method to listen for players attempting to swallow another player
     * @param e EntityDamageByEntityEvent
     */
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {

        if (!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)){
            return;
        }

        Player predator = (Player)e.getDamager();
        Player prey = (Player)e.getEntity();

        if (!VoreManager.canVore(predator, prey)){
            return;
        }

        try {
            BellySelectGui.create(predator, "vore");
            PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 300, 99999, false, false, false);
            prey.addPotionEffect(slow);
        } catch (NullPointerException exception) {
            predator.sendMessage(Settings.msgPrefix + "§c" + exception.getMessage());
            predator.sendMessage(Settings.msgPrefix + ChatColor.RED + "An error has occurred. Please contact an admin with the above error message.");
            return;
        }

        vorePlayers.put(predator.getUniqueId(), prey.getUniqueId());

        e.setCancelled(true);
    }

    /**
     * Listener to prevent swallowed players from taking damage other than digestion damage
     * @param e EntityDamageEvent e
     */
    @EventHandler
    public static void onTakeDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player p = (Player)e.getEntity();
        if (VoreManager.isVored(p) && e.getCause() != EntityDamageEvent.DamageCause.WITHER) {
            e.setCancelled(true);
        }
    }
}
