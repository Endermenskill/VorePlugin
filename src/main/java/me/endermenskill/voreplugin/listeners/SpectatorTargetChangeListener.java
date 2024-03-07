package me.endermenskill.voreplugin.listeners;

import me.endermenskill.voreplugin.belly.Belly;
import me.endermenskill.voreplugin.vore.VoreManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.UUID;

public class SpectatorTargetChangeListener implements Listener {

    @EventHandler
    public static void onSpectatorTargetChange(ServerTickEvent e) {
        for (Map.Entry<UUID, Belly> entry : VoreManager.digestedPlayers.entrySet()) {
            Player p = Bukkit.getPlayer(entry.getKey());
            assert p != null;
            Player pred = VoreManager.getPredator(p);

            if (p.getGameMode() != GameMode.SPECTATOR) {
                return;
            }

            if (p.getSpectatorTarget() != pred) {
                p.setSpectatorTarget(pred);
            }
        }
    }
}
