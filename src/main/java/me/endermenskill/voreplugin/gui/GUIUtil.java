package me.endermenskill.voreplugin.gui;

import me.endermenskill.voreplugin.Settings;
import me.endermenskill.voreplugin.belly.Belly;
import me.endermenskill.voreplugin.vore.VoreManager;
import me.endermenskill.voreplugin.vore.VoreType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import se.eris.notnull.NotNull;

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

        meta.setDisplayName("§d§l" + belly.getName());

        List<String> lore = new ArrayList<>();

        List<String> preyNames = new ArrayList<>();
        for (Player prey : VoreManager.getPrey(belly)) {
            preyNames.add("§8" + prey.getDisplayName());
        }
        lore.add("§8Swallowed prey: " + preyNames.size());
        lore.addAll(preyNames);

        meta.setCustomModelData(getVoreTypeModelData(belly.getType()));
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

    /**
     * Returns the Item equivalent of a Belly to use in GUI applications
     * @param belly Belly to get the data of
     * @param data Data set to get the information of
     * @param material Material to use for the item
     * @return ItemStack of the belly item
     */
    public static ItemStack getBellyDataItem(@NotNull Belly belly, @NotNull String data, Material material) {
        if (material == null) {
            material = Material.PAPER;
        }

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        List<String> lore = new ArrayList<>();

        switch (data) {
            case "name" -> {
                meta.setDisplayName("§9Belly name:");
                lore.add("§d" + belly.getName());
            }
            case "location" -> {
                meta.setDisplayName("§dBelly Location:");
                Location bellyLocation = belly.getLocation();
                lore.add("§9World: §a" + bellyLocation.getWorld().getName());
                lore.add("§9X: §a" + bellyLocation.getX());
                lore.add("§9Y: §a" + bellyLocation.getY());
                lore.add("§9Z: §a" + bellyLocation.getZ());
            }
            case "type" -> {
                meta.setDisplayName("§dVore type:");
                lore.add("§9" + belly.getType().toString());
            }
            case "swallowMessage" -> {
                meta.setDisplayName("§dSwallow message:");
                lore.add("§a" + belly.getSwallowMessage());
            }
            case "digestInitMessage" -> {
                meta.setDisplayName("§dDigestion start message:");
                lore.add("§a" + belly.getDigestInitMessage());
            }
            case "digestMessage" -> {
                meta.setDisplayName("§dDigestion message");
                lore.add("§a" + belly.getDigestMessage());
            }
            case "releaseMessage" -> {
                meta.setDisplayName("§dRelease message:");
                lore.add("§a" + belly.getReleaseMessage());
            }
            case "bellyEffect" -> {
                meta.setDisplayName("§dAmbient effect:");
                lore.add("§a" + belly.getBellyEffect());
            }
            case "acidStrength" -> {
                meta.setDisplayName("§dAcid strength:");
                lore.add("§a" + belly.getAcidStrength());
            }
            case "prey" -> {
                meta.setDisplayName("§dCurrently vored prey:");
                for (Player p : VoreManager.getPrey(belly.getOwner())) {
                    lore.add("§a" + p.getDisplayName());
                }
            }
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
