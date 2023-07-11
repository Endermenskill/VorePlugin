package me.endermenskill.voreplugin.gui;

import me.endermenskill.voreplugin.Settings;
import me.endermenskill.voreplugin.belly.Belly;
import me.endermenskill.voreplugin.vore.VoreManager;
import me.endermenskill.voreplugin.vore.VoreType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing utility methods for GUI
 */
public class GUIUtil {

    /**
     * Function to get an ItemStack representing a belly
     * @param belly Belly to itemise
     * @return ItemStack representing the given belly
     */
    public static ItemStack getBellyItem(Belly belly) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;

        meta.setDisplayName("§b§l" + belly.name);

        List<String> lore = new ArrayList<>();

        int swallowedPrey = 0;
        List<String> preyNames = new ArrayList<>();
        for (Player prey : VoreManager.getPrey(belly.getOwner())) {
            if (VoreManager.voredPlayers.get(prey.getUniqueId()).name.equals(belly.name)) {
                swallowedPrey++;
                preyNames.add("§8" + prey.getDisplayName());
            }
        }
        lore.add("§8Swallowed prey: " + swallowedPrey);
        lore.addAll(preyNames);

        meta.setCustomModelData(getVoreTypeModelData(belly.type));
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    /**
     * Function to get the customModelData for a specific vore type
     * @param voreType vore type to get the data of
     * @return Integer of the custom model data
     */
    public static Integer getVoreTypeModelData(VoreType voreType) {
        String model = Settings.bellyModelPrefix;
        model += voreType.getIndex();
        return Integer.parseInt(model);
    }
}
