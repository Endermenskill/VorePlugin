package me.endermenskill.voreplugin.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

/**
 * Interface designed to be implemented in GUI handling classes that require player input.
 */
public interface InputDialogue extends Listener {

    /**
     * Method to create the book used as text input for the player.
     * @param p Player to open the GUI for
     * @param title Title of the book (can be sed as an identifier)
     * @param msg Short message to display in the Book to give guidance on what to enter
     */
    default void createDialogue(Player p, String title, String msg) {
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) item.getItemMeta();
        assert meta != null;

        meta.setTitle(title);
        meta.setAuthor("Your Mother");
        meta.setGeneration(BookMeta.Generation.TATTERED);
        meta.addPage(msg);

        item.setItemMeta(meta);
        p.openBook(item);
    }

    /**
     * Method to catch the player closing a book that is being used as player input for a GUI.
     * Override this method in your GUI handling class to implement the required logic to get the player's input.
     * @param e PlayerEditBookEvent e
     */
    @EventHandler
    default void onBookClose(PlayerEditBookEvent e) {
        Player p = e.getPlayer();
        String titleIdentifier = "title you use to identify the book";

        BookMeta meta = e.getNewBookMeta();
        assert meta.getTitle() != null;

        if (!meta.getTitle().equals(titleIdentifier)) {
            return;
        }

        e.setCancelled(true);
        //TODO: close book GUI if it doesn't do it on its own

        StringBuilder output = new StringBuilder();
        for (String page: meta.getPages()) {
            output.append(page);
        }

        String result = output.toString().replaceFirst("mesage you put in the book during its creation", "");

        //Now do your own logic :)
        p.sendMessage(result);
    }
}
