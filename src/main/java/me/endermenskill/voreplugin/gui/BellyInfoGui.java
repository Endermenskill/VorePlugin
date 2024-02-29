package me.endermenskill.voreplugin.gui;

import me.endermenskill.voreplugin.belly.Belly;
import me.endermenskill.voreplugin.vore.VoreManager;
import me.endermenskill.voreplugin.vore.VoreType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class BellyInfoGui implements Listener, InputDialogue {

    final String bookTitle = "edit belly setting;";

    /**
     * Method to create the GUI
     * @param p Affected Player
     * @param bellyName Name of the belly to inspect
     */
    public static void create(Player p, String bellyName) {
        if (bellyName == null || bellyName.isBlank()) {
            return;
        }

        Belly belly = VoreManager.getBelly(p, bellyName);
        assert belly != null;

        Inventory inv = Bukkit.createInventory(p, 54, "Information for belly \"" + belly.getName() + "\"");

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
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().startsWith("Information for belly \"")) {
            return;
        }

        e.setCancelled(true);
        ItemStack item = e.getCurrentItem();

        if (item == null) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        if (meta.hasAttributeModifiers()) {
            return;
        }

        String bellyName = e.getView().getTitle().replaceAll("Information for belly ", "").replaceAll("\"", "");

        String dataType = meta.getDisplayName().replaceAll("§d", "").replaceAll(":", "");

        String title = bookTitle;
        title += bellyName;
        title += dataType;

        String msg = "Enter new ";
        msg += dataType;
        msg += " for the belly:";

        createDialogue((Player) e.getWhoClicked(), title, msg);
    }

    @EventHandler
    public void onBookClose(PlayerEditBookEvent e) {
        Player p = e.getPlayer();
        BookMeta meta = e.getNewBookMeta();
        String title = meta.getTitle();
        assert title != null;

        if (!title.startsWith(bookTitle)) {
            return;
        }

        e.setCancelled(true);
        StringBuilder output = new StringBuilder();
        for (String page: meta.getPages()) {
            output.append(page);
        }

        String[] splitString = output.toString().split(":");
        StringBuilder rawResult = new StringBuilder();
        for (int i = 1; i < splitString.length; i++) {
            rawResult.append(splitString[i]);
        }
        String result = rawResult.toString();

        String[] splitTitle = title.split(";");
        String bellyName = splitTitle[1];
        String dataType = splitTitle[2];
        Belly belly = VoreManager.getBelly(p, bellyName);

        if (belly == null) {
            p.sendMessage("Error fetching belly to modify");
            return;
        }

        switch(dataType) {
            case "name": {
                belly.setName(result);
            }
            case "type": {
                try {
                    VoreType type = VoreType.valueOf(result.toUpperCase());
                    belly.setType(type);
                } catch (IllegalArgumentException ex) {
                    p.sendMessage(ChatColor.RED + "Invalid vore type");
                }
            }
            case "swallowMessage": {
                belly.setSwallowMessage(result);
            }
            case "digestInitMessage": {
                belly.setDigestInitMessage(result);
            }
            case "digestMessage": {
                belly.setDigestMessage(result);
            }
            case "releaseMessage": {
                belly.setReleaseMessage(result);
            }
            case "bellyEffect": {
                p.sendMessage(ChatColor.YELLOW + "This feature is currently disabled. Maybe changing the ambient effect will be a thing for future versions.");
            }
            case "acidStrength": {
                try {
                    int strength = Integer.parseInt(result);
                    if (strength > 3){
                        strength = 3;
                    }
                    if (strength < 1) {
                        strength = 1;
                    }
                    belly.setAcidStrength(strength);
                } catch (NumberFormatException ex) {
                    p.sendMessage(ChatColor.RED + "Error formatting Integer from String");
                }
            }
        }
    }
}
