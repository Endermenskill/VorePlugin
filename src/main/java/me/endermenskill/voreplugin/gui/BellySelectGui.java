package me.endermenskill.voreplugin.gui;

import me.endermenskill.voreplugin.Settings;
import me.endermenskill.voreplugin.belly.Belly;
import me.endermenskill.voreplugin.listeners.HitListener;
import me.endermenskill.voreplugin.player.PlayerUtil;
import me.endermenskill.voreplugin.stats.VoreStats;
import me.endermenskill.voreplugin.vore.VoreManager;
import me.endermenskill.voreplugin.vore.VoreType;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BellySelectGui implements Listener {

    /**
     * Method to create the belly selection GUI
     * @param p Predator Player
     * @param args Keyword argument(s) to use for creation
     * @throws NullPointerException Throws exception on error
     */
    public static void create(Player p, @Nullable String args) {
        List<Belly> bellies = VoreManager.getBellies(p);

        String title = "§bSet bellies:";
        if (args != null && args.equals("vore")) {
            title = "§bSelect a belly to use for vore";
        }

        Inventory inv = Bukkit.createInventory(p, 54, title);

        for (Belly belly : bellies) {
            if (inv.getContents().length == 54) {
                break;
            }
            inv.addItem(GUIUtil.getBellyItem(belly));
        }

        p.openInventory(inv);
    }

    /**
     * Handle logic for clicks
     * @param e InventoryClickEvent
     */
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        String title = e.getView().getTitle();
        if (!(title.equals("§bSelect a belly to use for vore") || title.equals("§bSet bellies:"))) {
            return;
        }

        e.setCancelled(true);

        ItemStack item = e.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        if (title.equals("§bSelect a belly to use for vore")) {
            onVore(e);
        }
        else {
            onInspect(e);
        }
    }

    /**
     * Logic for clicks when the GUI is used in the context of vore
     * @param e InventoryClickEvent
     */
    private void onVore(InventoryClickEvent e) {
        Player pred = (Player)e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        assert item != null;

        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        String bellyName = meta.getDisplayName().replace("§d§l", "");

        Belly belly = VoreManager.getBelly(pred, bellyName);

        Player prey = Bukkit.getPlayer(HitListener.vorePlayers.get(pred.getUniqueId()));
        HitListener.vorePlayers.remove(pred.getUniqueId());

        if (prey == null) {
            return;
        }

        if (VoreManager.voredPlayers.get(pred.getUniqueId()) != null) {
            pred.sendMessage(Settings.msgPrefix + " §cYou cannot swallow players while inside someones belly (yet).");
            pred.closeInventory();
            return;
        }

        ArrayList<VoreType> preyPreferences = PlayerUtil.getPreferences(prey);

        assert belly != null;
        if (belly.getType() == null) {
            pred.sendMessage(Settings.msgPrefix + " §cError fetching belly type");
            return;
        }

        if (preyPreferences.contains(belly.getType())) {
            pred.sendMessage(Settings.msgPrefix + " §c" + prey.getDisplayName() + " has blacklisted " + belly.getType() + " type bellies.");
            return;
        }

        VoreManager.voredPlayers.put(prey.getUniqueId(),belly);
        if (!prey.teleport(belly.getLocation())) {
            pred.sendMessage(Settings.msgPrefix + " §cError teleporting " + prey.getDisplayName() + " to belly location.");
            VoreManager.voredPlayers.remove(prey.getUniqueId());
            prey.removePotionEffect(PotionEffectType.SLOW);
            return;
        }
        prey.removePotionEffect(PotionEffectType.SLOW);

        PotionEffectType bellyEffectType = belly.getBellyEffect();
        if (bellyEffectType == null) {
            bellyEffectType = PotionEffectType.SLOW;
        }
        PotionEffect bellyEffect = new PotionEffect(bellyEffectType, 2_147_483_647, 1, false, false, false);
        prey.addPotionEffect(bellyEffect);

        prey.sendMessage(belly.getSwallowMessage());
        pred.sendMessage(belly.getSwallowMessage());
        prey.setGameMode(GameMode.ADVENTURE);

        VoreStats.incrementPreyEaten(pred);
        VoreStats.incrementTimesEaten(prey);
    }

    /**
     * Logic for clicks when the GUI is used for inspecting the bellies
     * @param e InventoryClickEvent
     */
    private void onInspect(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        assert item != null;

        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        Player p = (Player) e.getWhoClicked();
        String bellyName = meta.getDisplayName().replace("§d§l", "");

        e.getView().close();
        BellyInfoGui.create(p, bellyName);
    }
}
