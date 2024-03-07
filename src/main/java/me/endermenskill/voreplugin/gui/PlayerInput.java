package me.endermenskill.voreplugin.gui;

import me.endermenskill.voreplugin.Settings;
import me.endermenskill.voreplugin.VorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * Interface designed to be implemented in GUI handling classes that require player input.
 */
public class PlayerInput implements Listener {

    static final HashMap<UUID, String> awaitingInput = new HashMap<>();

    /**
     * Method to send the message that chat input is being awaited
     * @param p Player to send the message to
     * @param context Context of the message (can be sed as an identifier)
     * @param msg Short message to display in Chat to give guidance on what to enter
     */
    public static void sendRequest(Player p, String context, String msg) {
        p.sendMessage(Settings.msgPrefix + msg);
        p.sendMessage(Settings.msgPrefix + ChatColor.GREEN + "Please enter the required input in chat.");
        awaitingInput.put(p.getUniqueId(), context);
    }

    /**
     * Method to catch the player entering a chat message in response to an input request.
     * @param e AsyncPlayerChatEvent e
     */
    @EventHandler
    private void onInputSend(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        if (!awaitingInput.containsKey(p.getUniqueId())) {
            return;
        }

        e.setCancelled(true);
        String context = awaitingInput.get(p.getUniqueId());
        awaitingInput.remove(p.getUniqueId());

        Bukkit.getScheduler().runTask(VorePlugin.getPlugin(), new Runnable() {
            @Override
            public void run() {
                Bukkit.getPluginManager().callEvent(new PlayerChatInputEvent(p, e.getMessage(), context));
            }
        });
    }

    {
        Bukkit.getPluginManager().registerEvents(this, VorePlugin.getPlugin());
    }
}
