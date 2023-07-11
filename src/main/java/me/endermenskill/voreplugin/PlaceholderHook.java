package me.endermenskill.voreplugin;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.endermenskill.voreplugin.player.PlayerRank;
import me.endermenskill.voreplugin.player.PlayerUtil;
import me.endermenskill.voreplugin.vore.VoreManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle the PAPI integration
 */
class PlaceholderHook extends PlaceholderExpansion {
    public PlaceholderHook(VorePlugin plugin) {
        this.plugin = plugin;
    }
    private final VorePlugin plugin;

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "vore";
    }

    @Override
    public @NotNull String getAuthor() {
        return this.plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    @Override
    public @NotNull List<String> getPlaceholders() {
        List<String> placeholders = new ArrayList<>();

        placeholders.add("rank");
        placeholders.add("pred");
        placeholders.add("prey");

        return placeholders;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        return switch (params) {
            case "rank" -> rankRequest(player);
            case "pred" -> predRequest(player);
            case "prey" -> preyRequest(player);
            default -> null;
        };
    }

    private String rankRequest(Player player) {
        PlayerRank rank = PlayerUtil.getPlayerRank(player);
        switch (rank) {
            case SWITCH -> {
                return "§6[§1SWITCH§6]§r";
            }
            case PREDATOR -> {
                return "§6[§cPRED§6]§r";
            }
            case PREY -> {
                return "§6[§aPREY§6]§r";
            }
        }
        return null;
    }

    private String predRequest(Player player) {
        Player pred = VoreManager.getPredator(player);
        if (pred != null) {
            return pred.getDisplayName();
        }
        return "nobody";
    }

    private String preyRequest(Player player) {
        ArrayList<Player> prey = VoreManager.getPrey(player);
        if (!prey.isEmpty()) {
            return prey.toString();
        }
        return "nobody";
    }
}
