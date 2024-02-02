package me.endermenskill.voreplugin.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public interface InputDialogue extends Listener {

    default void createDialogue(Player p, String title, String msg) {
        ItemStack item = new ItemStack(Material.WRITABLE_BOOK);
        BookMeta meta = (BookMeta) item.getItemMeta();
        assert meta != null;

        meta.setTitle(title);
        meta.setPage(1, msg + ":\n\n");

        item.setItemMeta(meta);
        p.openBook(item);
    }

    @EventHandler
    default void onBookClose(PlayerEditBookEvent e) {
        Player p = e.getPlayer();

        e.setCancelled(true);
        BookMeta meta = e.getNewBookMeta();

        StringBuilder output = new StringBuilder();
        for (String page: meta.getPages()) {
            output.append(page);
        }

        String result = output.toString();

        //Now do your own logic :)
        p.sendMessage(result);
    }
}
