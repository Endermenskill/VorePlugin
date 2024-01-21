package me.endermenskill.voreplugin.listeners;

import me.endermenskill.voreplugin.Settings;
import me.endermenskill.voreplugin.gui.GUIUtil;
import me.endermenskill.voreplugin.player.PlayerRank;
import me.endermenskill.voreplugin.player.PlayerUtil;
import me.endermenskill.voreplugin.vore.VoreManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

/**
 * Class handling Join, leave and Death (by digestion) Listeners
 */
public class JoinLeaveListener implements Listener {

    /**
     * Listener for PlayerJoinEvents
     * @param e PlayerJoinEvent
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        PlayerUtil.getPlayerFile(p);
        if (PlayerUtil.getPlayerRank(p) == PlayerRank.UNSET) {
            p.sendMessage(Settings.msgPrefix + " It seems like it's your first time playing. Be sure to set your vore rank with §a/setrank <rank>");
        }
    }

    /**
     * Listener for PlayerQuitEvents
     * @param e PlayerQuitEvent
     */
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        ArrayList<Player> swallowedPrey = VoreManager.getPrey(p);
        for (Player prey : swallowedPrey) {
            VoreManager.emergencyRelease(prey);
        }
    }
}
