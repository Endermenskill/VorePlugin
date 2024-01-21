package me.endermenskill.voreplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

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
        p.sendMessage(Settings.msgPrefix + " Testing: GUI stuff");

        Inventory inv = Bukkit.createInventory(p, 54, "Does this shit work?");

        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        assert fillerMeta != null;
        fillerMeta.setDisplayName(" ");
        fillerMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        for (int i = 0; i < 10; i++) {
            fillerMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(String.valueOf(i), 0, AttributeModifier.Operation.ADD_NUMBER));
            filler.setItemMeta(fillerMeta);
            inv.addItem(filler);
        }

        p.openInventory(inv);
        return true;
    }
}