package me.endermenskill.voreplugin;

import me.endermenskill.voreplugin.gui.GUIUtil;
import me.endermenskill.voreplugin.vore.VoreType;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Super secret testing command so shush!
 */
public class TestCommand implements CommandExecutor {

    /**
     * Definitely not the super secret testing command main function
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player p = (Player) sender;
        p.sendMessage("Testing: Belly items");

        List<String> lore = new ArrayList<>();
        lore.add("Mmmh, yummy prey. :3");

        for (VoreType type : VoreType.values()) {
            ItemStack item = new ItemStack(Material.PAPER);
            int model = GUIUtil.getVoreTypeModelData(type);

            ItemMeta meta = item.getItemMeta();
            meta.setCustomModelData(model);
            meta.setDisplayName("This represents a " + type.toString().toLowerCase() + " type belly.");

            meta.setLore(lore);

            item.setItemMeta(meta);
            p.getInventory().addItem(item);
        }
        return true;
    }
}
