package me.endermenskill.voreplugin.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerChatInputEvent extends Event {
    private final HandlerList handlerList = new HandlerList();
    private UUID player = null;
    private String msg = null;
    private String context = null;

    /**
     * Class constructor for the event
     * @param p player that entered the input
     * @param msg message that the player sent
     * @param context context used to identify input purpose
     */
    public PlayerChatInputEvent(@NotNull Player p, @NotNull String msg, @NotNull String context) {
        this.player = p.getUniqueId();
        this.msg = msg;
        this.context = context;
    }

    /**
     * Get the player that sent the message
     * @return Player p
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(this.player);
    }

    /**
     * Get the message sent by the player
     * @return String msg
     */
    public String getMsg() {
        return this.msg;
    }

    /**
     * Get the context for the chat input
     * @return String context
     */
    public String getContext() {
        return this.context;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
