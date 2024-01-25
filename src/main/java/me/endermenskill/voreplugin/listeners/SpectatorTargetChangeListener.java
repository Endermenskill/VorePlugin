package me.endermenskill.voreplugin.listeners;

import me.endermenskill.voreplugin.vore.VoreManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class SpectatorTargetChangeListener implements Listener {

    @EventHandler
    public static void onSpectatorTargetChange(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        if (!VoreManager.isDigeted(p)) {
            return;
        }

        Player pred = VoreManager.getPredator(p);

        if (p.getGameMode() != GameMode.SPECTATOR) {
            return;
        }

        if (p.getSpectatorTarget() != pred) {
            p.setSpectatorTarget(pred);
        }
    }
}
