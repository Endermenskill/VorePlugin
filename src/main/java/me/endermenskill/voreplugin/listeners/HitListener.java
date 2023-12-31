package me.endermenskill.voreplugin.listeners;

import me.endermenskill.voreplugin.Settings;
import me.endermenskill.voreplugin.gui.BellySelectGui;
import me.endermenskill.voreplugin.vore.VoreManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

/**
 * Class to handle damage related listeners
 */
public class HitListener implements Listener {

    public static HashMap<UUID, UUID> vorePlayers = new HashMap<>();

    /**
     * Method to listen for EntityDamageByEntityEvents
     * @param e EntityDamageByEntityEvent
     */
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {

        Inventory selectGUI;

        if (!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)){
            return;
        }

        Player predator = (Player)e.getDamager();
        Player prey = (Player)e.getEntity();

        if (!VoreManager.canVore(predator, prey)){
            return;
        }

        try {
            selectGUI = BellySelectGui.create(predator, prey);
            PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 300, 99999, false, false, false);
            prey.addPotionEffect(slow);
        } catch (NullPointerException exception) {
            predator.sendMessage(Settings.msgPrefix + " §c" + exception.getMessage());

            return;
        }

        vorePlayers.put(predator.getUniqueId(), prey.getUniqueId());

        e.setCancelled(true);
        predator.openInventory(selectGUI);
    }
}
