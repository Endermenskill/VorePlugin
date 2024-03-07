package me.endermenskill.voreplugin.vore.reformation;

import me.endermenskill.voreplugin.player.PlayerRank;
import me.endermenskill.voreplugin.player.PlayerUtil;
import me.endermenskill.voreplugin.vore.VoreManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ReformTabCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length > 1) {
            return null;
        }

        if (!(commandSender instanceof Player)) {
            return null;
        }

        Player p = (Player)commandSender;

        if (PlayerUtil.getPlayerRank(p) == PlayerRank.PREY) {
            return null;
        }

        ArrayList<String> list = new ArrayList<>();
        for (Player prey : VoreManager.getPrey(p)) {
            if (VoreManager.isDigeted(prey)) {
                list.add(prey.getName());
            }
        }

        return list;
    }
}
