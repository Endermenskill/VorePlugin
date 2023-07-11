package me.endermenskill.voreplugin.gui;

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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class BellySelectGui {

    public static ArrayList<Player> players = new ArrayList<>();

    /**
     * Method to create the belly selection GUI
     * @param p Predator Player
     * @param prey Prey Player
     * @return Inventory with the player's bellies
     * @throws NullPointerException Throws exception on error
     */
    public static Inventory create(Player p, Player prey) throws NullPointerException {
        List<ItemStack> items = new ArrayList<>();
        List<Belly> bellies = VoreManager.getBellies(p);

        for (Belly belly : bellies) {
            ItemStack item = GUIUtil.getBellyItem(belly);
            items.add(item);
        }

        Inventory inv = Bukkit.createInventory(p, 54, "§bSelect a belly for " + prey.getDisplayName() + ".");

        for (ItemStack item : items) {
            inv.addItem(item);
        }

        players.add(p);

        return inv;
    }

    public static void onClick(InventoryClickEvent e) {

        e.setCancelled(true);


        Player pred = (Player)e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        if (item == null || item.getType() == Material.AIR)
            return;

        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        String bellyName = meta.getDisplayName().replace("§b§l", "");

        Belly belly = VoreManager.getBelly(pred, bellyName);

        Player prey = Bukkit.getPlayer(HitListener.vorePlayers.get(pred.getUniqueId()));
        HitListener.vorePlayers.remove(pred.getUniqueId());

        if (prey == null) {
            return;
        }

        if (VoreManager.voredPlayers.get(pred.getUniqueId()) != null) {
            pred.sendMessage("§8[§b§lVorePlugin§8] §cYou cannot swallow players while inside someones belly (yet).");
            pred.closeInventory();
            return;
        }

        ArrayList<VoreType> preyPreferences = PlayerUtil.getPreferences(prey);

        assert belly != null;
        if (belly.type == null) {
            pred.sendMessage("§8[§b§lVorePlugin§8] §cError fetching belly type");
            return;
        }

        if (preyPreferences.contains(belly.type)) {
            pred.sendMessage("§8[§b§lVorePlugin§8] §c" + prey.getDisplayName() + " has blacklisted " + belly.type + " type bellies.");
            return;
        }

        VoreManager.voredPlayers.put(prey.getUniqueId(),belly);
        if (!prey.teleport(belly.location)) {
            pred.sendMessage("§8[§b§lVorePlugin§8] §cError teleporting " + prey.getDisplayName() + " to belly location.");
            VoreManager.voredPlayers.remove(prey.getUniqueId());
            prey.removePotionEffect(PotionEffectType.SLOW);
            return;
        }
        prey.removePotionEffect(PotionEffectType.SLOW);

        PotionEffectType bellyEffectType = PotionEffectType.getByName(belly.bellyEffect);
        if (bellyEffectType == null) {
            bellyEffectType = PotionEffectType.SLOW;
        }
        PotionEffect bellyEffect = new PotionEffect(bellyEffectType, 2_147_483_647, 1, false, false, false);
        prey.addPotionEffect(bellyEffect);

        prey.sendMessage(belly.getSwallowMessage(prey));
        pred.sendMessage(belly.getSwallowMessage(prey));
        prey.setGameMode(GameMode.ADVENTURE);

        if (pred.getOpenInventory() == e.getView()) {
            pred.closeInventory();
        }

        VoreStats.incrementPreyEaten(pred);
        VoreStats.incrementTimesEaten(prey);
    }
}
