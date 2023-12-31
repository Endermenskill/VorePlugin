package me.endermenskill.voreplugin.listeners;

import me.endermenskill.voreplugin.gui.BellySelectGui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.potion.PotionEffectType;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) {
            return;
        }

        Player p = (Player) e.getWhoClicked();

        if (BellySelectGui.players.contains(p)) {
            BellySelectGui.onClick(e);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();

        if (BellySelectGui.players.remove(p)) {
            p.removePotionEffect(PotionEffectType.SLOW);
        }
    }
}
