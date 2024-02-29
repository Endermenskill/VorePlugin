package me.endermenskill.voreplugin.listeners;

import me.endermenskill.voreplugin.vore.VoreManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;
import java.util.UUID;

/**
 * Class to handle chat related listeners. Currently unused because stupid.
 */
public class ChatListener implements Listener {

    /**
     * Function to listen for AsyncPlayerChatEvents
     * @param e AsyncPlayerChatEvent
     */
    @EventHandler
    public void onMessageSend(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        UUID pID = p.getUniqueId();

        if (VoreManager.voredPlayers.containsKey(pID)) {
            e.setCancelled(true);

            Player pred = VoreManager.voredPlayers.get(pID).getOwner();

            List<Entity> nearbyEntities = pred.getNearbyEntities(10,10,10);
            for (Entity entity : nearbyEntities) {
                Player player = Bukkit.getPlayer(entity.getUniqueId());
                if (player != null) {
                    player.sendMessage(p.getUniqueId(), e.getMessage());
                }
            }
        }
    }
}
