package me.endermenskill.voreplugin.gui;

import me.endermenskill.voreplugin.belly.Belly;
import me.endermenskill.voreplugin.vore.VoreManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BellyInfoGui implements Listener {

    /**
     * Method to create the GUI
     * @param p Affected Player
     * @param args String argument(s)
     */
    public void create(Player p, String args) {
        if (args == null || args.isBlank()) {
            return;
        }

        Belly belly = VoreManager.getBelly(p, args);
        assert belly != null;

        Inventory inv = Bukkit.createInventory(p, 54, "Information for belly \"" + belly.name + "\"");

        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        assert fillerMeta != null;
        fillerMeta.setDisplayName(" ");
        fillerMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        int fillerAttribute = 0;
        for (int loop = 0; loop < 10; loop++) {
            fillerMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(String.valueOf(fillerAttribute), 0, AttributeModifier.Operation.ADD_NUMBER));
            filler.setItemMeta(fillerMeta);
            inv.addItem(filler);
            fillerAttribute++;
        }

        inv.addItem(GUIUtil.getBellyDataItem(belly, "name", Material.PAPER));

        for (int loop = 0; loop < 2; loop++) {
            fillerMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(String.valueOf(fillerAttribute), 0, AttributeModifier.Operation.ADD_NUMBER));
            filler.setItemMeta(fillerMeta);
            inv.addItem(filler);
            fillerAttribute++;
        }

        inv.addItem(GUIUtil.getBellyDataItem(belly, "swallowMessage", Material.OAK_SIGN));

        for (int loop = 0; loop < 2; loop++) {
            fillerMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(String.valueOf(fillerAttribute), 0, AttributeModifier.Operation.ADD_NUMBER));
            filler.setItemMeta(fillerMeta);
            inv.addItem(filler);
            fillerAttribute++;
        }

        inv.addItem(GUIUtil.getBellyDataItem(belly, "bellyEffect", Material.CAULDRON));

        for (int loop = 0; loop < 2; loop++) {
            fillerMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(String.valueOf(fillerAttribute), 0, AttributeModifier.Operation.ADD_NUMBER));
            filler.setItemMeta(fillerMeta);
            inv.addItem(filler);
            fillerAttribute++;
        }

        inv.addItem(GUIUtil.getBellyDataItem(belly, "location", Material.ARROW));

        for (int loop = 0; loop < 2; loop++) {
            fillerMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(String.valueOf(fillerAttribute), 0, AttributeModifier.Operation.ADD_NUMBER));
            filler.setItemMeta(fillerMeta);
            inv.addItem(filler);
            fillerAttribute++;
        }

        inv.addItem(GUIUtil.getBellyDataItem(belly, "digestInitMessage", Material.WARPED_SIGN));

        for (int loop = 0; loop < 2; loop++) {
            fillerMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(String.valueOf(fillerAttribute), 0, AttributeModifier.Operation.ADD_NUMBER));
            filler.setItemMeta(fillerMeta);
            inv.addItem(filler);
            fillerAttribute++;
        }

        inv.addItem(GUIUtil.getBellyDataItem(belly, "acidStrength", Material.POTION));

        for (int loop = 0; loop < 2; loop++) {
            fillerMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(String.valueOf(fillerAttribute), 0, AttributeModifier.Operation.ADD_NUMBER));
            filler.setItemMeta(fillerMeta);
            inv.addItem(filler);
            fillerAttribute++;
        }

        inv.addItem(GUIUtil.getBellyDataItem(belly, "type", Material.ARMOR_STAND));

        for (int loop = 0; loop < 2; loop++) {
            fillerMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(String.valueOf(fillerAttribute), 0, AttributeModifier.Operation.ADD_NUMBER));
            filler.setItemMeta(fillerMeta);
            inv.addItem(filler);
            fillerAttribute++;
        }

        inv.addItem(GUIUtil.getBellyDataItem(belly, "digestMessage", Material.CRIMSON_SIGN));

        for (int loop = 0; loop < 2; loop++) {
            fillerMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(String.valueOf(fillerAttribute), 0, AttributeModifier.Operation.ADD_NUMBER));
            filler.setItemMeta(fillerMeta);
            inv.addItem(filler);
            fillerAttribute++;
        }

        inv.addItem(GUIUtil.getBellyDataItem(belly, "prey", Material.CHEST));

        for (int loop = 0; loop < 5; loop++) {
            fillerMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(String.valueOf(fillerAttribute), 0, AttributeModifier.Operation.ADD_NUMBER));
            filler.setItemMeta(fillerMeta);
            inv.addItem(filler);
            fillerAttribute++;
        }

        inv.addItem(GUIUtil.getBellyDataItem(belly, "releaseMessage", Material.JUNGLE_SIGN));

        for (int loop = 0; loop < 13; loop++) {
            fillerMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(String.valueOf(fillerAttribute), 0, AttributeModifier.Operation.ADD_NUMBER));
            filler.setItemMeta(fillerMeta);
            inv.addItem(filler);
            fillerAttribute++;
        }

        p.openInventory(inv);
    }

    /**
     * method handling click behavior
     * @param e InventoryClickEvent
     */
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
    }
}
